/*
 * www.javagl.de - Viewer - Glyphs
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
package de.javagl.viewer.glyphs;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;
import java.util.function.IntToDoubleFunction;

/**
 * Methods to create {@link ScatterChart} instances
 */
public class ScatterCharts
{
    /**
     * Create a {@link ScatterChart} that is a view on the given list of
     * points. Changes in the given list will be visible in the returned
     * chart.
     * 
     * @param points The list of points
     * @param fillPaint The paint for filling the points. If this is 
     * <code>null</code> then no points will be filled.
     * @param drawPaint The paint for drawing the point outlines. If this is 
     * <code>null</code> then no point outlines will be drawn.
     * @param drawStroke The stroke that will be used for drawing the point
     * outlines. If this is <code>null</code>, then no outlines will be drawn
     * @param shape The shape for the points. If this is <code>null</code>,
     * then no points will be painted.
     * @return The {@link ScatterChart}
     * @throws NullPointerException If the given list is <code>null</code>
     */
    public static ScatterChart create(
        List<? extends Point2D> points, 
        Paint fillPaint, Paint drawPaint, Stroke drawStroke, Shape shape)
    {
        Objects.requireNonNull(points, "The points are null");
        return create(
            points.size(),
            index -> points.get(index).getX(),
            index -> points.get(index).getY(),
            fillPaint, drawPaint, drawStroke, shape);
    }
    
    /**
     * Create a {@link ScatterChart} that is a view on the given lists of
     * point coordinates. Changes in the given lists will be visible in 
     * the returned chart, but care has to be taken that the lists always
     * have the same size.
     * 
     * @param xCoordinates The list of x-coordinates 
     * @param yCoordinates The list of y-coordinates 
     * @param fillPaint The paint for filling the points. If this is 
     * <code>null</code> then no points will be filled.
     * @param drawPaint The paint for drawing the point outlines. If this is 
     * <code>null</code> then no point outlines will be drawn.
     * @param drawStroke The stroke that will be used for drawing the point
     * outlines. If this is <code>null</code>, then no outlines will be drawn
     * @param shape The shape for the points. If this is <code>null</code>,
     * then no points will be painted.
     * @return The {@link ScatterChart}
     * @throws NullPointerException If any of the given lists is 
     * <code>null</code>
     * @throws IllegalArgumentException If the given lists have a different
     * size. This can only be checked at creation time.
     */
    public static ScatterChart create(
        List<? extends Number> xCoordinates,
        List<? extends Number> yCoordinates, 
        Paint fillPaint, Paint drawPaint, Stroke drawStroke, Shape shape)
    {
        Objects.requireNonNull(xCoordinates, "The xCoordinates are null");
        Objects.requireNonNull(yCoordinates, "The yCoordinates are null");
        if (xCoordinates.size() != yCoordinates.size())
        {
            throw new IllegalArgumentException(
                "The xCoordinates have a size of "+xCoordinates.size()+
                " and the xCoordinates have a size of "+yCoordinates.size());
        }
        return create(
            xCoordinates.size(),
            index -> xCoordinates.get(index).doubleValue(),
            index -> yCoordinates.get(index).doubleValue(),
            fillPaint, drawPaint, drawStroke, shape);
    }

    /**
     * Create a {@link ScatterChart} that is a view on the given 
     * point coordinates. Changes in the given functions will be 
     * visible in the returned chart, but care has to be taken that 
     * the lists always have the same size.
     * 
     * @param numPoints The number of points
     * @param xCoordinates The x-coordinates 
     * @param yCoordinates The y-coordinates 
     * @param fillPaint The paint for filling the points. If this is 
     * <code>null</code> then no points will be filled.
     * @param drawPaint The paint for drawing the point outlines. If this is 
     * <code>null</code> then no point outlines will be drawn.
     * @param drawStroke The stroke that will be used for drawing the point
     * outlines. If this is <code>null</code>, then no outlines will be drawn
     * @param shape The shape for the points. If this is <code>null</code>,
     * then no points will be painted.
     * @return The {@link ScatterChart}
     * @throws NullPointerException If any of the given lists is 
     * <code>null</code>
     * @throws IllegalArgumentException If number of points is negative
     */
    public static ScatterChart create(
        int numPoints,
        IntToDoubleFunction xCoordinates,
        IntToDoubleFunction yCoordinates, 
        Paint fillPaint, Paint drawPaint,
        Stroke drawStroke, Shape shape)
    {
        Objects.requireNonNull(xCoordinates, "The xCoordinates are null");
        Objects.requireNonNull(yCoordinates, "The yCoordinates are null");
        if (numPoints < 0)
        {
            throw new IllegalArgumentException(
                "The numPoints may not be negative, but is " + numPoints);
        }
            
        return new ScatterChart()
        {
            @Override
            public int getNumPoints()
            {
                return numPoints;
            }

            @Override
            public double getPointX(int index)
            {
                return xCoordinates.applyAsDouble(index);
            }

            @Override
            public double getPointY(int index)
            {
                return yCoordinates.applyAsDouble(index);
            }
            
            @Override
            public Paint getFillPaint(int index)
            {
                return fillPaint;
            }
            
            @Override
            public Paint getDrawPaint(int index)
            {
                return drawPaint;
            }
            
            @Override
            public Stroke getDrawStroke(int index)
            {
                return drawStroke;
            }
            
            @Override
            public Shape getShape(int index)
            {
                return shape;
            }
        };
    }
    
