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
 * Interface for a simple scatter chart
 */
public interface ScatterChart
{
    /**
     * Returns the number of points
     * 
     * @return The number of points
     */
    int getNumPoints();
    
    /**
     * Returns the x-coordinate of the specified point 
     * 
     * @param index The index of the point
     * @return The x-coordinate of the point
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumPoints()}
     */
    double getPointX(int index);
    
    /**
     * Returns the y-coordinate of the specified point 
     * 
     * @param index The index of the point
     * @return The y-coordinate of the point
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumPoints()}
     */
    double getPointY(int index);

    /**
     * Returns the shape that should be used for the specified point.
     * If this is <code>null</code>, then no shape will be painted.
     *  
     * @param index The index of the point
     * @return The shape for the point
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumPoints()}
     */
    Shape getShape(int index);
    
    /**
     * Returns the paint that should be used for filling the specified point.
     * If this is <code>null</code>, then the shape will not be filled.
     *  
     * @param index The index of the point
     * @return The paint for the point
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumPoints()}
     */
    Paint getFillPaint(int index);

    /**
     * Returns the paint that should be used for drawing the specified point.
     * If this is <code>null</code>, then no shape outline will be painted.
     *  
     * @param index The index of the point
     * @return The paint for the point
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumPoints()}
     */
    Paint getDrawPaint(int index);
    
    /**
     * Returns the stroke that should be used for drawing the specified point.
     * If this is <code>null</code>, then no shape outline will be painted.
     *  
     * @param index The index of the point
     * @return The stroke for the point
     * @throws IndexOutOfBoundsException May be thrown if the index is 
     * smaller than 0 or not smaller than {@link #getNumPoints()}
     */
    Stroke getDrawStroke(int index);
    
    
}
