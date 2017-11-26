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
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.geom.AffineTransforms;
import de.javagl.viewer.Painter;
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
public class ScatterChartPainterTest
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

        List<Point2D> points = Arrays.asList(
            new Point2D.Double(-1.5, -1.5),
            new Point2D.Double(-1.0, -1.0),
            new Point2D.Double(0.0, 0.0),
            new Point2D.Double(1.0, 0.0),
            new Point2D.Double(1.0, 1.0),
            new Point2D.Double(0.0, 1.0)
        );

        ScatterChart scatterChart = ScatterCharts.create(
            points, Color.BLUE, TickShapes.square(2));

        Viewer viewer = new Viewer();
        viewer.setFlippedVertically(true);

        double minX = -2.5;
        double minY = -2.5;
        double maxX = 2.0;
        double maxY = 2.0;
        
        Painter backgroundPainter = 
            BackgroundPainters.createVerticalGradient(
                new Color(250,250,250),
                new Color(210,210,210));
        viewer.addPainter(
            Painters.createTransformed(backgroundPainter, 
                AffineTransforms.getScaleInstance(minX, minY, maxX, maxY, null)));
        
        CoordinateSystemPainter coordinateSystemPainter = 
            new CoordinateSystemPainter();
        coordinateSystemPainter.setGridColorX(null);
        coordinateSystemPainter.setGridColorY(null);
        coordinateSystemPainter.setAxisRangeX(-2.5, 2.0);
        coordinateSystemPainter.setAxisRangeY(-2.5, 2.0);
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
