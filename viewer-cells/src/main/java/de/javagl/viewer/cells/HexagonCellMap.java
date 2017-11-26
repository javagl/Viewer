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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.javagl.hexagon.Hexagon;
import de.javagl.hexagon.HexagonGrid;
import de.javagl.hexagon.Hexagons;

/**
 * Implementation of a {@link CellMap} that is based on a {@link HexagonGrid}
 */
class HexagonCellMap implements CellMap
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
     * The cells
     */
    private final Cell cells[][];
    
    /**
     * Creates a new hexagon cell map with the specified size, based on
     * the given {@link HexagonGrid}
     * 
     * @param hexagonGrid The {@link HexagonGrid}
     * @param sizeX The size, in number of cells, in x-direction
     * @param sizeY The size, in number of cells, in y-direction
     */
    HexagonCellMap(HexagonGrid hexagonGrid, int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        
        Hexagon hexagon = hexagonGrid.getHexagon();
        Shape prototypeShape = Hexagons.createShape(hexagon);
        
        double innerSize = 1.0 / Math.sqrt(2) *
            Math.min(hexagon.getSizeX(), hexagon.getSizeY()); 
        AffineTransform contentTransformPart =  
            AffineTransform.getScaleInstance(innerSize, innerSize);
        contentTransformPart.concatenate(
            AffineTransform.getTranslateInstance(-0.5, -0.5));
        
        cells = new Cell[sizeX][sizeY];
        for (int x=0; x<sizeX; x++)
        {
            for (int y=0; y<sizeY; y++)
            {
                Point2D origin = hexagonGrid.getCenter(x, y, null);
                Cell cell = new HexagonCell(
                    x, y, prototypeShape, origin, contentTransformPart);
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
        // NOTE: Inefficient for larger maps. Optimize when necessary
        Point2D p = new Point2D.Double(worldX, worldY);
        for (int x=0; x<sizeX; x++)
        {
            for (int y=0; y<sizeY; y++)
            {
                Cell cell = cells[x][y];
                Shape shape = cell.getShape();
                Rectangle2D bounds = shape.getBounds2D();
                if (bounds.contains(p) && shape.contains(p))
                {
                    return cell;
                }
            }
        }
        return null;
    }
    
    

}