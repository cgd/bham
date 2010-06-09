/*
 * Copyright (c) 2008 The Jackson Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining  a copy
 * of this software and associated documentation files (the  "Software"), to
 * deal in the Software without restriction, including  without limitation the
 * rights to use, copy, modify, merge, publish,  distribute, sublicense, and/or
 * sell copies of the Software, and to  permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be  included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,  EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE  SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
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
