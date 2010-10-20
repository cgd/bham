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

package org.jax.bham;

import java.awt.EventQueue;

/**
 * The main application class for BHAM
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class BhamApplication
{
    private static final BhamApplication instance = new BhamApplication();
    
    private final BhamFrame bhamFrame;
    
    /**
     * Constructor
     */
    private BhamApplication()
    {
        this.bhamFrame = new BhamFrame();
    }
    
    /**
     * Getter for the singleton instance
     * @return the instance
     */
    public static BhamApplication getInstance()
    {
        return BhamApplication.instance;
    }
    
    /**
     * Getter for the main application frame
     * @return the bhamFrame
     */
    public BhamFrame getBhamFrame()
    {
        return this.bhamFrame;
    }
    
    /**
     * Start the application
     */
    private void start()
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                BhamApplication.this.bhamFrame.setVisible(true);
            }
        });
    }
    
    /**
     * The main entry point for BHAM
     * @param args
     *          don't care about these
     */
    public static void main(String args[])
    {
        BhamApplication.getInstance().start();
    }
}
