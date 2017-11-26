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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.DoubleFunction;

/**
 * Methods to create {@link LegendProvider} instances
 */
public class LegendProviders
{
    /**
     * Creates a default {@link LegendProvider} that always returns the given 
     * string
     * 
     * @param string The string
     * @return The {@link LegendProvider}
     */
    public static LegendProvider createDefault(
        final String string)
    {
        return new LegendProvider()
        {
            @Override
            public List<String> getLegend(
                DoubleFunction<? extends Number> function, 
                int screenX, int screenY, double worldX, double worldY)
            {
                return Arrays.asList(string);
            }
        };
    }

    /**
     * Creates a {@link LegendProvider} that returns the given title, 
     * and x and f(x)
     * 
     * @param title The title
     * @return The {@link LegendProvider}
     */
    public static LegendProvider createFunctionValuesLegendProvider(
        final String title)
    {
        return new LegendProvider()
        {
            @Override
            public List<String> getLegend(
                DoubleFunction<? extends Number> function,
                int screenX, int screenY, double worldX, double worldY)
            {
                double x = worldX;
                Number y = function.apply(x);
                String xValueString = "   x = "+defaultFormat(x);
                String yValueString = "f(x) = "+defaultFormat(y);
                return Arrays.asList(title, xValueString, yValueString);
            }
        };
    }
    
    /**
     * Creates a {@link LegendProvider} that returns x and f(x)
     * 
     * @return The {@link LegendProvider}
     */
    public static LegendProvider createFunctionValuesLegendProvider()
    {
        return new LegendProvider()
        {
            @Override
            public List<String> getLegend(
                DoubleFunction<? extends Number> function,
                int screenX, int screenY, double worldX, double worldY)
            {
                double x = worldX;
                Number y = function.apply(x);
                String xValueString = "   x = "+defaultFormat(x);
                String yValueString = "f(x) = "+defaultFormat(y);
                return Arrays.asList(xValueString, yValueString);
            }
        };
    }
    

    /**
     * Returns a default-formatted string for the given value
     * 
     * @param value The value
     * @return The string
     */
    private static String defaultFormat(Number value)
    {
        if (value == null)
        {
            return "?";
        }
        String s = String.format(Locale.ENGLISH, "%.4f", value.doubleValue());
        return String.format("%8s", s);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private LegendProviders()
    {
        // Private constructor to prevent instantiation
    }
}
