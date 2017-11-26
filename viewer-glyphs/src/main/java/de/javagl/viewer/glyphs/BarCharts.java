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
import java.util.List;
import java.util.Locale;

/**
 * Methods to create {@link BarChart} instances
 */
public class BarCharts
{
    /**
     * Create a simple {@link BarChart} that is a <i>view</i> on the given list.
     * This means that changes in the given list will be visible in the returned
     * chart.<br>
     * <br>
     * The chart will contain {@link BarChart#getValueString(int) value strings}
     * of the values in the list, with an unspecified string format.<br>
     * <br>
     * If any of the values in the given list is <code>null</code>, then
     * 0.0 will be used as the {@link BarChart#getValue(int) value}.
     * 
     * @param list The list 
     * @param barPaint The paint (color) to use for the bars
     * @return The {@link BarChart}
     */
    public static BarChart createFromList(
        List<? extends Number> list, Paint barPaint)
    {
        return createFromList(list, barPaint, false, true);
    }
    
    /**
     * Create a simple {@link BarChart} that is a <i>view</i> on the given 
     * list. This means that changes in the given list will be visible in 
     * the returned chart.<br>
     * <br>
     * If any of the values in the given list is <code>null</code>, then
     * 0.0 will be used as the {@link BarChart#getValue(int) value}.
     * 
     * @param list The list 
     * @param barPaint The paint (color) to use for the bars
     * @param paintLabels Whether the chart should have 
     * {@link BarChart#getLabel(int) labels}. These will just be the indices 
     * of the bars. 
     * @param paintValueStrings Whether the chart should have  
     * {@link BarChart#getValueString(int) value strings}. These will be
     * the string values of the values, in an unspecified format.
     * @return The {@link BarChart}
     */
    public static BarChart createFromList(
        List<? extends Number> list, Paint barPaint, 
        boolean paintLabels, boolean paintValueStrings)
    {
        return new BarChart()
        {
            @Override
            public String getValueString(int index)
            {
                if (!paintValueStrings)
                {
                    return null;
                }
                return String.format(Locale.ENGLISH, "%.3f", getValue(index));
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
            public int getNumBars()
            {
                return list.size();
            }
            
            @Override
            public String getLabel(int index)
            {
                if (!paintLabels)
                {
                    return null;
                }
                return String.valueOf(index);
            }
            
            @Override
            public Paint getBarPaint(int index)
            {
                return barPaint;
            }
        };
    }
    
    /**
     * Computes the minimum value that appears in the given {@link BarChart}.
     * Returns <code>POSITIVE_INFINITY</code> if the given chart is empty.
     * 
     * @param barChart The {@link BarChart}
     * @return The minimum value
     */
    public static double computeMin(BarChart barChart)
    {
        double min = Double.POSITIVE_INFINITY;
        int n = barChart.getNumBars();
        for (int i=0; i<n; i++)
        {
            min = Math.min(min, barChart.getValue(i));
        }
        return min;
    }
    
    /**
     * Computes the maximum value that appears in the given {@link BarChart}.
     * Returns <code>NEGATIVE_INFINITY</code> if the given chart is empty.
     * 
     * @param barChart The {@link BarChart}
     * @return The maximum value
     */
    public static double computeMax(BarChart barChart)
    {
        double max = Double.NEGATIVE_INFINITY;
        int n = barChart.getNumBars();
        for (int i=0; i<n; i++)
        {
            max = Math.max(max, barChart.getValue(i));
        }
        return max;
    }

    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the minimum of the given {@link BarChart} will be returned.
     * If the {@link BarChart} does not have any bars, then 0.0 will be
     * returned.
     * 
     * @param optionalMin The optional minimum value
     * @param barChart The {@link BarChart}
     * @return The minimum value
     */
    public static double getMin(double optionalMin, BarChart barChart)
    {
        if (!Double.isNaN(optionalMin))
        {
            return optionalMin;
        }
        if (barChart.getNumBars() == 0)
        {
            return 0.0;
        }
        return BarCharts.computeMin(barChart);
    }
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the maximum of the given {@link BarChart} will be returned.
     * If the {@link BarChart} does not have any bars, then 1.0 will be
     * returned.
     * 
     * @param optionalMax The optional maximum value
     * @param barChart The {@link BarChart}
     * @return The maximum value
     */
    public static double getMax(double optionalMax, BarChart barChart)
    {
        if (!Double.isNaN(optionalMax))
        {
            return optionalMax;
        }
        if (barChart.getNumBars() == 0)
        {
            return 1.0;
        }
        return BarCharts.computeMax(barChart);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private BarCharts()
    {
        // Private constructor to prevent instantiation
    }

}
