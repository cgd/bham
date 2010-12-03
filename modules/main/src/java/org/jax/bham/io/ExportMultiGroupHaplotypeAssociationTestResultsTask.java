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
import org.jax.haplotype.analysis.MultiGroupHaplotypeAssociationTest;
import org.jax.haplotype.analysis.MultiHaplotypeBlockTestResult;
import org.jax.util.concurrent.AbstractLongRunningTask;
import org.jax.util.datastructure.SequenceUtilities;
import org.jax.util.gui.MessageDialogUtilities;
import org.jax.util.io.FlatFileWriter;

/**
 * A long running task for exporting haplotype association test data
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ExportMultiGroupHaplotypeAssociationTestResultsTask
extends AbstractLongRunningTask
implements Runnable
{
    private static final Logger LOG = Logger.getLogger(
            ExportMultiGroupHaplotypeAssociationTestResultsTask.class.getName());
    
    private final MultiGroupHaplotypeAssociationTest haplotypeTest;
    
    private final FlatFileWriter flatFileWriter;
    
    private final boolean closeWriterWhenFinished;
    
    private volatile int workUnitsCompleted = 0;

    /**
     * Constructor
     * @param haplotypeTest
     *          the haplotype association test data to export
     * @param flatFileWriter
     *          the flat file that we're writing to
     * @param closeWriterWhenFinished
     *          should the given writer be closed when this task is done
     *          writing to it?
     */
    public ExportMultiGroupHaplotypeAssociationTestResultsTask(
            MultiGroupHaplotypeAssociationTest haplotypeTest,
            FlatFileWriter flatFileWriter,
            boolean closeWriterWhenFinished)
    {
        this.haplotypeTest = haplotypeTest;
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
            // write a comment for the haplotype strains
            {
                Set<String> haploStrains =
                    this.haplotypeTest.getHaplotypeDataSource().getAvailableStrains();
                String[] sortedHaploStrains =
                    haploStrains.toArray(new String[haploStrains.size()]);
                Arrays.sort(sortedHaploStrains);
                
                Writer plainWriter = this.flatFileWriter.getWriter();
                plainWriter.write("# Haplotype strains (same ordering as bit set):\n");
                String strainsString = SequenceUtilities.toString(
                        Arrays.asList(sortedHaploStrains),
                        ", ");
                plainWriter.write("# " + strainsString + "\n");
            }
            
            // write a comment for the phenotype strains
            {
                Set<String> phenoStrains =
                    this.haplotypeTest.getPhenotypeDataSource().getPhenotypeData().keySet();
                String[] sortedPhenoStrains =
                    phenoStrains.toArray(new String[phenoStrains.size()]);
                Arrays.sort(sortedPhenoStrains);
                
                Writer plainWriter = this.flatFileWriter.getWriter();
                plainWriter.write("# Phenotype strains:\n");
                String strainsString = SequenceUtilities.toString(
                        Arrays.asList(sortedPhenoStrains),
                        ", ");
                plainWriter.write("# " + strainsString + "\n");
            }
            
            // write a comment for the common strains
            {
                Set<String> commonStrains =
                    this.haplotypeTest.getCommonStrains();
                String[] sortedCommonStrains =
                    commonStrains.toArray(new String[commonStrains.size()]);
                Arrays.sort(sortedCommonStrains);
                
                Writer plainWriter = this.flatFileWriter.getWriter();
                plainWriter.write("# Common strains (only common strains can be tested):\n");
                String strainsString = SequenceUtilities.toString(
                        Arrays.asList(sortedCommonStrains),
                        ", ");
                plainWriter.write("# " + strainsString + "\n");
            }
            
            // write the results row by row
            this.flatFileWriter.writeRow(new String[] {
                    "chromosomeNumber",
                    "haplotypeBlockStartPositionInBasePairs",
                    "haplotypeBlockEndPositionInBasePairs",
                    "strainGroupingsInHaplotypeBlock",
                    "pValue"});
            int[] chromosomes = this.haplotypeTest.getAvailableChromosomes();
            for(int chromosome: chromosomes)
            {
                MultiHaplotypeBlockTestResult[] testResults =
                    this.haplotypeTest.getTestResults(chromosome);
                for(MultiHaplotypeBlockTestResult currResult: testResults)
                {
                    List<Short> currGroups = SequenceUtilities.toShortList(
                            currResult.getStrainGroups());
                    this.flatFileWriter.writeRow(new String[] {
                            Integer.toString(currResult.getChromosomeNumber()),
                            Long.toString(currResult.getStartInBasePairs()),
                            Long.toString(currResult.getEndInBasePairs()),
                            SequenceUtilities.toString(currGroups, ","),
                            Double.toString(currResult.getPValue())});
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
            String title = "Failed to Export Haplotype Test Results";
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
        return "Exporting " + this.haplotypeTest.getName();
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
