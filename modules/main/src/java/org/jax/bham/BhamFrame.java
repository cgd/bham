
package org.jax.bham;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.jax.bham.project.BhamProjectManager;
import org.jax.bham.project.BhamProjectTree;
import org.jax.util.ConfigurationUtilities;
import org.jax.util.concurrent.MultiTaskProgressPanel;
import org.jax.util.gui.desktoporganization.Desktop;


/**
 * The main application frame for BHAM!
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class BhamFrame extends javax.swing.JFrame
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 7782664260375551442L;
    
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            BhamFrame.class.getName());
    
    private final BhamMainMenuManager bhamMainMenuManager;
    
    private final MultiTaskProgressPanel multiTaskProgress;

    private final BhamProjectTree bhamProjectTree;
    
    private final Desktop desktop;

    /**
     * Constructor
     */
    public BhamFrame()
    {
        super(BhamFrame.getTitleString());
        
        this.desktop = new Desktop();
        this.bhamMainMenuManager = new BhamMainMenuManager(
                this.desktop);
        this.bhamProjectTree = new BhamProjectTree();
        this.bhamProjectTree.setProjectManager(
                BhamProjectManager.getInstance());
        this.multiTaskProgress = new MultiTaskProgressPanel();
        
        this.initComponents();
        this.postGuiInit();
    }
    
    /**
     * Get the title string that we should use for BHAM!
     * @return
     *          the title string
     */
    private static String getTitleString()
    {
        try
        {
            ConfigurationUtilities configUtil = new ConfigurationUtilities();
            
            return
                configUtil.getApplicationName() + " - " +
                configUtil.getApplicationVersion();
        }
        catch(IOException ex)
        {
            LOG.log(Level.SEVERE,
                    "Failed to load configuration information",
                    ex);
            return "BHAM!";
        }
    }
    
    /**
     * Do initialization after GUI builder finishes initialization
     */
    private void postGuiInit()
    {
        this.setJMenuBar(this.getBhamMainMenuManager().getMenuBar());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    /**
     * Getter for the desktop
     * @return the desktop
     */
    public Desktop getDesktop()
    {
        return this.desktop;
    }

    /**
     * Get the menu manager
     * @return the bhamMainMenuManager
     */
    public BhamMainMenuManager getBhamMainMenuManager()
    {
        return this.bhamMainMenuManager;
    }
    
    /**
     * Getter for the {@link MultiTaskProgressPanel}
     * @return the multiTaskProgress
     */
    public MultiTaskProgressPanel getMultiTaskProgress()
    {
        return this.multiTaskProgress;
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
        java.awt.GridBagConstraints gridBagConstraints;

        mainSplitPane = new javax.swing.JSplitPane();
        projectTreeScrollPane = new javax.swing.JScrollPane();
        javax.swing.JTree projectTree = this.bhamProjectTree;
        javax.swing.JDesktopPane desktopPane = this.desktop;
        javax.swing.JPanel progressPanel = this.multiTaskProgress;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        projectTreeScrollPane.setViewportView(projectTree);

        mainSplitPane.setLeftComponent(projectTreeScrollPane);
        mainSplitPane.setRightComponent(desktopPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(mainSplitPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(progressPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JScrollPane projectTreeScrollPane;
    // End of variables declaration//GEN-END:variables

}