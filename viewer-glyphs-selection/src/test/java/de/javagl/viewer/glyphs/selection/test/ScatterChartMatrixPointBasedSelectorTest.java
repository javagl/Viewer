/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.selection.test;

import java.awt.BorderLayout;

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
import de.javagl.viewer.glyphs.ScatterChartMatrices;
import de.javagl.viewer.glyphs.ScatterChartMatrix;
import de.javagl.viewer.glyphs.ScatterChartMatrixPainter;
import de.javagl.viewer.glyphs.selection.ScatterChartMatrixSelector;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.SelectionHandler;
import de.javagl.viewer.selection.SelectionHandlers;
import de.javagl.viewer.selection.ShapeBasedSelector;

/**
 * Simple integration test of the {@link ScatterChart} selection
 */
public class ScatterChartMatrixPointBasedSelectorTest
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
        viewer.setMouseControl(
            MouseControls.createDefault(viewer, false, true));

        // Create the selection model
        SelectionModel<Integer> selectionModel = SelectionModels.create();
        selectionModel.addSelectionListener(
            new LoggingSelectionListener<Integer>());

        ScatterChartMatrixSelector scatterChartMatrixSelector = 
            new ScatterChartMatrixSelector();
        
        // The selector implements both selector interfaces.
        // Make this clear by assigning it to respective variables
        PointBasedSelector<Integer> pointBasedSelector = 
            scatterChartMatrixSelector;
        ShapeBasedSelector<Integer> shapeBasedSelector = 
            scatterChartMatrixSelector;

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
        
        ScatterChartMatrix scatterChartMatrix = 
            ScatterChartMatrices.createTest(selectionModel::isSelected);
        
        scatterChartMatrixSelector.setScatterChartMatrix(scatterChartMatrix);

        ScatterChartMatrixPainter scatterChartMatrixPainter = 
            new ScatterChartMatrixPainter();
        viewer.addPainter(
            Painters.create(scatterChartMatrixPainter, scatterChartMatrix));
        viewer.setDisplayedWorldArea(-1, -1, 3, 3);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
    }
    
}
