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

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;

import org.jax.bham.project.BhamProject;
import org.jax.haplotype.analysis.PhenotypeDataSource;
import org.jax.haplotype.analysis.PhylogenyAssociationTest;
import org.jax.haplotype.analysis.PhylogenyDataSource;
import org.jax.util.datastructure.SequenceUtilities;
import org.jax.util.gui.MessageDialogUtilities;

/**
 * The dialog for constructing phylogeny association tests
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PhylogenyAssociationTestDialog extends JDialog
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 692553029667089497L;
    
    private final BhamProject project;
    
    /**
     * Constructor
     * @param parent
     *          the parent frame to use for this dialog
     * @param project
     *          the project that we're going to create a test for
     */
    public PhylogenyAssociationTestDialog(Frame parent, BhamProject project)
    {
        super(parent, "Phylogeny Association Test", false);
        
        this.project = project;
        
        this.initComponents();
        this.postGuiInit();
    }

    /**
     * handle the initialization not done by the GUI builder
     */
    private void postGuiInit()
    {
        for(PhylogenyDataSource phyloDataSrc: this.project.getPhylogenyDataSources())
        {
            this.phylogenyDataSourceComboBox.addItem(phyloDataSrc);
        }
        
        for(PhenotypeDataSource phenoDataSrc: this.project.getPhenotypeDataSources())
        {
            this.phenotypeDataSourceComboBox.addItem(phenoDataSrc);
        }
        
        this.okButton.addActionListener(new ActionListener()
        {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e)
            {
                PhylogenyAssociationTestDialog.this.ok();
            }
        });
        
        this.cancelButton.addActionListener(new ActionListener()
        {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e)
            {
                PhylogenyAssociationTestDialog.this.cancel();
            }
        });
        
        this.helpButton.addActionListener(new ActionListener()
        {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e)
            {
                PhylogenyAssociationTestDialog.this.showHelp();
            }
        });
    }
    
    private void showHelp()
    {
        MessageDialogUtilities.inform(
                this,
                "Sorry, no help yet..",
                "Help Not Implemented");
    }
    
    private void cancel()
    {
        this.dispose();
    }
    
    /**
     * Getter for the selected phylogeny data source
     * @return
     *          the selected phylogeny data source
     */
    private PhylogenyDataSource getSelectedPhylogenyDataSource()
    {
        return (PhylogenyDataSource)this.phylogenyDataSourceComboBox.getSelectedItem();
    }
    
    /**
     * Getter for the selected phenotype data source
     * @return
     *          the selected phenotype data source
     */
    private PhenotypeDataSource getSelectedPhenotypeDataSource()
    {
        return (PhenotypeDataSource)this.phenotypeDataSourceComboBox.getSelectedItem();
    }
    
    private String getSelectedName()
    {
        return this.nameTextField.getText().trim();
    }
    
    private void ok()
    {
        if(this.validateData())
        {
            PhylogenyAssociationTest phylogenyAssociationTest =
                new PhylogenyAssociationTest(
                        this.getSelectedName(),
                        this.getSelectedPhylogenyDataSource(),
                        this.getSelectedPhenotypeDataSource());
            this.project.addPhylogenyAssociationTest(phylogenyAssociationTest);
            
            this.dispose();
        }
    }
    
    private boolean validateData()
    {
        String errorMessage = null;
        if(this.getSelectedName().length() == 0)
        {
            errorMessage =
                "Please enter a name for the test before continuing";
        }
        else if(this.getSelectedPhylogenyDataSource() == null)
        {
            errorMessage =
                "You cannot perform a phylogeny association test without " +
                "any phylogeny data sources.";
        }
        else if(this.getSelectedPhenotypeDataSource() == null)
        {
            errorMessage =
                "You cannot perform a phylogeny association test without " +
                "any phenotype data sources.";
        }
        
        if(errorMessage == null)
        {
            Set<String> phyloStrains = this.getSelectedPhylogenyDataSource().getAvailableStrains();
            Set<String> phenoStrains = this.getSelectedPhenotypeDataSource().getPhenotypeData().keySet();
            
            if(phyloStrains.equals(phenoStrains))
            {
                return true;
            }
            else
            {
                // make sure that the user is aware that the strain sets do
                // not completely overlap
                Set<String> phyloOnlyStrains = new HashSet<String>(phyloStrains);
                phyloOnlyStrains.removeAll(phenoStrains);
                Set<String> phenoOnlyStrains = new HashSet<String>(phenoStrains);
                phenoOnlyStrains.removeAll(phyloStrains);
                Set<String> commonStrains = new HashSet<String>(phyloStrains);
                commonStrains.retainAll(phenoStrains);
                
                boolean performTest = MessageDialogUtilities.ask(
                        this,
                        "[" + SequenceUtilities.toString(phyloOnlyStrains, ", ") +
                        "] are unique to the phylogeny data and [" +
                        SequenceUtilities.toString(phenoOnlyStrains, ", ") +
                        "] are unique to the phenotype data. Would you " +
                        "like to perform the test using only the subset of " +
                        "strains that they have in common: [" +
                        SequenceUtilities.toString(commonStrains, ", ") +
                        "]?",
                        "Strains Do Not Match");
                return performTest;
            }
        }
        else
        {
            MessageDialogUtilities.warn(
                    this,
                    errorMessage,
                    "Validation Failed");
            return false;
        }
    }
    
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("all")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        javax.swing.JLabel phylogenyDataSourceLabel = new javax.swing.JLabel();
        phylogenyDataSourceComboBox = new javax.swing.JComboBox();
        javax.swing.JLabel phenotypeDataSourceLabel = new javax.swing.JLabel();
        phenotypeDataSourceComboBox = new javax.swing.JComboBox();
        actionPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        nameLabel.setText("Test Name:");

        phylogenyDataSourceLabel.setText("Phylogeny Data:");

        phenotypeDataSourceLabel.setText("Phenotype Data:");

        okButton.setText("OK");
        actionPanel.add(okButton);

        cancelButton.setText("Cancel");
        actionPanel.add(cancelButton);

        helpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/help-16x16.png"))); // NOI18N
        helpButton.setText("Help...");
        actionPanel.add(helpButton);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 368, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, actionPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(nameLabel)
                    .add(phylogenyDataSourceLabel)
                    .add(phenotypeDataSourceLabel))
                .add(19, 19, 19)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(phenotypeDataSourceComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(180, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(nameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .add(27, 27, 27))
                    .add(layout.createSequentialGroup()
                        .add(phylogenyDataSourceComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 161, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameLabel)
                    .add(nameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(phylogenyDataSourceLabel)
                    .add(phylogenyDataSourceComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(phenotypeDataSourceLabel)
                    .add(phenotypeDataSourceComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE)
                .add(actionPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton helpButton;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox phenotypeDataSourceComboBox;
    private javax.swing.JComboBox phylogenyDataSourceComboBox;
    // End of variables declaration//GEN-END:variables

}
