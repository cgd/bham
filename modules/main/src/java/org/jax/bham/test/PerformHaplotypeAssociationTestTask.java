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

package org.jax.bham.test;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jax.bham.BhamApplication;
import org.jax.haplotype.analysis.experimentdesign.HaplotypeAssociationTest;
import org.jax.haplotype.analysis.experimentdesign.HaplotypeBlockTestResult;
import org.jax.util.concurrent.AbstractLongRunningTask;
import org.jax.util.gui.MessageDialogUtilities;

/**
 * A long running task that converts the p-values in the given
 * haplotype test results into -log10(p-value) scores
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PerformHaplotypeAssociationTestTask
extends AbstractLongRunningTask
implements Enumeration<HaplotypeBlockTestResult[]>
{
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            PerformHaplotypeAssociationTestTask.class.getName());
    
    private final HaplotypeAssociationTest test;
    
    private final List<Integer> chromosomeNumbers;
    
    private volatile int chromosomeIndex = 0;

    /**
     * Constructor
     * @param test
     *          the association test to convert
     * @param chromosomeNumbers
     *          the chromosome numbers
     */
    public PerformHaplotypeAssociationTestTask(
            HaplotypeAssociationTest test,
            List<Integer> chromosomeNumbers)
    {
        this.test = test;
        this.chromosomeNumbers = chromosomeNumbers;
    }
    
    /**
     * Returns the chromosome number that will be processed on the next call
     * to {@link #nextElement()}
     * @return  the chromosome number
     */
    public int getNextChromosome()
    {
        return this.chromosomeNumbers.get(this.chromosomeIndex);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasMoreElements()
    {
        return !this.isComplete();
    }
    
    /**
     * {@inheritDoc}
     */
    public HaplotypeBlockTestResult[] nextElement()
    {
        HaplotypeBlockTestResult[] testResults = null;
        try
        {
            testResults = this.test.getHaplotypeTestResults(
                    this.getNextChromosome());
            Arrays.sort(testResults);
        }
        catch(Exception ex)
        {
            String errorMsg =
                "Encountered Error During Haplotype Association Test";
            LOG.log(Level.SEVERE,
                    errorMsg,
                    ex);
            MessageDialogUtilities.errorLater(
                    BhamApplication.getInstance().getBhamFrame(),
                    ex.getMessage(),
                    errorMsg);
        }
        finally
        {
            this.chromosomeIndex++;
            this.fireChangeEvent();
        }
        
        return testResults;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getTaskName()
    {
        if(!this.isComplete())
        {
            return
                this.test.getName() + " (Chr " +
                this.getNextChromosome() + ")";
        }
        else
        {
            return "Complete";
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getTotalWorkUnits()
    {
        return this.chromosomeNumbers.size();
    }

    /**
     * {@inheritDoc}
     */
    public int getWorkUnitsCompleted()
    {
        return this.chromosomeIndex;
    }
}
