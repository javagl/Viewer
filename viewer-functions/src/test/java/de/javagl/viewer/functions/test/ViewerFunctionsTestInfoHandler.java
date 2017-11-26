/*
 * www.javagl.de - Viewer - Functions
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.functions.test;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JLabel;

import de.javagl.geom.Points;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.functions.FunctionPanel;

/**
 * A mouse motion listener for the viewer cells test
 */
public class ViewerFunctionsTestInfoHandler implements
    MouseMotionListener
{
    /**
     * The label showing screen- and world coordinates
     */
    private final JLabel infoLabel;

    /**
     * The {@link FunctionPanel} (that is, the {@link Viewer}) to which 
     * this listener is attached
     */
    private final FunctionPanel functionPanel;

    /**
     * Default constructor
     * 
     * @param infoLabel The label showing screen- and world coordinates
     * @param functionPanel The {@link FunctionPanel} 
     * (that is, the {@link Viewer}) to which this listener is attached
     */
    ViewerFunctionsTestInfoHandler(FunctionPanel functionPanel,
        JLabel infoLabel)
    {
        this.functionPanel = functionPanel;
        this.infoLabel = infoLabel;
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
        Point2D worldPoint = Points.inverseTransform(
            functionPanel.getWorldToScreen(), screenPoint, null);
        infoLabel.setText(
            "Screen: "+format(screenPoint)+
            " World: "+format(worldPoint));
    }

    /**
     * Create a simple string representation of the given point
     *
     * @param p The point
     * @return The string representation
     */
    private String format(Point2D p)
    {
        String xs = String.format("%.2f", p.getX());
        String ys = String.format("%.2f", p.getY());
        return "("+xs+","+ys+")";
    }
}