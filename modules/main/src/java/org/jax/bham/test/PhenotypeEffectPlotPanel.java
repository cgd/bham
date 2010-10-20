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

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jax.haplotype.analysis.experimentdesign.PhenotypeDataSource;
import org.jax.util.datastructure.SequenceUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PhenotypeEffectPlotPanel extends JPanel
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 3819240536305070987L;
    
    private final ChartPanel chartPanel;
    
    private final JCheckBox showIndividualStrainEffectsCheckBox;

    private final PhenotypeDataSource phenotypeDataSource;

    private final Map<String, List<Double>> phenotypeData;
    
    private final Map<String, ? extends Collection<String>> strainGroups;

    /**
     * Constructor
     * @param phenotypeDataSource
     *          the phenotype data source
     * @param strainGroups
     *          the strain groupings
     */
    public PhenotypeEffectPlotPanel(
            PhenotypeDataSource phenotypeDataSource,
            Map<String, ? extends Collection<String>> strainGroups)
    {
        super(new BorderLayout());
        
        this.chartPanel = new ChartPanel(null, true);
        this.showIndividualStrainEffectsCheckBox = new JCheckBox(
                "Show Individual Strain Effects",
                false);
        this.phenotypeDataSource = phenotypeDataSource;
        this.phenotypeData = phenotypeDataSource.getPhenotypeData();
        this.strainGroups = strainGroups;
        
        this.updateChart();
        
        JToolBar toolBar = new JToolBar();
        toolBar.add(this.showIndividualStrainEffectsCheckBox);
        this.showIndividualStrainEffectsCheckBox.addItemListener(new ItemListener()
        {
            /**
             * {@inheritDoc}
             */
            public void itemStateChanged(ItemEvent e)
            {
                PhenotypeEffectPlotPanel.this.updateChart();
            }
        });
        
        this.add(toolBar, BorderLayout.PAGE_START);
        this.add(this.chartPanel, BorderLayout.CENTER);
    }
    
    private void updateChart()
    {
        BoxAndWhiskerCategoryDataset dataset = this.createDataset();
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                "Phenotype Effect Plot",
                "Groups",
                this.phenotypeDataSource.getName(),
                dataset,
                true);
        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        BoxAndWhiskerRenderer renderer =
            (BoxAndWhiskerRenderer)plot.getRenderer();
        renderer.setMaximumBarWidth(0.05);
        
        this.chartPanel.setChart(chart);
    }

    private BoxAndWhiskerCategoryDataset createDataset()
    {
        DefaultBoxAndWhiskerCategoryDataset dataset =
            new DefaultBoxAndWhiskerCategoryDataset();
        
        if(this.showIndividualStrainEffectsCheckBox.isSelected())
        {
            for(Entry<String, ? extends Collection<String>> strainGroupEntry: this.strainGroups.entrySet())
            {
                for(String strain: strainGroupEntry.getValue())
                {
                    dataset.add(
                            this.phenotypeData.get(strain),
                            strain,
                            strainGroupEntry.getKey());
                }
            }
        }
        else
        {
            Map<String, Double> meanPhenoData = new HashMap<String, Double>(
                    this.phenotypeData.size());
            for(Entry<String, List<Double>> phenoEntry: this.phenotypeData.entrySet())
            {
                if(!phenoEntry.getValue().isEmpty())
                {
                    double sum = 0.0;
                    for(double value: phenoEntry.getValue())
                    {
                        sum += value;
                    }
                    double mean = sum / phenoEntry.getValue().size();
                    
                    meanPhenoData.put(phenoEntry.getKey(), mean);
                }
            }
            
            for(Entry<String, ? extends Collection<String>> strainGroupEntry: this.strainGroups.entrySet())
            {
                List<Double> valueList = new ArrayList<Double>(
                        strainGroupEntry.getValue().size());
                for(String strain: strainGroupEntry.getValue())
                {
                    valueList.add(meanPhenoData.get(strain));
                }
                dataset.add(
                        valueList,
                        SequenceUtilities.toString(strainGroupEntry.getValue(), ", "),
                        strainGroupEntry.getKey());
            }
        }
        
        return dataset;
    }
}
