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

import de.javagl.hexagon.HexagonGrid;

/**
 * Methods to create {@link CellMap} instances
 */
public class CellMaps
{
    /**
     * Creates a new {@link CellMap} consisting of rectangle cells
     * 
     * @param sizeX The size, in number of cells, in x-direction
     * @param sizeY The size, in number of cells, in y-direction
     * @param cellSizeX The cell size, in x-direction
     * @param cellSizeY The cell size, in y-direction
     * @return The new {@link CellMap}
     */
    public static CellMap createRectangle(
        int sizeX, int sizeY, double cellSizeX, double cellSizeY)
    {
        return new RectangleCellMap(sizeX, sizeY, cellSizeX, cellSizeY);
    }
    
    /**
     * Creates a new {@link CellMap} consisting of hexagon cells,
     * as described by the given {@link HexagonGrid}
     * 
     * @param sizeX The size, in number of cells, in x-direction
     * @param sizeY The size, in number of cells, in y-direction
     * @param hexagonGrid The {@link HexagonGrid}
     * 
     * @return The new {@link CellMap}
     */
    public static CellMap createHexagon(
        int sizeX, int sizeY, HexagonGrid hexagonGrid)
    {
        return new HexagonCellMap(hexagonGrid, sizeX, sizeY);
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private CellMaps()
    {
        // Private constructor to prevent instantiation
    }
}
