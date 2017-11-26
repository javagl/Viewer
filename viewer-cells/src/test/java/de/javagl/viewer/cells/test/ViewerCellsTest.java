/*
 * www.javagl.de - Viewer - Cells
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.cells.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.hexagon.HexagonGrid;
import de.javagl.hexagon.HexagonGrids;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.cells.BasicCellPainter;
import de.javagl.viewer.cells.CellMap;
import de.javagl.viewer.cells.CellMapPanel;
import de.javagl.viewer.cells.CellMaps;

/**
 * Simple integration test and demonstration of the cell rendering
 * using the {@link Viewer} class
 */
public class ViewerCellsTest
{
    /**
     * The entry point of this test
     *
     * @param args Not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
   
    /**
     * Create and show the GUI, to be called on the EDT
     */
    private static void createAndShowGUI()
    {
        JFrame f = new JFrame("ViewerCells");
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

        CellMapPanel cellMapPanel = createCellMapPanel();
        f.getContentPane().add(cellMapPanel, BorderLayout.CENTER);

        JLabel infoLabel = new JLabel(" ");
        f.getContentPane().add(infoLabel, BorderLayout.SOUTH);
        cellMapPanel.addMouseMotionListener(
            new ViewerCellsTestInfoHandler(
                infoLabel, cellMapPanel));
        
        cellMapPanel.setPreferredSize(new Dimension(500,500));
        f.pack();
        cellMapPanel.setPreferredSize(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    /**
     * Create the panel that shows the cell map
     * 
     * @return The cell map panel
     */
    private static CellMapPanel createCellMapPanel()
    {
        CellMapPanel cellMapPanel = new CellMapPanel();
        HexagonGrid hexagonGrid = HexagonGrids.create(10, false, true);
        CellMap cellMap = null;
        cellMap = CellMaps.createHexagon(8, 6, hexagonGrid);
        //cellMap = CellMaps.createRectangle(10, 10, 10, 10);
        cellMapPanel.setCellMap(cellMap);
        
        // Create and configure a basic cell painter:
        BasicCellPainter basicCellPainter = new BasicCellPainter();
        
        // The borders of the cells should be blue
        basicCellPainter.setDrawPaint(Color.BLUE);
        
        // The cells should be filled white, except for a specific one
        basicCellPainter.setFillPaintFunction(
            (cell) -> cell.getX() == 3 && cell.getY() == 4 ?
                Color.GREEN : Color.WHITE);
        
        // The content area of the cells should be gray
        basicCellPainter.setContentFillPaint(Color.LIGHT_GRAY);
        
        // The labels should show the x/y coordinates of the cell,
        // except for one that should show some longer string
        basicCellPainter.setLabelFunction(
            (cell) -> cell.getX() == 5 && cell.getY() == 3 ?
                "LongLabelText" : cell.getX()+","+cell.getY());
        basicCellPainter.setLabelPaint(Color.BLACK);
        
        // All the cells should use a certain font
        Font font = new Font("Sans Serif", Font.PLAIN, 12);
        basicCellPainter.setLabelFont(font);
        
        cellMapPanel.addCellPainter(basicCellPainter, 0);
        return cellMapPanel;
    }
    
}

