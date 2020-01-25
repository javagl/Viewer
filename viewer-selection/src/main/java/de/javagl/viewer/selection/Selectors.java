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
import java.util.function.IntFunction;
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
     * Compute the list of indices of points in the given sequence that are 
     * represented by a shape that contains the given point. The given point 
     * is in screen coordinates. The points in the sequence are in world 
     * coordinates.
     * 
     * @param point The point
     * @param worldToScreen The world-to-screen transform
     * @param numPoints The number of points
     * @param xFunction The function that provides the x-coordinates
     * @param yFunction The function that provides the y-coordinates
     * @param shapeFunction The function that provides the shapes
     * @return The relevant points
     */
    public static List<Integer> computeShapeIndicesForPoint(
        Point2D point, AffineTransform worldToScreen, int numPoints,
        IntToDoubleFunction xFunction, 
        IntToDoubleFunction yFunction, 
        IntFunction<? extends Shape> shapeFunction)
    {
        Point2D worldPoint = new Point2D.Double();
        Point2D screenPoint = new Point2D.Double();
        Point2D relativeScreenPoint = new Point2D.Double();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < numPoints; i++)
        {
            double x = xFunction.applyAsDouble(i);
            double y = yFunction.applyAsDouble(i);
            worldPoint.setLocation(x, y);
            worldToScreen.transform(worldPoint, screenPoint);
            Shape shape = shapeFunction.apply(i);
            relativeScreenPoint.setLocation(
                point.getX() - screenPoint.getX(),
                point.getY() - screenPoint.getY());
            if (shape.contains(relativeScreenPoint))
            {
                indices.add(i);
            }
        }
        return indices;
    }
    
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
        Point2D screenPoint = new Point2D.Double();
        for (Point2D worldPoint : points)
        {
            worldToScreen.transform(worldPoint, screenPoint);
            double ds = screenPoint.distanceSq(point);
            if (ds <= rr)
            {
                result.add(worldPoint);
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
     * @param xFunction The function that provides the x-coordinates
     * @param yFunction The function that provides the y-coordinates
     * @param pointRadius The point radius
     * @return The relevant points
     */
    public static List<Integer> computePointIndicesForPoint(
        Point2D point, AffineTransform worldToScreen, int numPoints,
        IntToDoubleFunction xFunction, 
        IntToDoubleFunction yFunction, 
        double pointRadius)
    {
        double rr = pointRadius * pointRadius;
        Point2D worldPoint = new Point2D.Double();
        Point2D screenPoint = new Point2D.Double();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < numPoints; i++)
        {
            double x = xFunction.applyAsDouble(i);
            double y = yFunction.applyAsDouble(i);
            worldPoint.setLocation(x, y);
            worldToScreen.transform(worldPoint, screenPoint);
            double ds = screenPoint.distanceSq(point);
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
        Point2D screenPoint = new Point2D.Double();
        for (Point2D worldPoint : points)
        {
            worldToScreen.transform(worldPoint, screenPoint);
            if (shape.contains(screenPoint))
            {
                result.add(worldPoint);
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
     * @param xFunction The function that provides the x-coordinates
     * @param yFunction The function that provides the y-coordinates
     * @return The relevant points
     */
    public static List<Integer> computePointIndicesForShape(
        Shape shape, AffineTransform worldToScreen, int numPoints,
        IntToDoubleFunction xFunction, 
        IntToDoubleFunction yFunction)
    {
        Point2D worldPoint = new Point2D.Double();
        Point2D screenPoint = new Point2D.Double();
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < numPoints; i++)
        {
            double x = xFunction.applyAsDouble(i);
            double y = yFunction.applyAsDouble(i);
            worldPoint.setLocation(x, y);
            worldToScreen.transform(worldPoint, screenPoint);
            if (shape.contains(screenPoint))
            {
                indices.add(i);
            }
        }
        return indices;
    }

    // Could use this as a building block for the common concept of either
    // computing indices or elements directly, but then, the methods that
    // compute elements directly would need a List as their input
    // (at least, as long as they don't involve further generalizations)
//    /**
//     * Select the elements at the given indices from the given list. The
//     * indices must be in a valid range for the elements list, and may
//     * not be <code>null</code>.
//     * 
//     * @param <T> The element type
//     * 
//     * @param elements The elements
//     * @param indices The indices
//     * @return The selected elements
//     */
//    private static <T> List<T> select(
//        List<? extends T> elements, List<Integer> indices)
//    {
//        List<T> result = new ArrayList<T>();
//        for (Integer index : indices)
//        {
//            result.add(elements.get(index));
//        }
//        return result;
//    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Selectors()
    {
        // Private constructor to prevent instantiation
    }
}
