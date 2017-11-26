/*
 * www.javagl.de - Cells
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
package de.javagl.viewer.cells;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * Interface describing one cell in a {@link CellMap}
 */
public interface Cell
{
    /**
     * Returns the x-coordinate of this cell
     * 
     * @return The x-coordinate of this cell
     */
    int getX();
    
    /**
     * Returns the y-coordinate of this cell
     * 
     * @return The y-coordinate of this cell
     */
    int getY();
    
    /**
     * Returns the shape of this cell, at its tiled position
     * 
     * @return The shape of this cell
     */
    Shape getShape();
    
    /**
     * Returns the x-coordinate of the origin of this cell.
     * 
     * @return The x-coordinate of the origin of this cell
     */
    double getOriginX();

    /**
     * Returns the y-coordinate of the origin of this cell.
     * 
     * @return The y-coordinate of the origin of this cell
     */
    double getOriginY();
    
    /**
     * Returns the x-coordinate of the center of this cell.
     * 
     * @return The x-coordinate of the center of this cell
     */
    double getCenterX();

    /**
     * Returns the y-coordinate of the center of this cell.
     * 
     * @return The y-coordinate of the center of this cell
     */
    double getCenterY();

    /**
     * Writes the transform for this cell into the given transform and 
     * returns it. This is usually just the translation transform that 
     * is computed from the {@link #getOriginX() origin}.<br>
     * <br>
     * If the given transform is <code>null</code>, then a
     * new transform will be created and returned.
     * 
     * @param at The cell transform 
     * @return The cell transform
     */
    AffineTransform getTransform(AffineTransform at);
    
    /**
     * Concatenates the given transform with the 
     * {@link #getTransform(AffineTransform) cell transform} for this cell.
     * If the given result transform is <code>null</code>, then a new 
     * transform will be created and returned.
     * 
     * @param at The transform to which the transform of this cell will be 
     * concatenated 
     * @param result The result transform
     * @return The result transform
     */
    AffineTransform concatenateWithTransform(
        AffineTransform at, AffineTransform result);

    /**
     * Writes the transform for the contents of this cell into the given 
     * transform and returns it. This describes the transform that may 
     * be applied to a unit square in order to obtain the rectangular 
     * area that is available for rendering contents inside the cell.<br>
     * <br> 
     * For rectangular cells, this usually is the same as the 
     * {@link #getTransform(AffineTransform) cell transform}, concatenated
     * with a scaling transform that reflects the cell size. For hexagon
     * cells, this will usually result in the largest rectangular area
     * that is available inside the hexagonal boundaries of the cell.<br>
     * <br>
     * If the given transform is <code>null</code>, then a
     * new transform will be created and returned.
     *      
     * @param at The cell content transform
     * @return The cell content transform
     */
    AffineTransform getContentTransform(AffineTransform at);
    
    /**
     * Concatenates the given transform with the 
     * {@link #getContentTransform(AffineTransform) cell content transform} 
     * for this cell and stores the result in the given result transform.
     * If the given result transform is <code>null</code>, then a new 
     * transform will be created and returned.
     * 
     * @param at The transform to which the content transform of this cell
     * will be concatenated 
     * @param result The result transform
     * @return The result transform
     */
    AffineTransform concatenateWithContentTransform(
        AffineTransform at, AffineTransform result);
    
    
}
