/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.selection.test;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.selection.LoggingSelectionListener;
import de.javagl.selection.SelectionModel;
import de.javagl.selection.SelectionModels;
import de.javagl.viewer.MouseControls;
import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.glyphs.ScatterChartPainter;
import de.javagl.viewer.glyphs.ScatterCharts;
import de.javagl.viewer.glyphs.TickShapes;
import de.javagl.viewer.glyphs.selection.ScatterChartSelector;
import de.javagl.viewer.painters.CoordinateSystemPainter;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.SelectionHandler;
import de.javagl.viewer.selection.SelectionHandlers;
import de.javagl.viewer.selection.ShapeBasedSelector;

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

        ScatterChart scatterChart = ScatterCharts.create(points, 
            Color.GREEN, Color.BLUE, 
            new BasicStroke(3.0f), TickShapes.square(8));

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

        ScatterChartSelector scatterChartSelector = new ScatterChartSelector();
        scatterChartSelector.setScatterChart(scatterChart);
        
        // The selector implements both selector interfaces.
        // Make this clear by assigning it to respective variables
        PointBasedSelector<Integer> pointBasedSelector = scatterChartSelector;
        ShapeBasedSelector<Integer> shapeBasedSelector = scatterChartSelector;

        // Create a selection handler for clicks, and use it to connect
        // the viewer and the selection model
        SelectionHandler<Integer> clickSelectionHandler = 
            SelectionHandlers.createClick(pointBasedSelector);
        clickSelectionHandler.connect(viewer, selectionModel);
        
        // Create a selection handler for a lasso selection, and use it to 
        // connect the viewer and the selection model
        SelectionHandler<Integer> lassoSelectionHandler = 
            SelectionHandlers.createLasso(shapeBasedSelector);
        lassoSelectionHandler.connect(viewer, selectionModel);

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
        viewer.addPainter(Painters.create(scatterChartPainter, scatterChart));
        viewer.setDisplayedWorldArea(-3, -3, 6, 6);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
