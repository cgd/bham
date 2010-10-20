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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jax.bham.BhamApplication;
import org.jax.haplotype.analysis.experimentdesign.PhylogenyAssociationTest;
import org.jax.util.gui.MessageDialogUtilities;
import org.jax.util.gui.desktoporganization.Desktop;

/**
 * Graph a given phylogeny association test
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PlotSpecificPhylogenyAssociationTestAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 63789486393289116L;

    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            PlotSpecificPhylogenyAssociationTestAction.class.getName());
    
    private final PhylogenyAssociationTest testToPlot;
    
    /**
     * Constructor
     * @param testToPlot
     *          the test that we want to plot
     */
    public PlotSpecificPhylogenyAssociationTestAction(
            PhylogenyAssociationTest testToPlot)
    {
        super("Plot " + testToPlot.getName());
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
        this.testToPlot = testToPlot;
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            PhylogenyAssociationTestGraphPanel phyloAssocGraphPanel =
                new PhylogenyAssociationTestGraphPanel(this.testToPlot);
            Desktop desktop = BhamApplication.getInstance().getBhamFrame().getDesktop();
            desktop.createInternalFrame(
                    phyloAssocGraphPanel,
                    "Phylogeny Association",
                    null,
                    "phylo assoc graph: " + this.testToPlot.getName());
        }
        catch(Exception ex)
        {
            String title = "Error Plotting Phylogeny Association Test";
            LOG.log(Level.SEVERE,
                    title,
                    ex);
            MessageDialogUtilities.errorLater(
                    BhamApplication.getInstance().getBhamFrame(),
                    ex.getMessage(),
                    title);
        }
    }
}
