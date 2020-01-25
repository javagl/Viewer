/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2020 Marco Hutter - http://www.javagl.de
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
package de.javagl.viewer.selection;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntToDoubleFunction;

/**
 * Convenience methods for {@link PointBasedSelector} and 
 * {@link ShapeBasedSelector} implementations.<br>
 * <br>
 * These methods are preliminary and may change in the future.
 */
public class Selectors
{
    /**
     * Compute the list of points in the given sequence that have a distance
     * to the given point that is not larger than the given point radius.
     * The given point is in screen coordinates. The points in the sequence
     * are in world coordinates.
     * 
     * @param point The point
     * @param worldToScreen The world-to-screen transform
     * @param points The points
     * @param pointRadius The point radius
     * @return The relevant points
     */
    public static List<Point2D> computePointsForPoint(
        Point2D point, AffineTransform worldToScreen, 
        Iterable<? extends Point2D> points, double pointRadius)
    {
        List<Point2D> result = new ArrayList<Point2D>();
        double rr = pointRadius * pointRadius;
        Point2D q = new Point2D.Double();
        for (Point2D p : points)
        {
            worldToScreen.transform(p, q);
            double ds = q.distanceSq(point);
//            System.out.println("At " + point + " with " + p + " and " + q
//                + " distance is " + Math.sqrt(ds));
            if (ds <= rr)
            {
                result.add(p);
            }
        }
        return result;
    }
    
    /**
     * Compute the list of indices of points in the given sequence that have 
     * a distance to the given point that is not larger than the given point 
     * radius. The given point is in screen coordinates. The points in the 
     * sequence are in world coordinates.
     * 
     * @param point The point
     * @param worldToScreen The world-to-screen transform
     * @param numPoints The number of points
     * @param getX The function that provides the x-coordinates of the points
     * @param getY The function that provides the y-coordinates of the points
     * @param pointRadius The point radius
     * @return The relevant points
     */
    public static List<Integer> computePointIndicesForPoint(
        Point2D point, AffineTransform worldToScreen, int numPoints,
        IntToDoubleFunction getX, 
        IntToDoubleFunction getY, 
        double pointRadius)
    {
        double rr = pointRadius * pointRadius;
        Point2D p = new Point2D.Double();
        Point2D q = new Point2D.Double();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < numPoints; i++)
        {
            double x = getX.applyAsDouble(i);
            double y = getY.applyAsDouble(i);
            p.setLocation(x, y);
            worldToScreen.transform(p, q);
            double ds = q.distanceSq(point);
//            System.out.println("At " + point + " with " + p + " and " + q
//                + " distance is " + Math.sqrt(ds));
            if (ds <= rr)
            {
                indices.add(i);
            }
        }
        return indices;
    }
    
    /**
     * Compute the list of points in the given sequence that are contained
     * in the given shape. The given shape is in screen coordinates. The 
     * points in the sequence are in world coordinates.
     * 
     * @param shape The shape
     * @param worldToScreen The world-to-screen transform
     * @param points The points
     * @return The relevant points
     */
    public static List<Point2D> computePointsForShape(
        Shape shape, AffineTransform worldToScreen, 
        Iterable<? extends Point2D> points)
    {
        List<Point2D> result = new ArrayList<Point2D>();
        Point2D q = new Point2D.Double();
        for (Point2D p : points)
        {
            worldToScreen.transform(p, q);
            if (shape.contains(q))
            {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Compute the list of indices in the given sequence that represent
     * points that are contained in the given shape. The given shape is
     * in screen coordinates. The points in the sequence are in world 
     * coordinates.
     * 
     * @param shape The shape
     * @param worldToScreen The world-to-screen transform
     * @param numPoints The number of points
     * @param getX The function that provides the x-coordinates of the points
     * @param getY The function that provides the y-coordinates of the points
     * @return The relevant points
     */
    public static List<Integer> computePointIndicesForShape(
        Shape shape, AffineTransform worldToScreen, int numPoints,
        IntToDoubleFunction getX, 
        IntToDoubleFunction getY)
    {
        Point2D p = new Point2D.Double();
        Point2D q = new Point2D.Double();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < numPoints; i++)
        {
            double x = getX.applyAsDouble(i);
            double y = getY.applyAsDouble(i);
            p.setLocation(x, y);
            worldToScreen.transform(p, q);
            if (shape.contains(q))
            {
                indices.add(i);
            }
        }
        return indices;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Selectors()
    {
        // Private constructor to prevent instantiation
    }
}
