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

import java.awt.Color;
import java.util.function.Function;

import de.javagl.viewer.ObjectPainter;

/**
 * Methods to create {@link ObjectPainter} instances that can paint
 * {@link Cell}s.
 */
public class CellPainters
{
    /**
     * Create a default {@link ObjectPainter} that paints the a black outline
     * of a {@link Cell}, and its coordinates as the label.
     * 
     * @return The cell painter
     */
    public static ObjectPainter<Cell> createDefault()
    {
        BasicCellPainter basicCellPainter = new BasicCellPainter();
        basicCellPainter.setDrawPaint(Color.BLACK);
        basicCellPainter.setLabelPaint(Color.BLACK);
        basicCellPainter.setLabelFunction(coordinateLabelFunction());
        return basicCellPainter;
    }
    
    /**
     * Creates a function that returns the coordinates of a cell as a string
     * 
     * @return The function
     */
    private static Function<Cell, String> coordinateLabelFunction()
    {
        return new Function<Cell, String>()
        {
            @Override
            public String apply(Cell cell)
            {
                return cell.getX()+","+cell.getY();
            }
        };
    }

    /**
     * Private constructor to prevent instantiation
     */
    private CellPainters()
    {
        // Private constructor to prevent instantiation
    }
}