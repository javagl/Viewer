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

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;

/**
 * A class for creating a {@link ScatterChartMatrix} with test data.<br>
 * <br>
 * <b>Not part of the public API!</b>
 */
public class ScatterChartMatrices
{
    /**
     * Create the {@link ScatterChartMatrix} for the test.<br>
     * <br>
     * <b>Not part of the public API!</b>
     * 
     * @param fillPredicate The optional predicate indicating whether a
     * certain point should be filled
     * @return The {@link ScatterChartMatrix}
     */
    public static ScatterChartMatrix createTest(IntPredicate fillPredicate)
    {
        Object data[][] = createData();
        Object headers[] = createHeaders();
        ScatterChartMatrix scatterChartMatrix = new ScatterChartMatrix()
        {
            @Override
            public int getNumCharts()
            {
                return 4;
            }
            
            @Override
            public String getLabel(int row, int col)
            {
                return String.valueOf(headers[row]) + "/"
                    + String.valueOf(headers[col]);
            }
            
            /**
             * Returns the color for the species at the given index in the
             * test data
             * 
             * @param index The index
             * @return The color
             */
            private Paint colorFor(int index)
            {
                String species = String.valueOf(data[index][4]);
                if (species.equals("Iris setosa"))
                {
                    return Color.RED;
                }
                if (species.equals("Iris versicolor"))
                {
                    return Color.GREEN;
                }
                if (species.equals("Iris virginica"))
                {
                    return Color.BLUE;
                }
                return Color.MAGENTA;
            }
            
            @Override
            public ScatterChart getChart(int row, int col)
            {
                if (row <= col) 
                {
                    return null;
                }
                
                IntSupplier numPointsSupplier = () -> data.length;
                IntToDoubleFunction pointXFunction = 
                    index -> (Double)data[index][col];
                IntToDoubleFunction pointYFunction = 
                    index -> (Double)data[index][row];
                    
                BasicScatterChart scatterChart = new BasicScatterChart(
                    numPointsSupplier, pointXFunction, pointYFunction);

                scatterChart.setShape(TickShapes.square(4));
                scatterChart.setDrawPaintFunction(index -> colorFor(index));
                if (fillPredicate == null)
                {
                    scatterChart.setFillPaint(null);
                }
                else
                {
                    scatterChart.setFillPaintFunction(index ->
                    {
                        if (fillPredicate.test(index))
                        {
                            return colorFor(index);
                        }
                        return null;
                    });
                };
                return scatterChart;
            }
            
            @Override
            public Rectangle2D getRelativeCellBounds(int row, int col)
            {
                double top = 0.08;
                double bottom = 0.025;
                double left = 0.025;
                double right = 0.025;
                return new Rectangle2D.Double(left, top, 
                    1.0 - left - right, 1.0 - top - bottom);
            }
        };
        return scatterChartMatrix;
    }
    
