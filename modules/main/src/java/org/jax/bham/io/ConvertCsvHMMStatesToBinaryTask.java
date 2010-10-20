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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jax.bham.BhamApplication;
import org.jax.haplotype.data.BinaryMultiGroupHaplotypeDataSource;
import org.jax.haplotype.io.HiddenMarkovModelStateParser;
import org.jax.util.concurrent.AbstractLongRunningTask;
import org.jax.util.gui.MessageDialogUtilities;

/**
 * Long running task for converting CSV HMM data to binary data
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ConvertCsvHMMStatesToBinaryTask
extends AbstractLongRunningTask
implements Runnable
{
    private static final Logger LOG = Logger.getLogger(
            ConvertCsvHMMStatesToBinaryTask.class.getName());
    
    private static final String TASK_NAME = "Converting HMM States";
    
    private final File[] importFiles;
    
    private final File exportDirectory;
    
    private volatile int importFilesProcessed = 0;

    private final HiddenMarkovModelStateParser parser;
    
    /**
     * Constructor
     * @param parser
     *          the parser to use
     * @param importFiles
     *          the files to import
     * @param exportDirectory
     *          the directory to export to
     */
    public ConvertCsvHMMStatesToBinaryTask(
            HiddenMarkovModelStateParser parser,
            File[] importFiles,
            File exportDirectory)
    {
        this.parser = parser;
        this.importFiles = importFiles;
        this.exportDirectory = exportDirectory;
    }

    /**
     * {@inheritDoc}
     */
    public String getTaskName()
    {
        return TASK_NAME;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getTotalWorkUnits()
    {
        return this.importFiles.length;
    }
    
    /**
     * {@inheritDoc}
     */
    public int getWorkUnitsCompleted()
    {
        return this.importFilesProcessed;
    }
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
        try
        {
            for(File importFile: this.importFiles)
            {
                BinaryMultiGroupHaplotypeDataSource.writeHMMStatesAsBinaryData(
                        this.parser,
                        importFile,
                        this.exportDirectory);
                this.importFilesProcessed++;
                this.fireChangeEvent();
            }
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE,
                    "failed to import HMM data",
                    ex);
            MessageDialogUtilities.errorLater(
                    BhamApplication.getInstance().getBhamFrame(),
                    ex.getMessage(),
                    "Failed To Import HMM Data");
            
            // it might not be pretty, but it is complete...
            this.importFilesProcessed = this.getTotalWorkUnits();
            this.fireChangeEvent();
        }
    }
}
