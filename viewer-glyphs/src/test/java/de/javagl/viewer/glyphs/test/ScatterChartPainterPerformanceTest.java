/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.test;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.glyphs.ScatterChartPainter;
import de.javagl.viewer.glyphs.ScatterCharts;
import de.javagl.viewer.glyphs.TickShapes;
import de.javagl.viewer.painters.CoordinateSystemPainter;

/**
 * Simple integration test of the {@link ScatterChartPainter}
 */
public class ScatterChartPainterPerformanceTest
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

        
        List<Point2D> points = new ArrayList<Point2D>();
        int n = 64 * 15000;
        Random random = new Random(0);
        for (int i = 0; i < n; i++)
        {
            double x = -1.0 + random.nextDouble() * 2.0;
            double y = -1.0 + random.nextDouble() * 2.0;
            points.add(new Point2D.Double(x, y));
        }

        ScatterChart scatterChart = ScatterCharts.create(
            points, Color.BLUE, TickShapes.square(2));

        Viewer viewer = new Viewer();
        viewer.setFlippedVertically(true);

        CoordinateSystemPainter coordinateSystemPainter = 
            new CoordinateSystemPainter();
        coordinateSystemPainter.setGridColorX(null);
        coordinateSystemPainter.setGridColorY(null);
        coordinateSystemPainter.setAxisRangeX(-2.5, 2.5);
        coordinateSystemPainter.setAxisRangeY(-2.5, 2.5);
        coordinateSystemPainter.setAxisLocationX(-2.5);
        coordinateSystemPainter.setAxisLocationY(-2.5);
        viewer.addPainter(coordinateSystemPainter);

        ScatterChartPainter scatterChartPainter = new ScatterChartPainter();
        //scatterChartPainter.setLinePaint(Color.BLACK);
        scatterChartPainter.setLineStroke(new BasicStroke(1.0f));
        
        viewer.addPainter(Painters.create(scatterChartPainter, scatterChart));
        viewer.setDisplayedWorldArea(-3, -3, 6, 6);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
