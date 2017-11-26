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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import de.javagl.viewer.ObjectPainter;
import de.javagl.viewer.Painter;

/**
 * Implementation of a {@link Painter} that may paint a {@link CellMap}. <br>
 * <br>
 * It maintains several {@link ObjectPainter ObjectPainter&lt;Cell&gt;} 
 * instances, referred to as <i>cell painters</i>. The 
 * {@link #paint(Graphics2D, AffineTransform, double, double) paint} 
 * method simply iterates over all {@link Cell}s of the {@link CellMap}, 
 * and paints them using the cell painters.
 */
class CellMapPainter implements Painter
{
    /**
     * The {@link CellMap} that this painter is painting
     */
    private CellMap cellMap;
    
    /**
     * The map from layer indices to the list of painters
     * for the respective layer
     */
    private final Map<Integer, List<ObjectPainter<? super Cell>>> cellPainters;
    
    /**
     * Default constructor
     */
    CellMapPainter()
    {
        cellPainters = 
            new TreeMap<Integer, List<ObjectPainter<? super Cell>>>();
    }
    
    /**
     * Set the {@link CellMap} that this painter is painting
     * 
     * @param cellMap The {@link CellMap} that this painter is painting
     */
    void setCellMap(CellMap cellMap)
    {
    	this.cellMap = cellMap;
    }
    
    /**
     * Add the given cell painter in the given layer
     * 
     * @param cellPainter The cell painter
     * @param layer The layer
     */
    void addCellPainter(
        ObjectPainter<? super Cell> cellPainter, int layer)
    {
        List<ObjectPainter<? super Cell>> list = cellPainters.get(layer);
        if (list == null)
        {
            list = new ArrayList<ObjectPainter<? super Cell>>();
            cellPainters.put(layer, list);
        }
        list.add(cellPainter);
    }
    
    /**
     * Remove the given cell painter
     * 
     * @param cellPainter The cell painter
     */
    void removeCellPainter(ObjectPainter<? super Cell> cellPainter)
    {
        Set<Integer> toRemove = new LinkedHashSet<Integer>();
        for (Entry<Integer, List<ObjectPainter<? super Cell>>> entry : 
            cellPainters.entrySet())
        {
            List<ObjectPainter<? super Cell>> list = entry.getValue();
            boolean wasContained = list.remove(cellPainter);
            if (wasContained)
            {
                if (list.isEmpty())
                {
                    toRemove.add(entry.getKey());
                }
            }
        }
        cellPainters.keySet().removeAll(toRemove);
    }
    
    /**
     * Remove all cell painters
     */
    void clearCellPainters()
    {
        cellPainters.clear();
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
    	if (cellMap == null)
    	{
    		return;
    	}
        int sizeX = cellMap.getSizeX();
        int sizeY = cellMap.getSizeY();
        AffineTransform atCell = new AffineTransform();
        for (List<ObjectPainter<? super Cell>> list : cellPainters.values())
        {
            for (ObjectPainter<? super Cell> cellPainter : list)
            {
                for (int x=0; x<sizeX; x++)
                {
                    for (int y=0; y<sizeY; y++)
                    {
                        Cell cell = cellMap.getCell(x, y);
                        atCell.setTransform(worldToScreen);
                        cellPainter.paint(g, atCell, w, h, cell);
                    }
                }
            }
        }
    }
    
}