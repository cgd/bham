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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import org.jax.bham.BhamApplication;
import org.jax.haplotype.analysis.experimentdesign.PhylogenyAssociationTest;
import org.jax.haplotype.analysis.visualization.PhylogenyTreeImageFactory;
import org.jax.haplotype.analysis.visualization.PhylogenyTreeImagePanel;
import org.jax.haplotype.analysis.visualization.SimplePhylogenyTreeImageFactory;
import org.jax.haplotype.analysis.visualization.SmoothPaintScale;
import org.jax.haplotype.phylogeny.data.PhylogenyTreeEdgeWithRealValue;
import org.jax.haplotype.phylogeny.data.PhylogenyTreeNode;
import org.jax.util.gui.desktoporganization.Desktop;
import org.jax.util.math.NegativeLog10;
import org.jfree.chart.renderer.PaintScale;

/**
 * A simple action for showing a phylogeny tree image
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PlotPhylogeneticTreeAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -1788988428667216529L;
    
    private final PhylogenyTreeNode phylogenyTree;

    private final PhylogenyAssociationTest test;
    
    /**
     * Constructor
     * @param phylogenyTree
     *          the tree that this action will plot
     * @param test
     *          the test 
     */
    public PlotPhylogeneticTreeAction(
            PhylogenyTreeNode phylogenyTree,
            PhylogenyAssociationTest test)
    {
        super("Plot Phylogenetic Tree");
        
        this.phylogenyTree = phylogenyTree;
        this.test = test;
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        final PhylogenyTreeNode phylogenyTree;
        final PhylogenyTreeImageFactory treeImageFactory;
        if(this.test == null)
        {
            phylogenyTree = this.phylogenyTree;
            treeImageFactory = new SimplePhylogenyTreeImageFactory();
        }
        else
        {
            Map<String, List<Double>> phenoData =
                this.test.getPhenotypeDataSource().getPhenotypeData();
            phenoData.keySet().retainAll(this.test.getCommonStrains());
            
            PhylogenyTreeNode preTransformPhylogenyTree =
                this.test.getPhylogenyTester().testMultipleResponseSignificance(
                        this.phylogenyTree,
                        phenoData);
            
            // do a -log10 transform
            phylogenyTree = PhylogenyTreeEdgeWithRealValue.transform(
                    preTransformPhylogenyTree,
                    new NegativeLog10());
            PhylogenyTreeEdgeWithRealValue maxEdge =
                PhylogenyTreeEdgeWithRealValue.getEdgeWithMaximumValue(
                        phylogenyTree);
            
            PaintScale paintScale = new SmoothPaintScale(
                    0.0,
                    maxEdge.getRealValue(),
                    Color.BLUE,
                    Color.RED);
            treeImageFactory = new SimplePhylogenyTreeImageFactory(
                    paintScale);
        }
        
        PhylogenyTreeImagePanel phyloPanel = new PhylogenyTreeImagePanel(
                treeImageFactory,
                phylogenyTree);
        
        Desktop desktop =
            BhamApplication.getInstance().getBhamFrame().getDesktop();
        desktop.createInternalFrame(
                phyloPanel,
                "Phylogeny Tree Graph",
                null,
                "phylo tree graph: " + this.phylogenyTree.toNewickFormat());
    }
}
