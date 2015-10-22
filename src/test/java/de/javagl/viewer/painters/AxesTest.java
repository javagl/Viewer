/*
 * www.javagl.de - Viewer - Functions
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.painters;

import java.util.Locale;

import de.javagl.viewer.painters.Axes;

/**
 * A simple (non-unit) test for the {@link Axes} class
 */
@SuppressWarnings("javadoc")
public class AxesTest
{
    public static void main(String[] args)
    {
        testSnapping();
        testFormat();
    }
    
    private static void testFormat()
    {
        testFormat(0.0000000001);
        testFormat(0.001);
        testFormat(0.002);
        testFormat(0.005);
        testFormat(0.009);
        testFormat(0.01);
        testFormat(0.1);
        testFormat(1.0);
        testFormat(2.0);
        testFormat(9.0);
        testFormat(10.0);
        testFormat(100.0);
        testFormat(1000000000.0);
        testFormat(Double.POSITIVE_INFINITY);
        testFormat(Double.NEGATIVE_INFINITY);
        testFormat(Double.NaN);
    }

    private static void testFormat(double value)
    {
        String formatString = Axes.formatStringFor(value);
        String s = String.format(Locale.ENGLISH, formatString, value);
        System.out.println("For "+value+" format "+formatString+" gives "+s);
    }
    
    private static void testSnapping()
    {
        testSnapping(0.00000000009);
        testSnapping(0.000000000011);
        
        testSnapping(0.001);
        testSnapping(0.0015);
        testSnapping(0.002);
        testSnapping(0.0025);
        testSnapping(0.005);
        testSnapping(0.006);
        testSnapping(0.011);
        
        testSnapping(0.9);
        testSnapping(1.0);
        testSnapping(1.1);
        testSnapping(1.9);
        testSnapping(2.0);
        testSnapping(2.5);
        testSnapping(5.0);
        testSnapping(9.0);

        testSnapping( 999.9);
        testSnapping(1000.0);
        testSnapping(1000.1);

        testSnapping(1999.9);
        testSnapping(2000.0);
        testSnapping(2000.1);

        testSnapping(4999.9);
        testSnapping(5000.0);
        testSnapping(5000.1);

        testSnapping( 9999.9);
        testSnapping(10000.0);
        testSnapping(10000.1);
    }

    private static void testSnapping(double d)
    {
        System.out.println("For "+d+" snapped up   "+
            Axes.computeSnappedUpValue(d));
        System.out.println("For "+d+" snapped down "+
            Axes.computeSnappedDownValue(d));
    }

}
