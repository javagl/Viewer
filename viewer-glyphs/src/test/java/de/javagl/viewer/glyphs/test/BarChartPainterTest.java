/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.BarChart;
import de.javagl.viewer.glyphs.BarChartPainter;
import de.javagl.viewer.glyphs.BarCharts;
import de.javagl.viewer.painters.CoordinateSystemPainter;

/**
 * Simple integration test of the {@link BarChartPainter}
 */
public class BarChartPainterTest
{
    /**
     * The entry point of this test
     *
     * @param args Not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
   
    /**
     * Create and show the GUI, to be called on the EDT
     */
    private static void createAndShowGUI()
    {
        JFrame f = new JFrame("Viewer");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(
            new JLabel("<html>"
                + "Right mouse drags: Translate<br> "
                + "Left mouse drags: Rotate<br>"
                + "Mouse wheel: Zoom uniformly<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +shift: zoom along x<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: zoom along y<br>"
                + "</html>"),
            BorderLayout.NORTH);
        
        List<Double> values = Arrays.asList(
            -0.1, 0.1, 0.2, 0.8, 0.4, -0.3, 0.0, 0.6, 1.2);
        BarChart barChart = 
            BarCharts.createFromList(values, new Color(255, 160, 160));

        
        Viewer viewer = new Viewer();
        viewer.setFlippedVertically(true);
        
        CoordinateSystemPainter coordinateSystemPainter = 
            new CoordinateSystemPainter();
        coordinateSystemPainter.setAxisRangeX(0.0, 9.0);
        coordinateSystemPainter.setAxisRangeY(-1.0, 2.0);
        coordinateSystemPainter.setAxisColorX(null);
        coordinateSystemPainter.setGridColorX(null);
        viewer.addPainter(coordinateSystemPainter);
        
        BarChartPainter barChartPainter = new BarChartPainter();
        viewer.addPainter(Painters.create(barChartPainter, barChart));
        viewer.setDisplayedWorldArea(-1, 2, 11, 3);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        startAnimation(values, viewer);
    }
    
    @SuppressWarnings("javadoc")
    private static void startAnimation(List<Double> values, Viewer viewer)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    long ms = System.currentTimeMillis();
                    double alpha = (ms % 3000) / 3000.0;
                    values.set(0, Math.cos(alpha * Math.PI * 2));
                    viewer.repaint();
                    try
                    {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }); 
        thread.setDaemon(true);
        thread.start();
    }
    
}
