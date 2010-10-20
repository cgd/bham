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
import org.jax.haplotype.analysis.experimentdesign.MultiGroupHaplotypeAssociationTest;
import org.jax.util.gui.MessageDialogUtilities;
import org.jax.util.gui.desktoporganization.Desktop;

/**
 * Graph a given mult group association test
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PlotSpecificMultiGroupHaplotypeAssociationTestAction extends AbstractAction
{
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            PlotSpecificMultiGroupHaplotypeAssociationTestAction.class.getName());
    
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -9054618788766176542L;
    
    private final MultiGroupHaplotypeAssociationTest testToPlot;
    
    /**
     * Constructor
     * @param testToPlot
     *          the test that we want to plot
     */
    public PlotSpecificMultiGroupHaplotypeAssociationTestAction(
            MultiGroupHaplotypeAssociationTest testToPlot)
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
            MultiHaplotypeBlockTestGraphPanel hapAssocGraphPanel =
                new MultiHaplotypeBlockTestGraphPanel(this.testToPlot);
            Desktop desktop = BhamApplication.getInstance().getBhamFrame().getDesktop();
            desktop.createInternalFrame(
                    hapAssocGraphPanel,
                    "Multi-Group Haplotype Association",
                    null,
                    "multi group assoc graph: " + this.testToPlot.getName());
        }
        catch(Exception ex)
        {
            String title = "Error Plotting Multi-Group Association Test";
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
