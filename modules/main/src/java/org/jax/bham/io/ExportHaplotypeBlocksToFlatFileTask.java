/*
 * Copyright (c) 2010 The Jackson Laboratory
 * 
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jax.bham.io;

import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jax.bham.BhamApplication;
import org.jax.geneticutil.data.BasePairInterval;
import org.jax.geneticutil.data.PartitionedIntervalSet;
import org.jax.haplotype.analysis.experimentdesign.HaplotypeDataSource;
import org.jax.util.concurrent.AbstractLongRunningTask;
import org.jax.util.datastructure.SequenceUtilities;
import org.jax.util.datastructure.SetUtilities;
import org.jax.util.gui.MessageDialogUtilities;
import org.jax.util.io.FlatFileWriter;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ExportHaplotypeBlocksToFlatFileTask
extends AbstractLongRunningTask
implements Runnable
{
    private static final Logger LOG = Logger.getLogger(
            ExportHaplotypeBlocksToFlatFileTask.class.getName());
    
    private final HaplotypeDataSource haplotypeData;
    
    private final FlatFileWriter flatFileWriter;
    
    private final boolean closeWriterWhenFinished;
    
    private volatile int workUnitsCompleted = 0;

    /**
     * Constructor
     * @param haplotypeData
     *          the haplotype data to export
     * @param flatFileWriter
     *          the flat file that we're writing to
     * @param closeWriterWhenFinished
     *          should the given writer be closed when this task is done
     *          writing to it?
     */
    public ExportHaplotypeBlocksToFlatFileTask(
            HaplotypeDataSource haplotypeData,
            FlatFileWriter flatFileWriter,
            boolean closeWriterWhenFinished)
    {
        this.haplotypeData = haplotypeData;
        this.flatFileWriter = flatFileWriter;
        this.closeWriterWhenFinished = closeWriterWhenFinished;
    }
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
        try
        {
            List<PartitionedIntervalSet> haplotypeBlocks =
                this.haplotypeData.getHaplotypeEquivalenceClassData(null);
            
            Set<String> strains = this.haplotypeData.getAvailableStrains();
            String[] sortedStrains = strains.toArray(new String[strains.size()]);
            Arrays.sort(sortedStrains);
            
            Writer plainWriter = this.flatFileWriter.getWriter();
            plainWriter.write("# Haplotype strains (same ordering as bit set)\n");
            String strainsString = SequenceUtilities.toString(
                    Arrays.asList(sortedStrains),
                    ", ");
            plainWriter.write("# " + strainsString + "\n");
            
            this.flatFileWriter.writeRow(new String[] {
                    "chromosomeNumber",
                    "haplotypeBlockStartPositionInBasePairs",
                    "haplotypeBlockEndPositionInBasePairs",
                    "strainsInHaplotypeBlockBitSet"});
            for(PartitionedIntervalSet haploEquivClass: haplotypeBlocks)
            {
                for(BasePairInterval interval: haploEquivClass.getSnpIntervals())
                {
                    this.flatFileWriter.writeRow(new String[] {
                            Integer.toString(interval.getChromosomeNumber()),
                            Long.toString(interval.getStartInBasePairs()),
                            Long.toString(interval.getEndInBasePairs()),
                            SetUtilities.bitSetToBinaryString(haploEquivClass.getStrainBitSet())});
                }
            }
            this.flatFileWriter.flush();
            
            if(this.closeWriterWhenFinished)
            {
                this.flatFileWriter.close();
            }
        }
        catch(Exception ex)
        {
            String title = "Failed to Export Haplotype Blocks";
            LOG.log(Level.SEVERE,
                    title,
                    ex);
            MessageDialogUtilities.errorLater(
                    BhamApplication.getInstance().getBhamFrame(),
                    ex.getMessage(),
                    title);
        }
        finally
        {
            // no matter what we need to finish up
            this.workUnitsCompleted = 1;
            this.fireChangeEvent();
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getTaskName()
    {
        return "Exporting " + this.haplotypeData.getName();
    }

    /**
     * {@inheritDoc}
     */
    public int getTotalWorkUnits()
    {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getWorkUnitsCompleted()
    {
        return this.workUnitsCompleted;
    }
}