    /**
     * Create unspecified data for this test
     * 
     * @return The data
     */
    private static Object[][] createData()
    {
        Object data[][] = 
        {
            { 5.1, 3.5, 1.4, 0.2, "Iris setosa" },
            { 4.9, 3.0, 1.4, 0.2, "Iris setosa" },
            { 4.7, 3.2, 1.3, 0.2, "Iris setosa" },
            { 4.6, 3.1, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.6, 1.4, 0.2, "Iris setosa" },
            { 5.4, 3.9, 1.7, 0.4, "Iris setosa" },
            { 4.6, 3.4, 1.4, 0.3, "Iris setosa" },
            { 5.0, 3.4, 1.5, 0.2, "Iris setosa" },
            { 4.4, 2.9, 1.4, 0.2, "Iris setosa" },
            { 4.9, 3.1, 1.5, 0.1, "Iris setosa" },
            { 5.4, 3.7, 1.5, 0.2, "Iris setosa" },
            { 4.8, 3.4, 1.6, 0.2, "Iris setosa" },
            { 4.8, 3.0, 1.4, 0.1, "Iris setosa" },
            { 4.3, 3.0, 1.1, 0.1, "Iris setosa" },
            { 5.8, 4.0, 1.2, 0.2, "Iris setosa" },
            { 5.7, 4.4, 1.5, 0.4, "Iris setosa" },
            { 5.4, 3.9, 1.3, 0.4, "Iris setosa" },
            { 5.1, 3.5, 1.4, 0.3, "Iris setosa" },
            { 5.7, 3.8, 1.7, 0.3, "Iris setosa" },
            { 5.1, 3.8, 1.5, 0.3, "Iris setosa" },
            { 5.4, 3.4, 1.7, 0.2, "Iris setosa" },
            { 5.1, 3.7, 1.5, 0.4, "Iris setosa" },
            { 4.6, 3.6, 1.0, 0.2, "Iris setosa" },
            { 5.1, 3.3, 1.7, 0.5, "Iris setosa" },
            { 4.8, 3.4, 1.9, 0.2, "Iris setosa" },
            { 5.0, 3.0, 1.6, 0.2, "Iris setosa" },
            { 5.0, 3.4, 1.6, 0.4, "Iris setosa" },
            { 5.2, 3.5, 1.5, 0.2, "Iris setosa" },
            { 5.2, 3.4, 1.4, 0.2, "Iris setosa" },
            { 4.7, 3.2, 1.6, 0.2, "Iris setosa" },
            { 4.8, 3.1, 1.6, 0.2, "Iris setosa" },
            { 5.4, 3.4, 1.5, 0.4, "Iris setosa" },
            { 5.2, 4.1, 1.5, 0.1, "Iris setosa" },
            { 5.5, 4.2, 1.4, 0.2, "Iris setosa" },
            { 4.9, 3.1, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.2, 1.2, 0.2, "Iris setosa" },
            { 5.5, 3.5, 1.3, 0.2, "Iris setosa" },
            { 4.9, 3.6, 1.4, 0.1, "Iris setosa" },
            { 4.4, 3.0, 1.3, 0.2, "Iris setosa" },
            { 5.1, 3.4, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.5, 1.3, 0.3, "Iris setosa" },
            { 4.5, 2.3, 1.3, 0.3, "Iris setosa" },
            { 4.4, 3.2, 1.3, 0.2, "Iris setosa" },
            { 5.0, 3.5, 1.6, 0.6, "Iris setosa" },
            { 5.1, 3.8, 1.9, 0.4, "Iris setosa" },
            { 4.8, 3.0, 1.4, 0.3, "Iris setosa" },
            { 5.1, 3.8, 1.6, 0.2, "Iris setosa" },
            { 4.6, 3.2, 1.4, 0.2, "Iris setosa" },
            { 5.3, 3.7, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.3, 1.4, 0.2, "Iris setosa" },
            { 7.0, 3.2, 4.7, 1.4, "Iris versicolor" },
            { 6.4, 3.2, 4.5, 1.5, "Iris versicolor" },
            { 6.9, 3.1, 4.9, 1.5, "Iris versicolor" },
            { 5.5, 2.3, 4.0, 1.3, "Iris versicolor" },
            { 6.5, 2.8, 4.6, 1.5, "Iris versicolor" },
            { 5.7, 2.8, 4.5, 1.3, "Iris versicolor" },
            { 6.3, 3.3, 4.7, 1.6, "Iris versicolor" },
            { 4.9, 2.4, 3.3, 1.0, "Iris versicolor" },
            { 6.6, 2.9, 4.6, 1.3, "Iris versicolor" },
            { 5.2, 2.7, 3.9, 1.4, "Iris versicolor" },
            { 5.0, 2.0, 3.5, 1.0, "Iris versicolor" },
            { 5.9, 3.0, 4.2, 1.5, "Iris versicolor" },
            { 6.0, 2.2, 4.0, 1.0, "Iris versicolor" },
            { 6.1, 2.9, 4.7, 1.4, "Iris versicolor" },
            { 5.6, 2.9, 3.6, 1.3, "Iris versicolor" },
            { 6.7, 3.1, 4.4, 1.4, "Iris versicolor" },
            { 5.6, 3.0, 4.5, 1.5, "Iris versicolor" },
            { 5.8, 2.7, 4.1, 1.0, "Iris versicolor" },
            { 6.2, 2.2, 4.5, 1.5, "Iris versicolor" },
            { 5.6, 2.5, 3.9, 1.1, "Iris versicolor" },
            { 5.9, 3.2, 4.8, 1.8, "Iris versicolor" },
            { 6.1, 2.8, 4.0, 1.3, "Iris versicolor" },
            { 6.3, 2.5, 4.9, 1.5, "Iris versicolor" },
            { 6.1, 2.8, 4.7, 1.2, "Iris versicolor" },
            { 6.4, 2.9, 4.3, 1.3, "Iris versicolor" },
            { 6.6, 3.0, 4.4, 1.4, "Iris versicolor" },
            { 6.8, 2.8, 4.8, 1.4, "Iris versicolor" },
            { 6.7, 3.0, 5.0, 1.7, "Iris versicolor" },
            { 6.0, 2.9, 4.5, 1.5, "Iris versicolor" },
            { 5.7, 2.6, 3.5, 1.0, "Iris versicolor" },
            { 5.5, 2.4, 3.8, 1.1, "Iris versicolor" },
            { 5.5, 2.4, 3.7, 1.0, "Iris versicolor" },
            { 5.8, 2.7, 3.9, 1.2, "Iris versicolor" },
            { 6.0, 2.7, 5.1, 1.6, "Iris versicolor" },
            { 5.4, 3.0, 4.5, 1.5, "Iris versicolor" },
            { 6.0, 3.4, 4.5, 1.6, "Iris versicolor" },
            { 6.7, 3.1, 4.7, 1.5, "Iris versicolor" },
            { 6.3, 2.3, 4.4, 1.3, "Iris versicolor" },
            { 5.6, 3.0, 4.1, 1.3, "Iris versicolor" },
            { 5.5, 2.5, 4.0, 1.3, "Iris versicolor" },
            { 5.5, 2.6, 4.4, 1.2, "Iris versicolor" },
            { 6.1, 3.0, 4.6, 1.4, "Iris versicolor" },
            { 5.8, 2.6, 4.0, 1.2, "Iris versicolor" },
            { 5.0, 2.3, 3.3, 1.0, "Iris versicolor" },
            { 5.6, 2.7, 4.2, 1.3, "Iris versicolor" },
            { 5.7, 3.0, 4.2, 1.2, "Iris versicolor" },
            { 5.7, 2.9, 4.2, 1.3, "Iris versicolor" },
            { 6.2, 2.9, 4.3, 1.3, "Iris versicolor" },
            { 5.1, 2.5, 3.0, 1.1, "Iris versicolor" },
            { 5.7, 2.8, 4.1, 1.3, "Iris versicolor" },
            { 6.3, 3.3, 6.0, 2.5, "Iris virginica" },
            { 5.8, 2.7, 5.1, 1.9, "Iris virginica" },
            { 7.1, 3.0, 5.9, 2.1, "Iris virginica" },
            { 6.3, 2.9, 5.6, 1.8, "Iris virginica" },
            { 6.5, 3.0, 5.8, 2.2, "Iris virginica" },
            { 7.6, 3.0, 6.6, 2.1, "Iris virginica" },
            { 4.9, 2.5, 4.5, 1.7, "Iris virginica" },
            { 7.3, 2.9, 6.3, 1.8, "Iris virginica" },
            { 6.7, 2.5, 5.8, 1.8, "Iris virginica" },
            { 7.2, 3.6, 6.1, 2.5, "Iris virginica" },
            { 6.5, 3.2, 5.1, 2.0, "Iris virginica" },
            { 6.4, 2.7, 5.3, 1.9, "Iris virginica" },
            { 6.8, 3.0, 5.5, 2.1, "Iris virginica" },
            { 5.7, 2.5, 5.0, 2.0, "Iris virginica" },
            { 5.8, 2.8, 5.1, 2.4, "Iris virginica" },
            { 6.4, 3.2, 5.3, 2.3, "Iris virginica" },
            { 6.5, 3.0, 5.5, 1.8, "Iris virginica" },
            { 7.7, 3.8, 6.7, 2.2, "Iris virginica" },
            { 7.7, 2.6, 6.9, 2.3, "Iris virginica" },
            { 6.0, 2.2, 5.0, 1.5, "Iris virginica" },
            { 6.9, 3.2, 5.7, 2.3, "Iris virginica" },
            { 5.6, 2.8, 4.9, 2.0, "Iris virginica" },
            { 7.7, 2.8, 6.7, 2.0, "Iris virginica" },
            { 6.3, 2.7, 4.9, 1.8, "Iris virginica" },
            { 6.7, 3.3, 5.7, 2.1, "Iris virginica" },
            { 7.2, 3.2, 6.0, 1.8, "Iris virginica" },
            { 6.2, 2.8, 4.8, 1.8, "Iris virginica" },
            { 6.1, 3.0, 4.9, 1.8, "Iris virginica" },
            { 6.4, 2.8, 5.6, 2.1, "Iris virginica" },
            { 7.2, 3.0, 5.8, 1.6, "Iris virginica" },
            { 7.4, 2.8, 6.1, 1.9, "Iris virginica" },
            { 7.9, 3.8, 6.4, 2.0, "Iris virginica" },
            { 6.4, 2.8, 5.6, 2.2, "Iris virginica" },
            { 6.3, 2.8, 5.1, 1.5, "Iris virginica" },
            { 6.1, 2.6, 5.6, 1.4, "Iris virginica" },
            { 7.7, 3.0, 6.1, 2.3, "Iris virginica" },
            { 6.3, 3.4, 5.6, 2.4, "Iris virginica" },
            { 6.4, 3.1, 5.5, 1.8, "Iris virginica" },
            { 6.0, 3.0, 4.8, 1.8, "Iris virginica" },
            { 6.9, 3.1, 5.4, 2.1, "Iris virginica" },
            { 6.7, 3.1, 5.6, 2.4, "Iris virginica" },
            { 6.9, 3.1, 5.1, 2.3, "Iris virginica" },
            { 5.8, 2.7, 5.1, 1.9, "Iris virginica" },
            { 6.8, 3.2, 5.9, 2.3, "Iris virginica" },
            { 6.7, 3.3, 5.7, 2.5, "Iris virginica" },
            { 6.7, 3.0, 5.2, 2.3, "Iris virginica" },
            { 6.3, 2.5, 5.0, 1.9, "Iris virginica" },
            { 6.5, 3.0, 5.2, 2.0, "Iris virginica" },
            { 6.2, 3.4, 5.4, 2.3, "Iris virginica" },
            { 5.9, 3.0, 5.1, 1.8, "Iris virginica" },
        };
        return data;
    }
    
    /**
     * Create unspecified headers for this test
     * 
     * @return The headers
     */
    private static Object[] createHeaders()
    {
        Object headers[] = 
        {
            "Sepal length",
            "Sepal width",
            "Petal length",
            "Petal width",
            "Species"        
        };
        return headers;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ScatterChartMatrices()
    {
        // Private constructor to prevent instantiation
    }

}
