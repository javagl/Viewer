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


/**
 * An interface that describes the visual representation of a map
 * consisting of {@link Cell}s. These cells may, for example, be 
 * hexagons or rectangles.
 */
public interface CellMap
{
    /**
     * The size of the map, in number of cells, in x-direction
     * 
     * @return The size of the map in x-direction
     */
	int getSizeX();

    /**
     * The size of the map, in number of cells, in y-direction
     * 
     * @return The size of the map in y-direction
     */
	int getSizeY();

    /**
     * Returns the {@link Cell} with the given coordinates
     * 
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @return The cell
     * @throws IndexOutOfBoundsException If one of the given coordinates
     * is negative or not smaller than {@link #getSizeX()} 
     * and {@link #getSizeY()}, respectively.
     */
    Cell getCell(int x, int y);
    
    /**
     * Returns the {@link Cell} at the given world coordinates, or 
     * <code>null</code> if there is no cell.
     * 
     * @param worldX The x-world coordinate
     * @param worldY The y-world coordinate
     * @return The {@link Cell} at the given world coordinates, or 
     * <code>null</code>
     */
    Cell getCellAt(double worldX, double worldY);
	
}