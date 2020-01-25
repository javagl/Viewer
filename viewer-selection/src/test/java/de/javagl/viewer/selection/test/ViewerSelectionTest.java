/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2020 Marco Hutter - http://www.javagl.de
 */

package de.javagl.viewer.selection.test;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.selection.LoggingSelectionListener;
import de.javagl.selection.SelectionModel;
import de.javagl.selection.SelectionModels;
import de.javagl.viewer.MouseControls;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.SelectionHandler;
import de.javagl.viewer.selection.SelectionHandlers;
import de.javagl.viewer.selection.ShapeBasedSelector;

/**
 * Simple integration test and demonstration of the selection
 * functionality for the {@link Viewer} class
 */
public class ViewerSelectionTest
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
                + "Left mouse drags: Create selection shape<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +shift: remove from selection<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: add to selection<br>"
                + "Left mouse clicks: Select single<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: toggle single selection<br>"
                + "Mouse wheel: Zoom uniformly<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +shift: zoom along x<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: zoom along y<br>"
                + "</html>"),
            BorderLayout.NORTH);
       
        // Create a viewer, with mouse controls where the rotation is
        // disabled (left clicks are intended for the selection here)
        Viewer viewer = new Viewer();
        viewer.setMouseControl(
            MouseControls.createDefault(viewer, false, true));
        
        // Create the selection model
        SelectionModel<Point2D> selectionModel = SelectionModels.create();
        selectionModel.addSelectionListener(
            new LoggingSelectionListener<Point2D>());

        // Create the painter for the test
        ViewerSelectionTestPainter viewerSelectionTestPainter = 
            new ViewerSelectionTestPainter(selectionModel::isSelected);
        
        // The painter also serves as the point- and shape based selector.
        // Make this clear by assigning it to respective variables
        PointBasedSelector<Point2D> pointBasedSelector = 
            viewerSelectionTestPainter;
        ShapeBasedSelector<Point2D> shapeBasedSelector = 
            viewerSelectionTestPainter;

        // Create a selection handler for clicks, and use it to connect
        // the viewer and the selection model
        SelectionHandler<Point2D> clickSelectionHandler = 
            SelectionHandlers.createClick(pointBasedSelector);
        clickSelectionHandler.connect(viewer, selectionModel);
        
        // Create a selection handler for a lasso selection, and use it to 
        // connect the viewer and the selection model
        SelectionHandler<Point2D> lassoSelectionHandler = 
            SelectionHandlers.createLasso(shapeBasedSelector);
        lassoSelectionHandler.connect(viewer, selectionModel);

        // Create a selection handler for rectangles, which can be enabled
        // via the config panel
        SelectionHandler<Point2D> rectangleSelectionHandler = 
            SelectionHandlers.createRectangle(shapeBasedSelector);
        
        viewer.addPainter(
            viewerSelectionTestPainter);
        f.getContentPane().add(viewer, BorderLayout.CENTER);

        JPanel configPanel = createConfigPanel(viewer, selectionModel, 
            lassoSelectionHandler, rectangleSelectionHandler);
        f.getContentPane().add(configPanel, BorderLayout.EAST);

        JLabel infoLabel = new JLabel(" ");
        f.getContentPane().add(infoLabel, BorderLayout.SOUTH);
       
        viewer.setPreferredSize(new Dimension(500,500));
        f.pack();
        viewer.setPreferredSize(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        viewer.setDisplayedWorldArea(-0.1, -0.1, 1.2, 1.2);
    }
    
    /**
     * Create the config panel for the viewer
     * 
     * @param viewer The viewer
     * @param selectionModel The selection model
     * @param lassoSelectionHandler The lasso selection handler
     * @param rectangleSelectionHandler The rectangle selection handler
     * @return The config panel
     */
    static JPanel createConfigPanel(Viewer viewer,
        SelectionModel<Point2D> selectionModel,
        SelectionHandler<Point2D> lassoSelectionHandler,
        SelectionHandler<Point2D> rectangleSelectionHandler)
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new GridLayout(0,1));
        
        JCheckBox lassoCheckBox = 
            new JCheckBox("Lasso", true);
        lassoCheckBox.addActionListener(e -> 
        {
            lassoSelectionHandler.disconnect();
            rectangleSelectionHandler.disconnect();
            if (lassoCheckBox.isSelected())
            {
                lassoSelectionHandler.connect(viewer, selectionModel);
            }
            else
            {
                rectangleSelectionHandler.connect(viewer, selectionModel);
            }
        });
        controlPanel.add(lassoCheckBox);
        
        JButton setDisplayedWorldAreaButton = 
            new JButton("Reset displayed world area");
        setDisplayedWorldAreaButton.addActionListener(
            e -> viewer.setDisplayedWorldArea(
                new Rectangle2D.Double(-0.1, -0.1, 1.2, 1.2)));
        controlPanel.add(setDisplayedWorldAreaButton);
        
        JButton resetTransformButton = 
            new JButton("Reset transform");
        resetTransformButton.addActionListener(
            e -> viewer.resetTransform());
        controlPanel.add(resetTransformButton);
        
        configPanel.add(controlPanel, BorderLayout.NORTH);
        
        return configPanel;
    }
    
}
