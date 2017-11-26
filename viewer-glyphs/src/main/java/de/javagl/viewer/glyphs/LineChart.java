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
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Interface for a simple line chart
 */
public interface LineChart
{
    /**
     * Returns the number of points in this line chart
     * 
     * @return The number of points
     */
    int getNumPoints();
    
    /**
     * Returns the value at the given index
     * 
     * @param index The index
     * @return The value at the given index
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumPoints()}
     */
    double getValue(int index);
    
    /**
     * Returns the paint (color) that should be used for the line.
     * If this is <code>null</code>, then the line will not be
     * painted.
     * 
     * @return The paint
     */
    Paint getPaint();
    
    /**
     * Returns the stroke that should be used for the line. If this 
     * is <code>null</code>, then the line will not be painted.
     * 
     * @return The stroke
     */
    Stroke getStroke();
    
    /**
     * Returns the shape that should be painted as the tick marks.
     * If this is <code>null</code>, then no tick shape will be
     * painted. 
     * 
     * @return The tick shape
     */
    Shape getTickShape();
    
}

