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
import java.util.Collection;
import java.util.Map;

import javax.swing.AbstractAction;

import org.jax.bham.BhamApplication;
import org.jax.haplotype.analysis.experimentdesign.PhenotypeDataSource;
import org.jax.util.gui.desktoporganization.Desktop;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ShowPhenotypeEffectPlotAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 518046828168502354L;

    private final PhenotypeDataSource phenotypeDataSource;
    
    private final Map<String, ? extends Collection<String>> strainGroups;
    
    /**
     * Constructor
     * @param phenotypeDataSource
     *          the phenotype data source
     * @param strainGroups
     *          the strain groups
     */
    public ShowPhenotypeEffectPlotAction(
            PhenotypeDataSource phenotypeDataSource,
            Map<String, ? extends Collection<String>> strainGroups)
    {
        super("Show Phenotype Effect Plot");
        this.phenotypeDataSource = phenotypeDataSource;
        this.strainGroups = strainGroups;
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        PhenotypeEffectPlotPanel phenotypeEffectPlotPanel =
            new PhenotypeEffectPlotPanel(
                    this.phenotypeDataSource,
                    this.strainGroups);
        
        Desktop desktop =
            BhamApplication.getInstance().getBhamFrame().getDesktop();
        desktop.createInternalFrame(
                phenotypeEffectPlotPanel,
                "Phenotype Effect Plot",
                null,
                "phenoplot");
    }
}
