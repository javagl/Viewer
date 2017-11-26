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

/**
 * Interface for a simple bar chart
 */
public interface BarChart
{
    /**
     * Returns the number of bars
     * 
     * @return The number of bars
     */
    int getNumBars();
    
    /**
     * Returns the value of the specified bar 
     * 
     * @param index The index of the bar
     * @return The value
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumBars()}
     */
    double getValue(int index);

    /**
     * Returns the string showing the value for the bar with the
     * given index. If this is <code>null</code>, then no value 
     * string will be painted.
     * 
     * @param index The index of the bar
     * @return The value string for the bar
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumBars()}
     */
    String getValueString(int index);
    
    /**
     * Returns the label for the bar with the given index. If this
     * is <code>null</code>, then no label will be painted.
     * 
     * @param index The index of the bar
     * @return The label for the bar
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumBars()}
     */
    String getLabel(int index);
    
    /**
     * Returns the paint for the bar with the given index. If this is
     * <code>null</code>, then the respective bar will not be painted.
     * 
     * @param index The index of the bar
     * @return The paint
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumBars()}
     */
    Paint getBarPaint(int index);
}