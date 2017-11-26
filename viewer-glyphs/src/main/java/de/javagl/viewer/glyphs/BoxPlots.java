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

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;


/**
 * Methods to create {@link BoxPlot} instances
 */
public class BoxPlots
{
    /**
     * Creates a new {@link BoxPlot} from the given stream of 
     * <code>double</code> values
     * 
     * @param stream The stream of values
     * @return The {@link BoxPlot}
     */
    public static BoxPlot create(DoubleStream stream)
    {
        double array[] = stream.toArray();
        
        Arrays.sort(array);
        double sum = 0;
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (int j=0; j<array.length; j++)
        {
            min = Math.min(min, array[j]);
            max = Math.max(max, array[j]);
            sum += array[j];
        }
        double mean = sum / array.length;
        double lowerQuantile = quantile(array, 0.25);
        double upperQuantile = quantile(array, 0.75);
        double median = quantile(array, 0.5);
        BoxPlot boxPlot = BoxPlots.create(
            min, lowerQuantile, median, upperQuantile, 
            max, mean);
        return boxPlot;
    }

    /**
     * Computes the specified quantile of the given (sorted!) array
     * 
     * @param sortedArray The sorted array
     * @param p The percentage
     * @return The quantile
     */
    private static double quantile(double sortedArray[], double p)
    {
        if (sortedArray.length == 1)
        {
            return sortedArray[0];
        }
        double pos = p * (sortedArray.length + 1);
        double floorPos = Math.floor(pos);
        double d = pos - floorPos;
        if (pos < 1)
        {
            return sortedArray[0];
        }
        if (pos >= sortedArray.length)
        {
            return sortedArray[sortedArray.length-1];
        }
        double lower = sortedArray[(int)floorPos-1];
        double upper = sortedArray[(int)floorPos];
        return lower + d * (upper - lower);
    }
    
    /**
     * Creates a new {@link BoxPlot}
     * 
     * @param minimum The minimum
     * @param lowerQuantile The lower quantile
     * @param median The median
     * @param upperQuantile The upper quantile
     * @param maximum The maximum
     * @return The {@link BoxPlot}
     */
    public static BoxPlot create(
        double minimum, double lowerQuantile,
        double median, double upperQuantile,
        double maximum)
    {
        return create(
            minimum, lowerQuantile, median, 
            upperQuantile, maximum, Double.NaN);
    }
    
    /**
     * Creates a new {@link BoxPlot}
     * 
     * @param minimum The minimum
     * @param lowerQuantile The lower quantile
     * @param median The median
     * @param upperQuantile The upper quantile
     * @param maximum The maximum
     * @param mean The mean
     * @return The {@link BoxPlot}
     */
    public static BoxPlot create(
        double minimum, double lowerQuantile,
        double median, double upperQuantile,
        double maximum, double mean)
    {
        return new BoxPlot()
        {
            @Override
            public double getMinimum()
            {
                return minimum;
            }

            @Override
            public double getLowerQuantile()
            {
                return lowerQuantile;
            }

            @Override
            public double getMedian()
            {
                return median;
            }

            @Override
            public double getUpperQuantile()
            {
                return upperQuantile;
            }

            @Override
            public double getMaximum()
            {
                return maximum;
            }

            @Override
            public double getMean()
            {
                return mean;
            }
        };
    }

    /**
     * Compute the minimum of the given {@link BoxPlot}s. If the given
     * sequence is empty, then <code>POSITIVE_INFINITY</code> will
     * be returned.
     * 
     * @param boxPlots The {@link BoxPlot}s
     * @return The minimum value
     */
    public static double computeMin(Iterable<? extends BoxPlot> boxPlots)
    {
        double min = Double.POSITIVE_INFINITY;
        for (BoxPlot boxPlot : boxPlots)
        {
            min = Math.min(min, boxPlot.getMinimum());
        }
        return min;
    }
    
    /**
     * Compute the maximum of the given {@link BoxPlot}s. If the given
     * sequence is empty, then <code>NEGATIVE_INFINITY</code> will
     * be returned.
     * 
     * @param boxPlots The {@link BoxPlot}s
     * @return The maximum value
     */
    public static double computeMax(Iterable<? extends BoxPlot> boxPlots)
    {
        double max = Double.NEGATIVE_INFINITY;
        for (BoxPlot boxPlot : boxPlots)
        {
            max = Math.max(max, boxPlot.getMaximum());
        }
        return max;
    }
    
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the minimum of the given {@link BoxPlot}s will be returned.
     * If the given collection is empty, or none of its elements has a 
     * finite minimum, then 0.0 will be returned.
     * 
     * @param optionalMin The optional minimum value
     * @param boxPlots The {@link BoxPlot}s
     * @return The minimum value
     */
    public static double getMin(double optionalMin, 
        Collection<? extends BoxPlot> boxPlots)
    {
        if (!Double.isNaN(optionalMin))
        {
            return optionalMin;
        }
        double min = Double.POSITIVE_INFINITY;
        for (BoxPlot boxPlot : boxPlots)
        {
            min = Math.min(min, boxPlot.getMinimum());
        }
        if (!Double.isFinite(min))
        {
            return 0.0;
        }
        return min;
    }
    
    /**
     * If the given optional value is not NaN, then it will be returned.
     * Otherwise, the maximum of the given {@link BoxPlot}s will be returned.
     * If the given collection is empty, or none of its elements has a
     * finite maximum, then 1.0 will be returned.
     * 
     * @param optionalMax The optional maximum value
     * @param boxPlots The {@link BoxPlot}s
     * @return The maximum value
     */
    public static double getMax(
        double optionalMax, Collection<? extends BoxPlot> boxPlots)
    {
        if (!Double.isNaN(optionalMax))
        {
            return optionalMax;
        }
        double max = Double.NEGATIVE_INFINITY;
        for (BoxPlot boxPlot : boxPlots)
        {
            max = Math.max(max, boxPlot.getMaximum());
        }
        if (!Double.isFinite(max))
        {
            return 1.0;
        }
        return max;
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private BoxPlots()
    {
        // Private constructor to prevent instantiation
    }
    
}