    /**
     * Returns the minimum x-coordinate that appears in the given
     * chart. Returns <code>POSITIVE_INFINITY</code> if the chart is
     * empty. 
     * 
     * @param scatterChart The {@link ScatterChart}
     * @return The minimum value
     */
    public static double computeMinX(ScatterChart scatterChart)
    {
        double min = Double.POSITIVE_INFINITY;
        int n = scatterChart.getNumPoints();
        for (int i=0; i<n; i++)
        {
            min = Math.min(min, scatterChart.getPointX(i));
        }
        return min;
    }

    /**
     * Returns the minimum y-coordinate that appears in the given
     * chart. Returns <code>POSITIVE_INFINITY</code> if the chart is
     * empty. 
     * 
     * @param scatterChart The {@link ScatterChart}
     * @return The minimum value
     */
    public static double computeMinY(ScatterChart scatterChart)
    {
        double min = Double.POSITIVE_INFINITY;
        int n = scatterChart.getNumPoints();
        for (int i=0; i<n; i++)
        {
            min = Math.min(min, scatterChart.getPointY(i));
        }
        return min;
    }

    /**
     * Returns the minimum x-coordinate that appears in the given
     * chart. Returns <code>NEGATIVE_INFINITY</code> if the chart is
     * empty. 
     * 
     * @param scatterChart The {@link ScatterChart}
     * @return The maximum value
     */
    public static double computeMaxX(ScatterChart scatterChart)
    {
        double max = Double.NEGATIVE_INFINITY;
        int n = scatterChart.getNumPoints();
        for (int i=0; i<n; i++)
        {
            max = Math.max(max, scatterChart.getPointX(i));
        }
        return max;
    }

    /**
     * Returns the maximum y-coordinate that appears in the given
     * chart. Returns <code>NEGATIVE_INFINITY</code> if the chart is
     * empty. 
     * 
     * @param scatterChart The {@link ScatterChart}
     * @return The maximum value
     */
    public static double computeMaxY(ScatterChart scatterChart)
    {
        double max = Double.NEGATIVE_INFINITY;
        int n = scatterChart.getNumPoints();
        for (int i=0; i<n; i++)
        {
            max = Math.max(max, scatterChart.getPointY(i));
        }
        return max;
    }
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the minimum x-value of the given {@link ScatterChart} will 
     * be returned. If the {@link ScatterChart} does not have any points, 
     * then 0.0 will be returned.
     * 
     * @param optionalMin The optional minimum value
     * @param scatterChart The {@link ScatterChart}
     * @return The minimum value
     */
    static double getMinX(double optionalMin, ScatterChart scatterChart)
    {
        if (!Double.isNaN(optionalMin))
        {
            return optionalMin;
        }
        if (scatterChart.getNumPoints() == 0)
        {
            return 0.0;
        }
        return ScatterCharts.computeMinX(scatterChart);
    }

    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the minimum y-value of the given {@link ScatterChart} will 
     * be returned. If the {@link ScatterChart} does not have any points, 
     * then 0.0 will be returned.
     * 
     * @param optionalMin The optional minimum value
     * @param scatterChart The {@link ScatterChart}
     * @return The minimum value
     */
    static double getMinY(double optionalMin, ScatterChart scatterChart)
    {
        if (!Double.isNaN(optionalMin))
        {
            return optionalMin;
        }
        if (scatterChart.getNumPoints() == 0)
        {
            return 0.0;
        }
        return ScatterCharts.computeMinY(scatterChart);
    }
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the maximum x-value of the given {@link ScatterChart} will 
     * be returned. If the {@link ScatterChart} does not have any points, 
     * then 1.0 will be returned.
     * 
     * @param optionalMax The optional maximum value
     * @param scatterChart The {@link ScatterChart}
     * @return The minimum value
     */
    static double getMaxX(double optionalMax, ScatterChart scatterChart)
    {
        if (!Double.isNaN(optionalMax))
        {
            return optionalMax;
        }
        if (scatterChart.getNumPoints() == 0)
        {
            return 1.0;
        }
        return ScatterCharts.computeMaxX(scatterChart);
    }

    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the maximum y-value of the given {@link ScatterChart} will 
     * be returned. If the {@link ScatterChart} does not have any points, 
     * then 1.0 will be returned.
     * 
     * @param optionalMax The optional maximum value
     * @param scatterChart The {@link ScatterChart}
     * @return The minimum value
     */
    static double getMaxY(double optionalMax, ScatterChart scatterChart)
    {
        if (!Double.isNaN(optionalMax))
        {
            return optionalMax;
        }
        if (scatterChart.getNumPoints() == 0)
        {
            return 1.0;
        }
        return ScatterCharts.computeMaxY(scatterChart);
    }
    
    /**
     * Compute the bounds of the given chart, and return them as a rectangle
     * 
     * @param scatterChart The chart
     * @return The bounds
     */
    static Rectangle2D computeBounds(ScatterChart scatterChart)
    {
        double minX = computeMinX(scatterChart);
        double minY = computeMinY(scatterChart);
        double maxX = computeMaxX(scatterChart);
        double maxY = computeMaxY(scatterChart);
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private ScatterCharts()
    {
        // Private constructor to prevent instantiation
    }

}
