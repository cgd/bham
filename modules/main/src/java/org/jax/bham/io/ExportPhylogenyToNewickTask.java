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

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jax.bham.BhamApplication;
import org.jax.geneticutil.data.BasePairInterval;
import org.jax.haplotype.analysis.PhylogenyDataSource;
import org.jax.haplotype.phylogeny.data.PhylogenyInterval;
import org.jax.haplotype.phylogeny.data.PhylogenyTreeNode;
import org.jax.util.concurrent.AbstractLongRunningTask;
import org.jax.util.gui.MessageDialogUtilities;
import org.jax.util.io.FlatFileWriter;

/**
 * Export the given phylogeny tree to newick format
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ExportPhylogenyToNewickTask
extends AbstractLongRunningTask
implements Runnable
{
    private static final Logger LOG = Logger.getLogger(
            ExportPhylogenyToNewickTask.class.getName());
    
    private final PhylogenyDataSource phylogenyData;
    
    private final FlatFileWriter flatFileWriter;
    
    private volatile int workUnitsCompleted = 0;

    private final boolean fullyResolveTrees;

    private final boolean closeWriterWhenFinished;
    
    /**
     * Constructor
     * @param phylogenyData
     *          the phylogeny data to export
     * @param fullyResolveTrees
     *          if true than strains on the same node will be given a
     *          pseudo branch with length 0, otherwise they will just be
     *          separated with a '|'
     * @param flatFileWriter
     *          the flat file that we're writing to
     * @param closeWriterWhenFinished
     *          should the given writer be closed when this task is done
     *          writing to it?
     */
    public ExportPhylogenyToNewickTask(
            PhylogenyDataSource phylogenyData,
            boolean fullyResolveTrees,
            FlatFileWriter flatFileWriter,
            boolean closeWriterWhenFinished)
    {
        this.phylogenyData = phylogenyData;
        this.fullyResolveTrees = fullyResolveTrees;
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
            Map<Integer, List<PhylogenyInterval>> phylogenyIntervals =
                this.phylogenyData.getPhylogenyData(null);
            
            this.flatFileWriter.writeRow(new String[] {
                    "chromosomeNumber",
                    "phylogenyIntervalStartPositionInBasePairs",
                    "phylogenyIntervalEndPositionInBasePairs",
                    "newickFormattedPhylogenyTree"});
            for(List<PhylogenyInterval> currPhyloIntervals: phylogenyIntervals.values())
            {
                for(PhylogenyInterval currPhyloInterval: currPhyloIntervals)
                {
                    BasePairInterval interval = currPhyloInterval.getInterval();
                    PhylogenyTreeNode phyloTree = currPhyloInterval.getPhylogeny();
                    if(this.fullyResolveTrees)
                    {
                        phyloTree = phyloTree.resolveToSingleStrainLeafNodes(0.0);
                    }
                    
                    this.flatFileWriter.writeRow(new String[] {
                            Integer.toString(interval.getChromosomeNumber()),
                            Long.toString(interval.getStartInBasePairs()),
                            Long.toString(interval.getEndInBasePairs()),
                            phyloTree.toNewickFormat()});
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
            String title = "Failed to Export Phylogeny trees";
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
        return "Exporting " + this.phylogenyData.getName();
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
