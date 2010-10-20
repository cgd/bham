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

import javax.swing.ImageIcon;

import org.jax.bham.BhamApplication;
import org.jax.util.project.AbstractSaveProjectAction;
import org.jax.util.project.ProjectManager;

/**
 * Save the current BHAM project
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SaveBhamProjectAction extends AbstractSaveProjectAction
{
    /**
     * logger
     */
    private static final Logger LOG = Logger.getLogger(
            SaveBhamProjectAction.class.getName());
    
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -8861769332760442577L;

    /**
     * the name the user sees
     */
    private static final String ACTION_NAME = "Save Project";
    
    /**
     * the icon resource location
     */
    private static final String ICON_RESOURCE_LOCATION =
        "/images/action/save-16x16.png";
    
    /**
     * Constructor
     */
    public SaveBhamProjectAction()
    {
        super(ACTION_NAME,
              new ImageIcon(SaveBhamProjectAction.class.getResource(
                      ICON_RESOURCE_LOCATION)));
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
        ProjectManager projectManager = BhamProjectManager.getInstance();
        File activeProjFile = projectManager.getActiveProjectFile();
        
        if(activeProjFile == null)
        {
            // since we don't have an active file this is the same as a
            // "save as"
            if(LOG.isLoggable(Level.FINE))
            {
                LOG.fine(
                        "calling save as since we don't have an existing " +
                        "file name for the project");
            }
            
            SaveBhamProjectAsAction saveAsAction = new SaveBhamProjectAsAction();
            saveAsAction.actionPerformed(null);
        }
        else
        {
            projectManager.saveActiveProject(activeProjFile);
        }
    }
}
