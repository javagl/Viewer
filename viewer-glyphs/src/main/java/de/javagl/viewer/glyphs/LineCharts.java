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

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.List;

/**
 * Methods to create {@link LineChart} instances
 */
public class LineCharts
{
    /**
     * A default stroke
     */
    private static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);
    
    /**
     * Create a simple {@link LineChart} that is a <i>view</i> on the given 
     * list. This means that changes in the given list will be visible in 
     * the returned chart.<br>
     * <br>
     * If any of the values in the given list is <code>null</code>, then
     * 0.0 will be used as the {@link LineChart#getValue(int) value}.
     * 
     * @param list The list 
     * @param paint The paint for the line chart. If this is <code>null</code>,
     * then nothing will be painted
     * @return The {@link LineChart}
     */
    public static LineChart createFromList(
        List<? extends Number> list, Paint paint)
    {
        return createFromList(list, paint, DEFAULT_STROKE, null);
    }
    
    /**
     * Create a simple {@link LineChart} that is a <i>view</i> on the given 
     * list. This means that changes in the given list will be visible in 
     * the returned chart.<br>
     * <br>
     * If any of the values in the given list is <code>null</code>, then
     * 0.0 will be used as the {@link LineChart#getValue(int) value}.
     * 
     * @param list The list 
     * @param paint The paint for the line chart. If this is <code>null</code>,
     * then nothing will be painted
     * @param stroke The stroke for the line chart. If this is 
     * <code>null</code>, then no line will be painted.
     * @param tickShape The tick shape. If this is <code>null</code>, then
     * no ticks will be painted
     * @return The {@link LineChart}
     */
    public static LineChart createFromList(
        List<? extends Number> list, 
        Paint paint, Stroke stroke, Shape tickShape)
    {
        return new LineChart()
        {
            @Override
            public int getNumPoints()
            {
                return list.size();
            }
            
            @Override
            public double getValue(int index)
            {
                Number number = list.get(index);
                if (number == null)
                {
                    return 0.0;
                }
                return number.doubleValue();
            }
            
            @Override
            public Paint getPaint()
            {
                return paint;
            }

            @Override
            public Stroke getStroke()
            {
                return stroke;
            }

            @Override
            public Shape getTickShape()
            {
                return tickShape;
            }
            
        };
    }
    
    /**
     * Returns the minimum {@link LineChart#getValue(int) value} of the given
     * line chart. Returns <code>POSITIVE_INFINITY</code> if the chart is
     * empty. 
     * 
     * @param lineChart The {@link LineChart}
     * @return The minimum value
     */
    public static double computeMin(LineChart lineChart)
    {
        double min = Double.POSITIVE_INFINITY;
        int n = lineChart.getNumPoints();
        for (int i = 0; i < n; i++)
        {
            min = Math.min(min, lineChart.getValue(i));
        }
        return min;
    }

    /**
     * Returns the maximum {@link LineChart#getValue(int) value} of the given
     * line chart. Returns <code>NEGATIVE_INFINITY</code> if the chart is
     * empty. 
     * 
     * @param lineChart The {@link LineChart}
     * @return The maximum value
     */
    public static double computeMax(LineChart lineChart)
    {
        double max = Double.NEGATIVE_INFINITY;
        int n = lineChart.getNumPoints();
        for (int i = 0; i < n; i++)
        {
            max = Math.max(max, lineChart.getValue(i));
        }
        return max;
    }
    
    /**
     * Compute the minimum of the given {@link LineChart}s. If the given 
     * sequence is empty, or none of its elements as any points, then 
     * <code>POSITIVE_INFINITY</code> will be returned.
     * 
     * @param lineCharts The {@link LineChart}s
     * @return The minimum value
     */
    public static double computeMin(Iterable<? extends LineChart> lineCharts)
    {
        double min = Double.POSITIVE_INFINITY;
        for (LineChart lineChart : lineCharts)
        {
            min = Math.min(min, LineCharts.computeMin(lineChart));
        }
        return min;
    }

    /**
     * Compute the maximum of the given {@link LineChart}s. If the given 
     * sequence is empty, or none of its elements as any points, then 
     * <code>NEGATIVE_INFINITY</code> will be returned.
     * 
     * @param lineCharts The {@link LineChart}s
     * @return The maximum value
     */
    public static double computeMax(Iterable<? extends LineChart> lineCharts)
    {
        double max = Double.NEGATIVE_INFINITY;
        for (LineChart lineChart : lineCharts)
        {
            max = Math.max(max, LineCharts.computeMax(lineChart));
        }
        return max;
    }
    
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the minimum of the given {@link LineChart} will be returned.
     * If the {@link LineChart} does not have any points, then 0.0 will be
     * returned.
     * 
     * @param optionalMin The optional minimum value
     * @param lineChart The {@link LineChart}
     * @return The minimum value
     */
    static double getMin(double optionalMin, LineChart lineChart)
    {
        if (!Double.isNaN(optionalMin))
        {
            return optionalMin;
        }
        double min = LineCharts.computeMin(lineChart);
        if (!Double.isFinite(min))
        {
            return 0.0;
        }
        return min;
    }
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the maximum of the given {@link LineChart} will be returned.
     * If the {@link LineChart} does not have any points, then 1.0 will be
     * returned.
     * 
     * @param optionalMax The optional maximum value
     * @param lineChart The {@link LineChart}
     * @return The maximum value
     */
    static double getMax(double optionalMax, LineChart lineChart)
    {
        if (!Double.isNaN(optionalMax))
        {
            return optionalMax;
        }
        double max = LineCharts.computeMax(lineChart);
        if (!Double.isFinite(max))
        {
            return 1.0;
        }
        return max;
    }
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the minimum of the given {@link LineChart}s will be returned.
     * If the {@link LineChart} list is empty, or none of them has any points, 
     * then 0.0 will be returned.
     * 
     * @param optionalMin The optional minimum value
     * @param lineCharts The {@link LineChart}s
     * @return The minimum value
     */
    static double getMin(double optionalMin, 
        Iterable<? extends LineChart> lineCharts)
    {
        if (!Double.isNaN(optionalMin))
        {
            return optionalMin;
        }
        double min = computeMin(lineCharts);
        if (!Double.isFinite(min))
        {
            return 0.0;
        }
        return min;
    }

    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the maximum of the given {@link LineChart}s will be returned.
     * If the {@link LineChart} list is empty, or none of them has any points,
     * then 1.0 will be returned.
     * 
     * @param optionalMax The optional maximum value
     * @param lineCharts The {@link LineChart}s
     * @return The maximum value
     */
    static double getMax(double optionalMax, 
        Iterable<? extends LineChart> lineCharts)
    {
        if (!Double.isNaN(optionalMax))
        {
            return optionalMax;
        }
        double max = computeMax(lineCharts);
        if (!Double.isFinite(max))
        {
            return 1.0;
        }
        return max;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private LineCharts()
    {
        // Private constructor to prevent instantiation
    }

}
