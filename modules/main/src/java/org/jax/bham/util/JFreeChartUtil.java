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

package org.jax.bham.util;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

/**
 * Some utility functions for JFreeChart
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class JFreeChartUtil
{
    /**
     * Convert from a Java2D point to a graph point
     * @param java2DPoint
     *          the java 2D point to convert
     * @param chartPanel
     *          the chart panel to convert
     * @return
     *          the point
     */
    public static Point2D java2DPointToGraphPoint(
            Point2D java2DPoint,
            ChartPanel chartPanel)
    {
        JFreeChart chart = chartPanel.getChart();
        ChartRenderingInfo info =
            chartPanel.getChartRenderingInfo();
        Rectangle2D dataArea = info.getPlotInfo().getDataArea();
        XYPlot xyPlot = chart.getXYPlot();
        
        double graphX = xyPlot.getDomainAxis().java2DToValue(
                java2DPoint.getX(),
                dataArea,
                xyPlot.getDomainAxisEdge());
        double graphY = xyPlot.getRangeAxis().java2DToValue(
                java2DPoint.getY(),
                dataArea,
                xyPlot.getRangeAxisEdge());
        
        return new Point2D.Double(graphX, graphY);
    }
}
