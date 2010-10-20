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

package org.jax.bham.infer;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jax.bham.BhamApplication;
import org.jax.bham.project.BhamProjectManager;

/**
 * Action to prompt the user with a phylogeny inference dialog
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class InferPhylogenyAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -795544116461287504L;

    /**
     * Constructor
     */
    public InferPhylogenyAction()
    {
        super("Infer Perfect Phylogeny Trees...");
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        InferPhylogenyDialog inferPhyloDialog = new InferPhylogenyDialog(
                BhamApplication.getInstance().getBhamFrame(),
                BhamProjectManager.getInstance().getActiveProject());
        inferPhyloDialog.setVisible(true);
    }
}
