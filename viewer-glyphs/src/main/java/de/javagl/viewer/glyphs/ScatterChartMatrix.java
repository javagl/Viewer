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


/**
 * Interface for a simple scatter chart matrix
 */
public interface ScatterChartMatrix
{
    /**
     * Returns the number of {@link ScatterChart}s in this matrix
     * 
     * @return The number of charts
     */
    int getNumCharts();
    
    /**
     * Returns the {@link ScatterChart} with the specified coordinates.
     * 
     * @param row The row index
     * @param col The column index
     * @return The {@link ScatterChart}
     * @throws IndexOutOfBoundsException May be thrown if any index is 
     * smaller than 0 or not smaller than {@link #getNumCharts()}
     */
    ScatterChart getChart(int row, int col);
    
    /**
     * Returns the label that should be displayed for the specified 
     * scatter chart. If this is <code>null</code>, then no label will
     * be displayed.
     * 
     * @param row The row index
     * @param col The column index
     * @return The {@link ScatterChart}
     * @throws IndexOutOfBoundsException May be thrown if any index is 
     * smaller than 0 or not smaller than {@link #getNumCharts()}
     */
    String getLabel(int row, int col);
}
