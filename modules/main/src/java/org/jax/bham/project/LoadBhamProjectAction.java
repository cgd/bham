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
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jax.bham.BhamApplication;
import org.jax.util.TextWrapper;
import org.jax.util.project.ProjectManager;

/**
 * An action class for loading a BHAM project file
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class LoadBhamProjectAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 8091426647586900801L;
    
    private static final Logger LOG = Logger.getLogger(
            LoadBhamProjectAction.class.getName());
    
    private static final String ACTION_NAME = "Open BHAM Project...";
    
    /**
     * the icon resource location
     */
    private static final String ICON_RESOURCE_LOCATION =
        "/images/action/open-project-16x16.png";
    
    /**
     * Constructor
     */
    public LoadBhamProjectAction()
    {
        super(ACTION_NAME,
              new ImageIcon(LoadBhamProjectAction.class.getResource(
                      ICON_RESOURCE_LOCATION)));
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        // prompt the user if they're about to lose unsaved changes
        ProjectManager projectManager = BhamProjectManager.getInstance();
        if(projectManager.isActiveProjectModified())
        {
            String message =
                "The current project contains unsaved modifications. Loading " +
                "a new project will cause these modifications to be lost. " +
                "Would you like to continue without saving?";
            int response = JOptionPane.showConfirmDialog(
                    this.getParentFrame(),
                    TextWrapper.wrapText(
                            message,
                            TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                    "Unsaved Project Modifications",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if(response == JOptionPane.CLOSED_OPTION || response == JOptionPane.CANCEL_OPTION)
            {
                return;
            }
        }
        
        // TODO try to be smart about the file dialogs starting dir
        
        // slect the project file to load
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(
                projectManager.getProjectFileFilter());
        fileChooser.setFileFilter(
                projectManager.getProjectFileFilter());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setApproveButtonText("Open BHAM Project");
        fileChooser.setDialogTitle("Open BHAM Project");
        fileChooser.setMultiSelectionEnabled(false);
        int response = fileChooser.showOpenDialog(this.getParentFrame());
        if(response == JFileChooser.APPROVE_OPTION)
        {
            if(!projectManager.loadActiveProject(fileChooser.getSelectedFile()))
            {
                // there was a problem... tell the user
                String message =
                    "Failed to load selected BHAM project file: " +
                    fileChooser.getSelectedFile().getAbsolutePath();
                LOG.info(message);
                
                JOptionPane.showMessageDialog(
                        this.getParentFrame(),
                        TextWrapper.wrapText(
                                message,
                                TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                        "Error Loading Project",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Getter for the parent frame to use for any popups
     * @return  the parent frame
     */
    private Frame getParentFrame()
    {
        return BhamApplication.getInstance().getBhamFrame();
    }
}
