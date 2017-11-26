/*
 * www.javagl.de - Viewer - Cells
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.cells.test;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Locale;

import javax.swing.JLabel;

import de.javagl.viewer.Viewer;
import de.javagl.viewer.cells.Cell;
import de.javagl.viewer.cells.CellMapPanel;

/**
 * A mouse motion listener for the viewer cells test
 */
public class ViewerCellsTestInfoHandler implements
    MouseMotionListener
{
    /**
     * The label showing screen- and world coordinates
     */
    private final JLabel infoLabel;
    
    /**
     * The {@link CellMapPanel} (that is, the {@link Viewer}) to which 
     * this listener is attached
     */
    private final CellMapPanel cellMapPanel;

    /**
     * Default constructor
     * 
     * @param infoLabel The label showing screen- and world coordinates
     * @param cellMapPanel The {@link CellMapPanel} 
     * (that is, the {@link Viewer}) to which this listener is attached
     */
    public ViewerCellsTestInfoHandler(
        JLabel infoLabel, CellMapPanel cellMapPanel)
    {
        this.infoLabel = infoLabel;
        this.cellMapPanel = cellMapPanel;
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        updateInfo(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        updateInfo(e.getPoint());
    }

    /**
     * Update the info label depending on the given screen point
     * @param screenPoint The screen point
     */
    private void updateInfo(Point screenPoint)
    {
        Cell cell = cellMapPanel.getCellAtScreen(screenPoint.x, screenPoint.y);
        AffineTransform screenToWorld = 
            cellMapPanel.getScreenToWorld();
        Point2D worldPoint = screenToWorld.transform(screenPoint, null);
        StringBuilder sb = new StringBuilder();
        sb.append("Screen: "+format(screenPoint)+" ");
        sb.append("World: "+format(worldPoint)+" ");
        if (cell != null)
        {
            sb.append("Cell: "+cell.getX()+","+cell.getY());
        }
        infoLabel.setText(sb.toString());
    }

    /**
     * Create a simple string representation of the given point
     *
     * @param p The point
     * @return The string representation
     */
    private String format(Point2D p)
    {
        String xs = String.format(Locale.ENGLISH, "%.2f", p.getX());
        String ys = String.format(Locale.ENGLISH, "%.2f", p.getY());
        return "("+xs+","+ys+")";
    }
}