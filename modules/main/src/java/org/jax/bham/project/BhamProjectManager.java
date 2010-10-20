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

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jax.bham.BhamApplication;
import org.jax.haplotype.jaxbgenerated.HaplotypeAssociationExperimentDesign;
import org.jax.haplotype.jaxbgenerated.ObjectFactory;
import org.jax.util.gui.MessageDialogUtilities;
import org.jax.util.io.FileChooserExtensionFilter;
import org.jax.util.project.Project;
import org.jax.util.project.ProjectManager;


/**
 * A project manager for BHAM! Note that this project manager does not extend
 * {@link org.jax.util.project.ProjectManager} because it cannot conform
 * to that class's assumptions
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class BhamProjectManager extends ProjectManager
{
    /**
     * the singleton instance
     */
    private static final BhamProjectManager instance =
        new BhamProjectManager();
    
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            BhamProjectManager.class.getName());
    
    /**
     * The extension that should be used for bham projects
     */
    public static final String BHAM_PROJECT_EXTENSION = "bham";

    private static final FileFilter BHAM_PROJECT_FILE_FILTER =
        new FileChooserExtensionFilter(
                BHAM_PROJECT_EXTENSION,
                "BHAM Project (*.bham)");
    
    private final ObjectFactory objectFactory;
    
    private JAXBContext jaxbContext;
    
    /**
     * Constructor
     */
    public BhamProjectManager()
    {
        this.objectFactory = new ObjectFactory();
        
        try
        {
            this.jaxbContext = JAXBContext.newInstance(
                    HaplotypeAssociationExperimentDesign.class);
        }
        catch(JAXBException ex)
        {
            LOG.log(Level.SEVERE,
                    "failed to initialize project manager",
                    ex);
        }
        
        this.createNewActiveProject();
    }
    
    /**
     * Get the singleton instance of this project manager
     * @return
     *          the instance
     */
    public static BhamProjectManager getInstance()
    {
        return BhamProjectManager.instance;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Project createNewActiveProject()
    {
        this.setActiveProjectFile(null);
        this.setActiveProjectModified(false);
        
        BhamProject newProject = new BhamProject(null);
        this.setActiveProject(newProject);
        
        return newProject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileFilter getProjectFileFilter()
    {
        return BHAM_PROJECT_FILE_FILTER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean loadActiveProject(File projectFile)
    {
        try
        {
            System.out.println("loading active project from: " + projectFile);
            
            // create the project
            BhamProject bhamProject = new BhamProject(null);
            bhamProject.loadProjectFromFile(projectFile);
            
            // update and notify
            this.setActiveProjectFile(projectFile);
            this.setActiveProjectModified(false);
            this.setActiveProject(bhamProject);
            
            return true;
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE,
                    "failed to load project file: " + projectFile.getPath(),
                    ex);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshProjectDataStructures()
    {
        // nothing to do here. project data structures are always up to date
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveActiveProject(File projectFile)
    {
        BhamProject activeProject = this.getActiveProject();
        
        if(activeProject == null)
        {
            String title = "No Active Project";
            String message =
                "Cannot save project because there is no active " +
                "project to save";
            LOG.severe(message);
            MessageDialogUtilities.errorLater(
                    BhamApplication.getInstance().getBhamFrame(),
                    message,
                    title);
            
            return false;
        }
        else
        {
            try
            {
                activeProject.saveProjectToFile(projectFile);
                
                return true;
            }
            catch(Exception ex)
            {
                String title = "Error Saving Project File";
                LOG.log(Level.SEVERE,
                        title,
                        ex);
                MessageDialogUtilities.error(
                        BhamApplication.getInstance().getBhamFrame(),
                        ex.getMessage(),
                        title);
                
                return false;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BhamProject getActiveProject()
    {
        return (BhamProject)super.getActiveProject();
    }
}
