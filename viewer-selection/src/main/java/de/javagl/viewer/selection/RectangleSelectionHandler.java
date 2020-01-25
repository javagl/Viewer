/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2020 Marco Hutter - http://www.javagl.de
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
package de.javagl.viewer.selection;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import de.javagl.selection.SelectionModel;
import de.javagl.viewer.InputEventPredicates;
import de.javagl.viewer.Painter;
import de.javagl.viewer.Viewer;

/**
 * Implementation of the {@link SelectionHandler} interface for a rectangle 
 * selection.<br>
 * <br>
 * It allows drawing a selection area that contains element that should 
 * added to (or set as) the selection.<br>
 * 
 * @param <T> The type of the elements
 */
class RectangleSelectionHandler<T> implements SelectionHandler<T>
{
    /**
     * The viewer this selection handler is working on
     */
    private Viewer viewer;
    
    /**
     * The {@link SelectionModel} that is controlled by this handler
     */
    private SelectionModel<T> selectionModel;

    /**
     * The {@link SelectionModelUpdater}
     */
    private final SelectionModelUpdater<T> selectionModelUpdater;
    
    /**
     * The starting point of the rectangle creation
     */
    private Point2D rectangleStart = null;
    
    /**
     * The current selection rectangle
     */
    private Rectangle2D selectionRectangle = null;

    /**
     * A {@link Painter} that paints the current selection shape
     */
    private final Painter selectionShapePainter;

    /**
     * The {@link ShapeBasedSelector}
     */
    private final ShapeBasedSelector<T> shapeBasedSelector;

    /**
     * The predicate that is checked for a mouse press or click event to 
     * determine whether the event should start or affect the selection 
     */
    private Predicate<MouseEvent> selectPredicate = 
        InputEventPredicates.button(1);
    
    /**
     * The MouseAdapter that encapsulates the selection functionality
     */
    private final MouseAdapter mouseAdapter = new MouseAdapter()
    {
        @Override
        public void mousePressed(MouseEvent e)
        {
            if (!selectPredicate.test(e))
            {
                return;
            }
            rectangleStart = new Point2D.Double(e.getX(), e.getY());
            selectionRectangle = new Rectangle2D.Double(
                e.getX(), e.getY(), 0.0, 0.0);
            viewer.repaint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e)
        {
            if (selectionRectangle != null)
            {
                double minX = Math.min(rectangleStart.getX(), e.getX());
                double minY = Math.min(rectangleStart.getY(), e.getY());
                double maxX = Math.max(rectangleStart.getX(), e.getX());
                double maxY = Math.max(rectangleStart.getY(), e.getY());
                selectionRectangle.setRect(
                    minX, minY, maxX - minX, maxY - minY);
                viewer.repaint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) 
        {
            if (selectionRectangle != null)
            {
                AffineTransform worldToScreen = viewer.getWorldToScreen();
                Collection<T> affectedElements = 
                    shapeBasedSelector.computeElementsForShape(
                        selectionRectangle, worldToScreen);
                selectionModelUpdater.updateSelectionModel(
                    e, selectionModel, affectedElements);
            }
            selectionRectangle = null;
            viewer.repaint();
        }
    };
    
    
    /**
     * Creates a new selection handler
     * 
     * @param shapeBasedSelector The {@link ShapeBasedSelector}
     */
    RectangleSelectionHandler(ShapeBasedSelector<T> shapeBasedSelector)
    {
        this.shapeBasedSelector = Objects.requireNonNull(
            shapeBasedSelector, "The shapeBasedSelector may not be null");
        this.selectionShapePainter = new SelectionShapePainter(
            () -> selectionRectangle);
        this.selectionModelUpdater = new SelectionModelUpdater<T>();
    }

    
    @Override
    public void connect(
        Viewer viewer, SelectionModel<T> selectionModel)
    {
        if (viewer == null && selectionModel == null)
        {
            disconnect();
            return;
        }
        this.viewer = Objects.requireNonNull(
            viewer, "The viewer may not be null");
        this.selectionModel = Objects.requireNonNull(
            selectionModel, "The selectionModel may not be null");
        
        viewer.addMouseListener(mouseAdapter);
        viewer.addMouseMotionListener(mouseAdapter);
        viewer.addPainter(selectionShapePainter);        
    }
    
    @Override
    public void disconnect()
    {
        if (viewer != null)
        {
            viewer.removeMouseListener(mouseAdapter);
            viewer.removeMouseMotionListener(mouseAdapter);
            viewer.removePainter(selectionShapePainter);
            viewer = null;
        }
        selectionModel = null;
    }
    
}



