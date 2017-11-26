/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.test;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.LineChart;
import de.javagl.viewer.glyphs.LineChartPainter;
import de.javagl.viewer.glyphs.LineCharts;
import de.javagl.viewer.glyphs.TickShapes;
import de.javagl.viewer.painters.CoordinateSystemPainter;

/**
 * Simple integration test of the {@link LineChartPainter}
 */
public class LineChartPainterTest
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
        
        LineChart lineChart = LineCharts.createFromList(
            values, Color.BLUE, new BasicStroke(1), TickShapes.circle(4));

        Viewer viewer = new Viewer();
        viewer.setFlippedVertically(true);

        CoordinateSystemPainter coordinateSystemPainter = 
            new CoordinateSystemPainter();
        coordinateSystemPainter.setAxisRangeX(0.0, 8.0);
        coordinateSystemPainter.setAxisRangeY(-1.0, 2.0);
        viewer.addPainter(coordinateSystemPainter);
        
        LineChartPainter lineChartPainter = new LineChartPainter();
        viewer.addPainter(Painters.create(lineChartPainter, lineChart));

        viewer.setDisplayedWorldArea(-1, 2, 10, 3);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
