/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.selection.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.selection.LoggingSelectionListener;
import de.javagl.selection.SelectionModel;
import de.javagl.selection.SelectionModels;
import de.javagl.viewer.MouseControls;
import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.BasicScatterChart;
import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.glyphs.ScatterChartPainter;
import de.javagl.viewer.glyphs.ScatterCharts;
import de.javagl.viewer.glyphs.TickShapes;
import de.javagl.viewer.glyphs.selection.ScatterChartSelectionHandlers;
import de.javagl.viewer.painters.CoordinateSystemPainter;

/**
 * Simple integration test of the {@link ScatterChart} selection
 */
public class ScatterChartPointBasedSelectorTest
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

        JLabel usageLabel = new JLabel("<html>"
            + "Right mouse drags: Translate<br> "
            + "Left mouse drags: Create selection shape<br>"
            + "&nbsp;&nbsp;&nbsp;&nbsp; +shift: remove from selection<br>"
            + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: add to selection<br>"
            + "Left mouse clicks: Select single<br>"
            + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: toggle single selection<br>"
            + "Mouse wheel: Zoom uniformly<br>"
            + "&nbsp;&nbsp;&nbsp;&nbsp; +shift: zoom along x<br>"
            + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: zoom along y<br>"
            + "</html>");
        f.getContentPane().add(usageLabel, BorderLayout.NORTH);


        // Create a viewer, with mouse controls where the rotation is
        // disabled (left clicks are intended for the selection here)
        Viewer viewer = new Viewer();
        viewer.setFlippedVertically(true);
        viewer.setMouseControl(
            MouseControls.createDefault(viewer, false, true));

        // Create the selection model
        SelectionModel<Integer> selectionModel = SelectionModels.create();
        selectionModel.addSelectionListener(
            new LoggingSelectionListener<Integer>());

        
        // Create the scatter chart
        int numPoints = 40;
        Random random = new Random(0);
        List<Point2D> points = new ArrayList<Point2D>();
        for (int i = 0; i < numPoints; i++)
        {
            double x = random.nextDouble();
            double y = random.nextDouble();
            Point2D p = new Point2D.Double(x, y);
            points.add(p);
        }
        BasicScatterChart scatterChart = ScatterCharts.create(points, 
            Color.GREEN, Color.BLUE, null, TickShapes.square(12));
        
        // Establish the connection between the viewer, the scatter chart
        // and the selection model. 
        // See the implementation of the "createDefault" method for details 
        ScatterChartSelectionHandlers.createDefault(
            viewer, scatterChart, selectionModel);

        CoordinateSystemPainter coordinateSystemPainter = 
            new CoordinateSystemPainter();
        coordinateSystemPainter.setGridColorX(null);
        coordinateSystemPainter.setGridColorY(null);
        coordinateSystemPainter.setAxisRangeX(0.0, 1.0);
        coordinateSystemPainter.setAxisRangeY(0.0, 1.0);
        viewer.addPainter(coordinateSystemPainter);
        
        ScatterChartPainter scatterChartPainter = new ScatterChartPainter();
        viewer.addPainter(Painters.create(scatterChartPainter, scatterChart));
        viewer.setDisplayedWorldArea(-1, -1, 3, 3);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
