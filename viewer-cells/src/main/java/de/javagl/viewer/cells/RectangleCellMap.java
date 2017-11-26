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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Implementation of a {@link CellMap} that is based on rectangles
 */
class RectangleCellMap implements CellMap
{
    /**
     * The size of the cell map, in number of cells, in x-direction
     */
    private final int sizeX;

    /**
     * The size of the cell map, in number of cells, in y-direction
     */
    private final int sizeY;
    
    /**
     * The size of one cell in x-direction
     */
    private final double cellSizeX;

    /**
     * The size of one cell in y-direction
     */
    private final double cellSizeY;
    
    /**
     * The cells
     */
    private final Cell cells[][];
    
    /**
     * Creates a new rectangle cell map
     * 
     * @param sizeX The size, in number of cells, in x-direction
     * @param sizeY The size, in number of cells, in y-direction
     * @param cellSizeX The cell size, in x-direction
     * @param cellSizeY The cell size, in y-direction
     */
    RectangleCellMap(int sizeX, int sizeY, double cellSizeX, double cellSizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.cellSizeX = cellSizeX;
        this.cellSizeY = cellSizeY;
        
        Shape prototypeShape = 
            new Rectangle2D.Double(0, 0, cellSizeX, cellSizeY);
        
        cells = new Cell[sizeX][sizeY];
        for (int x=0; x<sizeX; x++)
        {
            for (int y=0; y<sizeY; y++)
            {
                Point2D origin = new Point2D.Double(x*cellSizeX, y*cellSizeY);
                Cell cell = new RectangleCell(
                    x, y, prototypeShape, origin, cellSizeX, cellSizeY);
                cells[x][y] = cell;
            }
        }
    }

    @Override
    public int getSizeX()
    {
        return sizeX;
    }

    @Override
    public int getSizeY()
    {
        return sizeY;
    }

    @Override
    public Cell getCell(int x, int y)
    {
        return cells[x][y];
    }
    
    @Override
    public Cell getCellAt(double worldX, double worldY)
    {
        if (worldX < 0 || worldY < 0)
        {
            return null;
        }
        int x = (int)(worldX / cellSizeX);
        int y = (int)(worldY / cellSizeY);
        if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
        {
            return cells[x][y];
        }
        return null;
    }
    

}
