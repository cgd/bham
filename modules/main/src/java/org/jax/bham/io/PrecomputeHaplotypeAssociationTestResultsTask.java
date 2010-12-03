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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jax.bham.BhamApplication;
import org.jax.haplotype.analysis.CachingHaplotypeAssociationTest;
import org.jax.util.concurrent.AbstractLongRunningTask;
import org.jax.util.gui.MessageDialogUtilities;

/**
 * A long running task for precomputing haplotype association test results
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PrecomputeHaplotypeAssociationTestResultsTask
extends AbstractLongRunningTask
implements Runnable
{
    private static final Logger LOG = Logger.getLogger(
            PrecomputeHaplotypeAssociationTestResultsTask.class.getName());
    
    private final CachingHaplotypeAssociationTest haplotypeTest;
    
    private volatile int workUnitsCompleted = 0;

    /**
     * Constructor
     * @param haplotypeTest
     *          the haplotype association test data to export
     */
    public PrecomputeHaplotypeAssociationTestResultsTask(
            CachingHaplotypeAssociationTest haplotypeTest)
    {
        this.haplotypeTest = haplotypeTest;
    }
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
        try
        {
            this.haplotypeTest.getEquivalenceClassTestResults();
        }
        catch(Exception ex)
        {
            String title = "Failed to Precompute Haplotype Test Results";
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
        return "Precomputing Results For: " + this.haplotypeTest.getName();
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
