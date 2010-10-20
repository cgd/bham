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

package org.jax.bham.project;

import java.awt.Frame;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jax.bham.BhamApplication;
import org.jax.util.TextWrapper;
import org.jax.util.project.AbstractSaveProjectAction;
import org.jax.util.project.ProjectManager;

/**
 * Action that allows the user to do a "Save As" for thier BHAM project.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SaveBhamProjectAsAction extends AbstractSaveProjectAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 2307880919839710575L;
    
    /**
     * the name the user sees
     */
    private static final String ACTION_NAME = "Save Project As...";
    
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            SaveBhamProjectAsAction.class.getName());

    /**
     * Constructor
     */
    public SaveBhamProjectAsAction()
    {
        super(ACTION_NAME);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Frame getParentFrame()
    {
        return BhamApplication.getInstance().getBhamFrame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ProjectManager getProjectManager()
    {
        return BhamProjectManager.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performSave()
    {
        // try to be smart about the file dialogs starting file
        ProjectManager projectManager = this.getProjectManager();
        File activeProjFile = projectManager.getActiveProjectFile();
        
        // select the project file to save
        JFileChooser fileChooser = new JFileChooser(activeProjFile);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setApproveButtonText("Save BHAM Project");
        fileChooser.setDialogTitle("Save BHAM Project");
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.addChoosableFileFilter(
                projectManager.getProjectFileFilter());
        fileChooser.setFileFilter(
                projectManager.getProjectFileFilter());
        int response = fileChooser.showSaveDialog(this.getParentFrame());
        if(response == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            
            // tack on the project extension if there isn't one
            // already
            String dotBham = "." + BhamProjectManager.BHAM_PROJECT_EXTENSION;
            if(!selectedFile.exists() && !selectedFile.getName().toLowerCase().endsWith(dotBham))
            {
                String newFileName =
                    selectedFile.getName() + "." + BhamProjectManager.BHAM_PROJECT_EXTENSION;
                selectedFile =
                    new File(selectedFile.getParentFile(), newFileName);
            }
            
            if(selectedFile.exists() && !selectedFile.equals(activeProjFile))
            {
                // ask the user if they're sure they want to overwrite
                String message =
                    "Saving the current BHAM project to " +
                    selectedFile.getAbsolutePath() + " will overwrite an " +
                    " existing file. Would you like to continue anyway?";
                if(LOG.isLoggable(Level.FINE))
                {
                    LOG.fine(message);
                }
                
                int overwriteResponse = JOptionPane.showConfirmDialog(
                        this.getParentFrame(),
                        TextWrapper.wrapText(
                                message,
                                TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                        "Overwriting Existing File",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(overwriteResponse != JOptionPane.OK_OPTION)
                {
                    if(LOG.isLoggable(Level.FINE))
                    {
                        LOG.fine("overwrite canceled");
                    }
                    return;
                }
            }
            
            if(!projectManager.saveActiveProject(selectedFile))
            {
                // there was a problem... tell the user
                String message =
                    "Failed to save to selected BHAM project file: " +
                    selectedFile.getAbsolutePath();
                LOG.info(message);
                
                JOptionPane.showMessageDialog(
                        this.getParentFrame(),
                        TextWrapper.wrapText(
                                message,
                                TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                        "Error Saving Project",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
