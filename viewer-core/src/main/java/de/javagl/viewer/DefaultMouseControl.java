/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.viewer;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.function.Predicate;

import javax.swing.event.MouseInputAdapter;

/**
 * A default implementation of the {@link MouseControl} interface, summarizing 
 * the default mouse interaction for a {@link Viewer}.<br>
 * <br> 
 * This class is not part of the public API, and may be omitted in the future.
 */
class DefaultMouseControl extends MouseInputAdapter implements MouseControl
{
    /**
     * The viewer that is controlled by this instance
     */
    private final Viewer viewer;
    
    /**
     * The previous mouse position
     */
    private final Point previousPoint = new Point();
    
    /**
     * The position where the mouse was previously pressed
     */
    private final Point pressPoint = new Point();

    /**
     * The current zooming speed
     */
    private final double zoomingSpeed = 0.15;
    
    /**
     * The current rotation speed
     */
    private final double rotationSpeed = 0.4;
    
    /**
     * The predicate that is checked for a mouse drag event to determine
     * whether a translation should be done
     */
    private final Predicate<MouseEvent> translatePredicate =
        InputEventPredicates.buttonDown(3);
    
    /**
     * The predicate that is checked for a mouse drag event to determine
     * whether a rotation should be done
     */
    private Predicate<MouseEvent> rotatePredicate =
        InputEventPredicates.buttonDown(1);

    /**
     * The predicate that is checked for a mouse wheel event to determine
     * whether the zooming should be restricted to the x-axis
     */
    private Predicate<MouseEvent> zoomPredicateRestrictToX =
        InputEventPredicates.shiftDown();

    /**
     * The predicate that is checked for a mouse wheel event to determine
     * whether the zooming should be restricted to the y-axis
     */
    private Predicate<MouseEvent> zoomPredicateRestrictToY =
        InputEventPredicates.controlDown();
    
    /**
     * Default constructor
     * 
     * @param viewer The viewer that is controlled by this instance
     */
    DefaultMouseControl(Viewer viewer)
    {
        this.viewer = viewer;
    }
    
    /**
     * Set whether rotations are allowed 
     * 
     * @param rotationAllowed Whether rotations are allowed
     */
    void setRotationAllowed(boolean rotationAllowed)
    {
        if (rotationAllowed)
        {
            this.rotatePredicate = InputEventPredicates.buttonDown(1);
        }
        else
        {
            this.rotatePredicate = InputEventPredicates.alwaysFalse();
        }
    }
    
    /**
     * Set whether non-uniform scaling is allowed 
     * 
     * @param nonUniformScalingAllowed The flag
     */
    void setNonUniformScalingAllowed(boolean nonUniformScalingAllowed)
    {
        if (nonUniformScalingAllowed)
        {
            this.zoomPredicateRestrictToX = InputEventPredicates.shiftDown();
            this.zoomPredicateRestrictToY = InputEventPredicates.controlDown();
        }
        else
        {
            this.zoomPredicateRestrictToX = InputEventPredicates.alwaysFalse();
            this.zoomPredicateRestrictToY = InputEventPredicates.alwaysFalse();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        pressPoint.setLocation(e.getPoint());
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        double d = Math.pow(1+zoomingSpeed, e.getWheelRotation())-1;
        double factorX = 1.0 + d;
        double factorY = 1.0 + d;
        if (zoomPredicateRestrictToX.test(e))
        {
            factorY = 1.0;
        }
        if (zoomPredicateRestrictToY.test(e))
        {
            factorX = 1.0;
        }
        viewer.zoom(e.getX(), e.getY(), factorX, factorY);
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (translatePredicate.test(e))
        {
            int dx = e.getX() - previousPoint.x;
            int dy = e.getY() - previousPoint.y;
            viewer.translate(dx, dy);
        }
        if (rotatePredicate.test(e))
        {
            int dy = e.getY() - previousPoint.y;
            viewer.rotate(
                pressPoint.x, pressPoint.y, 
                Math.toRadians(dy)*rotationSpeed);
        }
        previousPoint.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        previousPoint.setLocation(e.getX(), e.getY());
    }
}