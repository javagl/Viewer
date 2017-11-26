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

import de.javagl.viewer.MouseControls;
import de.javagl.viewer.ObjectPainter;
import de.javagl.viewer.Viewer;

/**
 * A panel that paints a {@link CellMap}. Hence the name.<br>
 * <br>
 * It allows adding and removing {@link ObjectPainter ObjectPainter&lt;Cell&gt;} 
 * instances, referred to as <i>cell painters</i>. These cell painters have 
 * an associated <i>layer</i>, to determine the order in which the cell 
 * painting operations take place.<br>
 * <br>
 * The {@link BasicCellPainter} class offers a flexible base implementation
 * of a cell painter, that allows creating own cell painters with minimal
 * implementation effort.
 */
public class CellMapPanel extends Viewer
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 345530708914201681L;

    /**
     * The {@link CellMap} that is painted 
     */
    private CellMap cellMap;
    
    /**
     * The {@link CellMapPainter}
     */
    private CellMapPainter cellMapPainter;
    
    /**
     * Creates a new cell map panel.<br>
     * <br>
     * This cell map panel that does not paint anything, unless a 
     * {@link #addCellPainter(ObjectPainter, int) cell painter} 
     * is added.<br>
     * <br>
     * It will have {@link MouseControls#createDefault(Viewer) default 
     * mouse controls} where rotation and non-uniform scaling are
     * allowed
     */
    public CellMapPanel()
    {
        setMouseControl(MouseControls.createDefault(this, true, true));
        setCellMapPainter(new CellMapPainter());
    }
    
    /**
     * Set the {@link CellMapPainter} that will be used
     * 
     * @param cellMapPainter The {@link CellMapPainter}
     */
    private final void setCellMapPainter(CellMapPainter cellMapPainter)
    {
        if (this.cellMapPainter != null)
        {
            removePainter(this.cellMapPainter);
        }
        this.cellMapPainter = cellMapPainter;
        this.cellMapPainter.setCellMap(this.cellMap);
        addPainter(cellMapPainter);
    }

    /**
     * Add the given cell painter. The {@link ObjectPainter#paint} method
     * of the given painter will be called when this panel is repainted,
     * passing in the {@link Cell}s of the current {@link CellMap} as
     * the last argument (if the current {@link CellMap} is not 
     * <code>null</code>)
     * 
     * @param cellPainter The cell painter to add
     * @param layer The layer
     */
    public final void addCellPainter(
        ObjectPainter<? super Cell> cellPainter, int layer)
    {
        cellMapPainter.addCellPainter(cellPainter, layer);
        repaint();
    }

    /**
     * Remove the given cell painter
     * 
     * @param cellPainter The cell painter to remove
     */
    public final void removeCellPainter(
        ObjectPainter<? super Cell> cellPainter)
    {
        cellMapPainter.removeCellPainter(cellPainter);
        repaint();
    }

    /**
     * Remove all cell painters
     */
    public final void clearCellPainters()
    {
        cellMapPainter.clearCellPainters();
        repaint();
    }
    
    /**
     * Set the {@link CellMap} that will be used for painting. <br>
     * <br>
     * If the given {@link CellMap} is <code>null</code>, then this
     * panel will not paint anything.<br>
     * <br>
     * Otherwise, when this panel is repainted, the {@link Cell}s of the 
     * given {@link CellMap} will be passed to the {@link ObjectPainter#paint} 
     * method of all {@link #addCellPainter(ObjectPainter, int) cell painters}
     * that have been added to this panel.
     * 
     * @param cellMap The {@link CellMap}
     */
    public final void setCellMap(CellMap cellMap)
    {
        this.cellMap = cellMap;
        this.cellMapPainter.setCellMap(this.cellMap);
        repaint();
    }
    
    /**
     * Returns the {@link CellMap} that is used for painting. This may
     * be <code>null</code>.
     * 
     * @return The {@link CellMap}
     */
    public final CellMap getCellMap()
    {
        return cellMap;
    }
    
    /**
     * Returns the {@link Cell} at the given screen coordinates. 
     * Returns <code>null</code> if no 
     * {@link #setCellMap(CellMap) cell map has been set}, or 
     * if there is no cell at the specified position
     * 
     * @param sx The x-coordinate on the screen
     * @param sy The y-coordinate on the screen
     * @return The {@link Cell}, or <code>null</code>
     */
    public final Cell getCellAtScreen(int sx, int sy)
    {
        if (cellMap == null)
        {
            return null;
        }
        Point2D point = new Point2D.Double(sx,sy);
        getScreenToWorld().transform(point, point);
        return cellMap.getCellAt(point.getX(), point.getY());
    }
    
    /**
     * Make sure that this panel displays the whole {@link CellMap}
     */
    public final void autoFit()
    {
        Rectangle2D totalBounds = null;
        if (cellMap != null)
        {
            int sizeX = cellMap.getSizeX();
            int sizeY = cellMap.getSizeY();
            for (int x=0; x<sizeX; x++)
            {
                for (int y=0; y<sizeY; y++)
                {
                    Cell cell = cellMap.getCell(x, y);
                    Shape shape = cell.getShape();
                    Rectangle2D bounds = shape.getBounds2D();
                    if (totalBounds == null)
                    {
                        totalBounds = bounds;
                    }
                    else
                    {
                        Rectangle2D.union(totalBounds, bounds, totalBounds);
                    }
                }
            }
        }
        if (totalBounds == null)
        {
            totalBounds = new Rectangle2D.Double(0,0,1,1);
        }
        else
        {
            // Add a small margin
            double x = totalBounds.getX() - 0.1 * totalBounds.getWidth();
            double y = totalBounds.getY() - 0.1 * totalBounds.getHeight();
            double w = totalBounds.getWidth() * 1.2; 
            double h = totalBounds.getHeight() * 1.2; 
            totalBounds.setRect(x, y, w, h);
        }
        setDisplayedWorldArea(totalBounds);
    }
    
    
}