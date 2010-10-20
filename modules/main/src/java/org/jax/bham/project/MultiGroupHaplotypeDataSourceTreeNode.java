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

import org.jax.haplotype.data.MultiGroupHaplotypeDataSource;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MultiGroupHaplotypeDataSourceTreeNode extends DefaultMutableTreeNode
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 2587645269817947542L;

    /**
     * Constructor
     * @param dataSource
     *          the data source that this tree node represents
     */
    public MultiGroupHaplotypeDataSourceTreeNode(
            MultiGroupHaplotypeDataSource dataSource)
    {
        super(dataSource);
    }
    
    /**
     * Getter for the data source that this node is holding
     * @return
     *          the data source
     */
    public MultiGroupHaplotypeDataSource getDataSource()
    {
        return (MultiGroupHaplotypeDataSource)this.getUserObject();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        String name = this.getDataSource().getName();
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
