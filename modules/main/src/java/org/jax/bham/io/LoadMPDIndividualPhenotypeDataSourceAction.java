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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jax.bham.BhamApplication;
import org.jax.bham.project.BhamProjectManager;

/**
 * The action class for loading phenotype data into the application
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class LoadMPDIndividualPhenotypeDataSourceAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 3302178280957977276L;

    /**
     * Constructor
     */
    public LoadMPDIndividualPhenotypeDataSourceAction()
    {
        super("Load Individual Phenotype Data (MPD Format)...");
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        LoadMPDIndividualPhenotypeDataSourceDialog loadPhenoDialog =
            new LoadMPDIndividualPhenotypeDataSourceDialog(
                    BhamApplication.getInstance().getBhamFrame(),
                    BhamProjectManager.getInstance().getActiveProject());
        loadPhenoDialog.setVisible(true);
    }
}
