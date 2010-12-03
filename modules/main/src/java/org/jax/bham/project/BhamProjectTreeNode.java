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

import javax.swing.tree.DefaultMutableTreeNode;

import org.jax.haplotype.analysis.HaplotypeAssociationTest;
import org.jax.haplotype.analysis.HaplotypeDataSource;
import org.jax.haplotype.analysis.MultiGroupHaplotypeAssociationTest;
import org.jax.haplotype.analysis.PhenotypeDataSource;
import org.jax.haplotype.analysis.PhylogenyAssociationTest;
import org.jax.haplotype.analysis.PhylogenyDataSource;
import org.jax.haplotype.data.GenomeDataSource;
import org.jax.haplotype.data.MultiGroupHaplotypeDataSource;
import org.jax.util.gui.ListTreeNode;

/**
 * A {@link javax.swing.JTree} node for representing
 * {@link BhamProject}s
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class BhamProjectTreeNode extends DefaultMutableTreeNode
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 3517608478954616212L;
    
    private final ListTreeNode<GenomeDataSource> genomeDataSourcesTreeNode;

    private final ListTreeNode<PhenotypeDataSource> phenotypeDataSourcesTreeNode;

    private final ListTreeNode<HaplotypeDataSource> haplotypeDataSourcesTreeNode;

    private final ListTreeNode<HaplotypeAssociationTest> haplotypeAssociationTestsTreeNode;

    private final ListTreeNode<PhylogenyDataSource> phylogenyDataSourcesTreeNode;

    private final ListTreeNode<PhylogenyAssociationTest> phylogenyAssociationTestsTreeNode;
    
    private final ListTreeNode<MultiGroupHaplotypeDataSource> multiGroupHaplotypeDataSourcesTreeNode;
    
    private final ListTreeNode<MultiGroupHaplotypeAssociationTest> multiGroupHaplotypeAssociationTestsTreeNode;
    
    /**
     * Constructor
     * @param bhamProject
     *          the project
     */
    public BhamProjectTreeNode(BhamProject bhamProject)
    {
        super(bhamProject);
        
        this.setAllowsChildren(true);
        
        // create the direct children for this project
        this.genomeDataSourcesTreeNode = new ListTreeNode<GenomeDataSource>(
                "Genome Data Sources",
                bhamProject.getGenomeDataSources());
        this.phenotypeDataSourcesTreeNode = new ListTreeNode<PhenotypeDataSource>(
                "Phenotype Data Sources",
                bhamProject.getPhenotypeDataSources());
        this.haplotypeDataSourcesTreeNode = new ListTreeNode<HaplotypeDataSource>(
                "Haplotype Data Sources",
                bhamProject.getHaplotypeDataSources());
        this.haplotypeAssociationTestsTreeNode = new ListTreeNode<HaplotypeAssociationTest>(
                "Haplotype Association Tests",
                bhamProject.getHaplotypeAssociationTests());
        this.phylogenyDataSourcesTreeNode = new ListTreeNode<PhylogenyDataSource>(
                "Phylogeny Data Sources",
                bhamProject.getPhylogenyDataSources());
        this.phylogenyAssociationTestsTreeNode = new ListTreeNode<PhylogenyAssociationTest>(
                "Phylogeny Association Tests",
                bhamProject.getPhylogenyAssociationTests());
        this.multiGroupHaplotypeDataSourcesTreeNode = new ListTreeNode<MultiGroupHaplotypeDataSource>(
                "Multi-Group Haplotype Data Sources",
                bhamProject.getMultiGroupHaplotypeDataSources());
        this.multiGroupHaplotypeAssociationTestsTreeNode = new ListTreeNode<MultiGroupHaplotypeAssociationTest>(
                "Multi-Group Haplotype Association Tests",
                bhamProject.getMultiGroupHaplotypeAssociationTests());
    }
    
    /**
     * Getter for the BHAM project which is just a typecast version of
     * {@link #getUserObject()}
     * @return the BHAM project
     */
    public BhamProject getBhamProject()
    {
        return (BhamProject)this.getUserObject();
    }
    
    /**
     * Getter for the genome data sources tree node
     * @return the genome data sources tree node
     */
    public ListTreeNode<GenomeDataSource> getGenomeDataSourcesTreeNode()
    {
        return this.genomeDataSourcesTreeNode;
    }
    
    /**
     * Getter for the phenotypes node
     * @return the phenotypes node
     */
    public ListTreeNode<PhenotypeDataSource> getPhenotypeDataSourcesTreeNode()
    {
        return this.phenotypeDataSourcesTreeNode;
    }
    
    /**
     * Get the haplotype data sources
     * @return the haplotype data sources
     */
    public ListTreeNode<HaplotypeDataSource> getHaplotypeDataSourcesTreeNode()
    {
        return this.haplotypeDataSourcesTreeNode;
    }
    
    /**
     * Get the haplotype association tests node
     * @return the haplotypeAssociationTestsTreeNode
     */
    public ListTreeNode<HaplotypeAssociationTest> getHaplotypeAssociationTestsTreeNode()
    {
        return this.haplotypeAssociationTestsTreeNode;
    }
    
    /**
     * Get the phylogeny data sources node
     * @return the phylogenyDataSourcesTreeNode
     */
    public ListTreeNode<PhylogenyDataSource> getPhylogenyDataSourcesTreeNode()
    {
        return this.phylogenyDataSourcesTreeNode;
    }
    
    /**
     * get the phylogeny association tests node
     * @return the phylogenyAssociationTests
     */
    public ListTreeNode<PhylogenyAssociationTest> getPhylogenyAssociationTestsTreeNode()
    {
        return this.phylogenyAssociationTestsTreeNode;
    }
    
    /**
     * Getter for the multi-group haplotype data sources
     * @return the multiGroupHaplotypeDataSourcesTreeNode
     */
    public ListTreeNode<MultiGroupHaplotypeDataSource> getMultiGroupHaplotypeDataSourcesTreeNode()
    {
        return this.multiGroupHaplotypeDataSourcesTreeNode;
    }
    
    /**
     * Getter for the sliding window tests node
     * @return the tree node for sliding window tests
     */
    public ListTreeNode<MultiGroupHaplotypeAssociationTest> getMultiGroupHaplotypeAssociationTestsTreeNode()
    {
        return this.multiGroupHaplotypeAssociationTestsTreeNode;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        String name = this.getBhamProject().getName();
        if(name == null)
        {
            return "New Project";
        }
        else
        {
            return name;
        }
    }
}
