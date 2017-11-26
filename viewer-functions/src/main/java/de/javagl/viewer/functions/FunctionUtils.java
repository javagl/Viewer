/*
 * www.javagl.de - Viewer - Functions
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
package de.javagl.viewer.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Utility methods related to functions in the GUI
 */
class FunctionUtils
{
    /**
     * Estimate the minimum (double) value that the given function has in the
     * interval [min,max]. <br> 
     * <br>
     * If the function returns <code>null</code> or NaN for any value, this 
     * will be ignored. If the function returns <code>null</code> for all 
     * values, then <code>null</code> will be returned. If the function 
     * returns NaN for all values, then NaN will be returned.
     * 
     * @param min The minimum value
     * @param max The maximum value
     * @param function The function
     * @return The estimate for the minimum value
     */
    static Number estimateMinValue(
        DoubleFunction<? extends Number> function, double min, double max)
    {
        return computeMin(function, interpolate(min, max, 100));
    }

    /**
     * Estimate the maximum (double) value that the given function has in the
     * interval [min,max]. <br> 
     * <br>
     * If the function returns <code>null</code> or NaN for any value, this 
     * will be ignored. If the function returns <code>null</code> for all 
     * values, then <code>null</code> will be returned. If the function 
     * returns NaN for all values, then NaN will be returned.
     * 
     * @param min The minimum value
     * @param max The maximum value
     * @param function The function
     * @return The estimate for the maximum value
     */
    static Number estimateMaxValue(
        DoubleFunction<? extends Number> function, double min, double max)
    {
        return computeMax(function, interpolate(min, max, 100));
    }
    
    /**
     * Interpolate between the given minimum and maximum value (inclusive)
     * with the given number of steps and return the result as a list.
     * 
     * @param min The minimum value
     * @param max The maximum value
     * @param steps The number of steps
     * @return The list containing the interpolated values
     */
    static List<Double> interpolate(double min, double max, int steps)
    {
        List<Double> result = new ArrayList<Double>();
        for (int i=0; i<steps; i++)
        {
            double alpha = (double)i / (steps+1);
            double x = min + alpha * (max-min);
            result.add(x);
        }
        return result;
    }
    
    /**
     * Compute the minimum (double) value of applying the given function to 
     * the given values.<br> 
     * <br>
     * If the function returns <code>null</code> or NaN for any value, this 
     * will be ignored. If the function returns <code>null</code> for all 
     * values, then <code>null</code> will be returned. If the function 
     * returns NaN for all values, then NaN will be returned.
     * 
     * @param function The function
     * @param values The values
     * @return The minimum
     */
    private static Number computeMin(
        DoubleFunction<? extends Number> function, Iterable<Double> values)
    {
        Number min = null;
        for (double x : values)
        {
            Number y = function.apply(x);
            min = min(min, y);
        }
        return min;
    }

    /**
     * Compute the maximum (double) value of applying the given function to 
     * the given values.<br> 
     * <br>
     * If the function returns <code>null</code> or NaN for any value, this 
     * will be ignored. If the function returns <code>null</code> for all 
     * values, then <code>null</code> will be returned. If the function 
     * returns NaN for all values, then NaN will be returned.
     *  
     * @param function The function
     * @param values The values
     * @return The maximum
     */
    private static Number computeMax(
        DoubleFunction<? extends Number> function, Iterable<Double> values)
    {
        Number max = null;
        for (double x : values)
        {
            Number y = function.apply(x);
            max = max(max, y);
        }
        return max;
    }
    
    /**
     * Returns the minimum (double) of the given values.<br> 
     * <br>
     * If either value is <code>null</code>, then the other value will be 
     * returned. If both values are <code>null</code>, then <code>null</code>
     * will be returned. If neither value is <code>null</code>, and either 
     * value is NaN, then the other value will be returned. If both values 
     * are NaN, then NaN will be returned.
     * 
     * @param n0 The first value
     * @param n1 The second value 
     * @return The minimum
     */
    static Number min(Number n0, Number n1)
    {
        if (n0 == null)
        {
            return n1;
        }
        if (n1 == null)
        {
            return n0;
        }
        double d0 = n0.doubleValue();
        double d1 = n1.doubleValue();
        if (Double.isNaN(d0))
        {
            return d1;
        }
        if (Double.isNaN(d1))
        {
            return d0;
        }
        return Math.min(d0, d1);
    }
    
    /**
     * Returns the maximum (double) of the given values.<br> 
     * <br>
     * If either value is <code>null</code>, then the other value will be 
     * returned. If both values are <code>null</code>, then <code>null</code>
     * will be returned. If neither value is <code>null</code>, and either 
     * value is NaN, then the other value will be returned. If both values 
     * are NaN, then NaN will be returned.
     * 
     * @param n0 The first value
     * @param n1 The second value 
     * @return The maximum
     */
    static Number max(Number n0, Number n1)
    {
        if (n0 == null)
        {
            return n1;
        }
        if (n1 == null)
        {
            return n0;
        }
        double d0 = n0.doubleValue();
        double d1 = n1.doubleValue();
        if (Double.isNaN(d0))
        {
            return d1;
        }
        if (Double.isNaN(d1))
        {
            return d0;
        }
        return Math.max(d0, d1);
    }
    

    /**
     * Private constructor to prevent instantiation
     */
    private FunctionUtils()
    {
        // Private constructor to prevent instantiation
    }
}
