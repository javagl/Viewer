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
package de.javagl.viewer.painters;

import java.awt.geom.AffineTransform;

import de.javagl.geom.AffineTransforms;

/**
 * Utility methods related to computations of ticks on axes
 */
class Axes
{
    /**
     * The default values to snap to
     */
    private static final double DEFAULT_SNAP_VALUES[] = 
        new double[]{ 1.0, 2.0, 5.0 };
    
    /**
     * Returns a format string that can be used in <code>String#format</code> 
     * to format values of the given order. The exact meaning of this is 
     * intentionally left unspecified, but for numbers that are "reasonable" 
     * to be displayed as decimal numbers (without scientific notation),
     * this function will return a format with the "appropriate" number of
     * decimal digits in order to format axis labels.  
     * 
     * @param order The order
     * @return The format string
     */
    static String formatStringFor(double order)
    {
        if (order < 1e-100 || !Double.isFinite(order))
        {
            return "%f";
        }
        double exponent = Math.floor(Math.log10(order));
        int digits = (int)Math.abs(exponent);
        if (order >= 1.0)
        {
            digits = 0;
        }
        String result = "%."+digits+"f";
        return result;
    }
    
    /**
     * Computes the coordinates of ticks for an axis with the given parameters
     * 
     * @param worldMin The minimum world coordinate on the axis
     * @param worldMax The maximum world coordinate on the axis
     * @param worldTickDistance The distance that two ticks
     * should have in world coordinates
     * @return The world coordinates for the ticks. If the given worldMax
     * value is smaller than the worldMin, then an empty array will be
     * returned.
     */
    static double[] computeWorldTicks(
        double worldMin, double worldMax, double worldTickDistance)
    {
        if (worldMax < worldMin)
        {
            return new double[0];
        }
        long nMin = (long) Math.ceil(worldMin / worldTickDistance);
        long nMax = (long) Math.floor(worldMax / worldTickDistance);
        int n = (int) (nMax - nMin + 1);
        double worldTicks[] = new double[n];
        for (long i = nMin; i <= nMax; i++)
        {
            double w = i * worldTickDistance;
            worldTicks[(int) (i - nMin)] = w;
        }
        return worldTicks;
    }
    
    /**
     * Computes the smallest value greater than or equal to the given value, 
     * and that is of the form d*(10^x), where d is any of {1.0, 2.0, 5.0} 
     * 
     * @param value The value
     * @return The snapped value
     */
    static double computeSnappedUpValue(double value)
    {
        return computeSnappedUpValue(value, DEFAULT_SNAP_VALUES);
    }
    
    /**
     * Computes the smallest value greater than or equal to the given value, 
     * and that is of the form d*(10^x), where d is any of the given divisors 
     * 
     * @param value The value
     * @param divisors The divisors
     * @return The snapped value
     */
    static double computeSnappedUpValue(double value, double divisors[])
    {
        final double epsilon = 1e-8;
        double exponent = Math.floor(Math.log10(value));
        double scaling = Math.pow(10, exponent);
        double scaledValue = value / scaling;
        double bestDivisor = 10.0 * divisors[0];
        for (int i = 0; i < divisors.length; i++)
        {
            double divisor = divisors[i];
            if (scaledValue <= divisor * (1.0 + epsilon))
            {
                bestDivisor = divisor;
                break;
            }
        }
        double snappedValue = bestDivisor * scaling;
        return snappedValue;
    }
    
    /**
     * Computes the greatest value smaller than or equal to the given value, 
     * and that is of the form d*(10^x), where d is any of {1.0, 2.0, 5.0} 
     * 
     * @param value The value
     * @return The snapped value
     */
    static double computeSnappedDownValue(double value)
    {
        return computeSnappedDownValue(value, DEFAULT_SNAP_VALUES);
    }
    
    /**
     * Computes the smallest value greater than or equal to the given value, 
     * and that is of the form d*(10^x), where d is any of the given divisors 
     * 
     * @param value The value
     * @param divisors The divisors
     * @return The snapped value
     */
    static double computeSnappedDownValue(double value, double divisors[])
    {
        final double epsilon = 1e-8;
        double exponent = Math.floor(Math.log10(value));
        double scaling = Math.pow(10, exponent);
        double scaledValue = value / scaling;
        double bestDivisor = divisors[0];
        for (int i = 0; i < divisors.length; i++)
        {
            double divisor = divisors[i];
            if (scaledValue * (1.0 + epsilon) >= divisor)
            {
                bestDivisor = divisor;
            }
        }
        double snappedValue = bestDivisor * scaling;
        return snappedValue;
    }
    
    /**
     * Computes the distance that two ticks on the x-axis should have
     * in world coordinates. This will be the given screen tick distance,
     * converted to world coordinates and snapped to a "nice" value.
     * 
     * @param worldToScreen The world-to-screen transform
     * @param minScreenTickDistanceX  The minimum distance of two ticks
     * in screen coordinates
     * @return The tick distance in world coordinates
     */
    static double computeWorldTickDistanceX(
        AffineTransform worldToScreen, double minScreenTickDistanceX)
    {
        double unitLengthScreenX =
            AffineTransforms.computeDistanceX(worldToScreen, 1.0);
        double minWorldTickDistanceX =
            minScreenTickDistanceX / unitLengthScreenX;
        double worldTickDistanceX =
            computeSnappedUpValue(minWorldTickDistanceX);
        return worldTickDistanceX;
    }

    /**
     * Computes the distance that two ticks on the x-axis should have
     * in world coordinates. This will be the given screen tick distance,
     * converted to world coordinates and snapped to a "nice" value.
     * 
     * @param worldToScreen The world-to-screen transform
     * @param minScreenTickDistanceY The minimum distance of two ticks
     * in screen coordinates
     * @return The tick distance in world coordinates
     */
    static double computeWorldTickDistanceY(
        AffineTransform worldToScreen, double minScreenTickDistanceY)
    {
        double unitLengthScreenY =
            AffineTransforms.computeDistanceY(worldToScreen, 1.0);
        double minWorldTickDistanceY =
            minScreenTickDistanceY / unitLengthScreenY;
        double worldTickDistanceY =
            computeSnappedUpValue(minWorldTickDistanceY);
        return worldTickDistanceY;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Axes()
    {
        // Private constructor to prevent instantiation
    }

    
}