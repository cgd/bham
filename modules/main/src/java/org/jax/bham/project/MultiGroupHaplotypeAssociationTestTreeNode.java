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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jax.bham.test.PlotSpecificMultiGroupHaplotypeAssociationTestAction;
import org.jax.haplotype.analysis.MultiGroupHaplotypeAssociationTest;

/**
 * Tree node representing a multi-group haplotype association test
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MultiGroupHaplotypeAssociationTestTreeNode
extends DefaultMutableTreeNode
implements MouseListener
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 360551934793553591L;

    /**
     * Constructor
     * @param multiGroupHaplotypeAssociationTest
     *          the test in this node
     */
    public MultiGroupHaplotypeAssociationTestTreeNode(
            MultiGroupHaplotypeAssociationTest multiGroupHaplotypeAssociationTest)
    {
        super(multiGroupHaplotypeAssociationTest);
    }
    
    /**
     * Getter for the association test
     * @return
     *          the test
     */
    public MultiGroupHaplotypeAssociationTest getMultiGroupHaplotypeAssociationTest()
    {
        return (MultiGroupHaplotypeAssociationTest)this.getUserObject();
    }
    
    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
            this.contextMenuTriggered(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent e)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
            this.contextMenuTriggered(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
            this.contextMenuTriggered(e);
        }
    }
    
    /**
     * Respond to a popup trigger event.
     * @param e
     *          the event we're responding to
     */
    private void contextMenuTriggered(MouseEvent e)
    {
        JPopupMenu popupMenu = new JPopupMenu(
                this.toString());
        popupMenu.add(new PlotSpecificMultiGroupHaplotypeAssociationTestAction(
                this.getMultiGroupHaplotypeAssociationTest()));
        
        popupMenu.show(
                (Component)e.getSource(),
                e.getX(),
                e.getY());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        String name = this.getMultiGroupHaplotypeAssociationTest().getName();
        if(name == null)
        {
            return "anonymous";
        }
        else
        {
            return name;
        }
    }
}
