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

import de.javagl.viewer.MouseControls;
import de.javagl.viewer.MultiObjectPainter;
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
public class MultiLineChartPainterTest
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
        
        List<Double> values0 = Arrays.asList(
            -0.1, 0.1, 0.2, 0.8, 0.4, -0.3, 0.0, 0.6, 1.2);
        List<Double> values1 = Arrays.asList(
            -0.5, 0.2, 0.8, 0.1, -0.2, -0.7, -0.3, 0.2, 0.6);
        List<Double> values2 = Arrays.asList(
             0.2, -0.1, 0.4, 0.5, 0.7, 0.3, 0.1, -0.2, -1.2);
        LineChart lineChart0 = LineCharts.createFromList(
            values0, Color.RED, new BasicStroke(1), TickShapes.circle(5));
        LineChart lineChart1 = LineCharts.createFromList(
            values1, Color.GREEN, new BasicStroke(1), TickShapes.square(5));
        LineChart lineChart2 = LineCharts.createFromList(
            values2, Color.BLUE, new BasicStroke(1), TickShapes.diamond(5));

        List<LineChart> lineCharts = 
            Arrays.asList(lineChart0, lineChart1, lineChart2);
        
        
        Viewer viewer = new Viewer();
        viewer.setMouseControl(MouseControls.createDefault(viewer, false, true));
        viewer.setFlippedVertically(true);
        
        CoordinateSystemPainter coordinateSystemPainter = 
            new CoordinateSystemPainter();
        coordinateSystemPainter.setAxisRangeX(0.0, 8.0);
        coordinateSystemPainter.setAxisRangeY(-2.0, 2.0);
        viewer.addPainter(coordinateSystemPainter);
        
        MultiObjectPainter<LineChart> multiLineChartPainter = 
            new MultiObjectPainter<LineChart>(new LineChartPainter());
        viewer.addPainter(Painters.create(multiLineChartPainter, lineCharts));
        viewer.setDisplayedWorldArea(-1, -3, 10, 6);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
