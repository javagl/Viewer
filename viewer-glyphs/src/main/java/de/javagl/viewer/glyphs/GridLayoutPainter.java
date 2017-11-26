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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import de.javagl.geom.AffineTransforms;
import de.javagl.viewer.Painter;

/**
 * Implementation of a {@link Painter} that lays out other painters in a grid.
 * The {@link #setDelegate(int, int, Painter) delegate painters} will be laid
 * out in a way that each of them is responsible for one grid cell. 
 */
public final class GridLayoutPainter implements Painter
{
    /**
     * An affine transform, used internally for painting
     */
    private static final AffineTransform TEMP_TRANSFORM = new AffineTransform();
    
    /**
     * The number of cells in x-direction
     */
    private final int cellsX;
    
    /**
     * The number of cells in y-direction
     */
    private final int cellsY;

    /**
     * The size of the border for the cells, at x0
     */
    private double relativeCellBorderSizeX0;
    
    /**
     * The size of the border for the cells, at y0
     */
    private double relativeCellBorderSizeY0;
    
    /**
     * The size of the border for the cells, at x1
     */
    private double relativeCellBorderSizeX1;
    
    /**
     * The size of the border for the cells, at y1
     */
    private double relativeCellBorderSizeY1;

    /**
     * The delegates
     */
    private final Painter[][] delegates;
    
    /**
     * The concatenation of the world-to-screen transform and the transform
     */
    private final AffineTransform delegateWorldToScreen;

    /**
     * Creates a new instance
     * 
     * @param cellsX The number of cells in x-direction
     * @param cellsY The number of cells in y-direction
     * @throws IllegalArgumentException If any number of cells is negative
     */
    public GridLayoutPainter(int cellsX, int cellsY)
    {
        this(cellsX, cellsY, 0.0, 0.0);
    }
    
    /**
     * Creates a new instance with the given border sizes for the cells.
     * The border size is relative, i.e. referring to the total size of
     * the cell. Thus, it will usually be a positive value between 0.0
     * and 1.0. 
     * 
     * @param cellsX The number of cells in x-direction
     * @param cellsY The number of cells in y-direction
     * @param relativeCellBorderSizeX The border size of each cell in 
     * x-direction
     * @param relativeCellBorderSizeY The border size of each cell in 
     * y-direction
     * @throws IllegalArgumentException If any number of cells is negative
     */
    public GridLayoutPainter(int cellsX, int cellsY, 
        double relativeCellBorderSizeX, double relativeCellBorderSizeY)
    {
        if (cellsX <= 0) 
        {
            throw new IllegalArgumentException(
                "The value of cellsX must be positive, but is " + cellsX);
        }
        if (cellsY <= 0) 
        {
            throw new IllegalArgumentException(
                "The value of cellsY must be positive, but is " + cellsY);
        }
        this.cellsX = cellsX;
        this.cellsY = cellsY;
        this.relativeCellBorderSizeX0 = relativeCellBorderSizeX;
        this.relativeCellBorderSizeY0 = relativeCellBorderSizeY;
        this.relativeCellBorderSizeX1 = relativeCellBorderSizeX;
        this.relativeCellBorderSizeY1 = relativeCellBorderSizeY;
        this.delegates = new Painter[cellsX][cellsY];
        this.delegateWorldToScreen = new AffineTransform();
    }
    
    /**
     * Returns the number of cells in x-direction
     * 
     * @return The number of cells in x-direction
     */
    public int getCellsX()
    {
        return cellsX;
    }
    
    /**
     * Returns the number of cells in y-direction
     * 
     * @return The number of cells in y-direction
     */
    public int getCellsY()
    {
        return cellsY;
    }
    
    
    /**
     * Set the painter for the cell with the given coordinates
     * 
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @param delegate The delegate
     * @throws IndexOutOfBoundsException If the given indices are invalid
     */
    public void setDelegate(int x, int y, Painter delegate)
    {
        delegates[x][y] = delegate;
    }

    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        double stepSizeX = 1.0 / cellsX;
        double stepSizeY = 1.0 / cellsY;
        for (int y = 0; y < cellsY; y++)
        {
            for (int x = 0; x < cellsX; x++)
            {
                Painter delegate = delegates[x][y];
                if (delegate == null)
                {
                    continue;
                }
                double x0 = x * stepSizeX;
                double y0 = y * stepSizeY;
                double x1 = x0 + stepSizeX;
                double y1 = y0 + stepSizeY;
                x0 += relativeCellBorderSizeX0 * stepSizeX;
                y0 += relativeCellBorderSizeY0 * stepSizeY;
                x1 -= relativeCellBorderSizeX1 * stepSizeX;
                y1 -= relativeCellBorderSizeY1 * stepSizeY;
                AffineTransforms.getScaleInstance(
                    x0, y0, x1, y1, TEMP_TRANSFORM);
                delegateWorldToScreen.setTransform(worldToScreen);
                delegateWorldToScreen.concatenate(TEMP_TRANSFORM);
                delegate.paint(g, delegateWorldToScreen, w, h);
            }
        }
    }
}
