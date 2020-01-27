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
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import de.javagl.selection.SelectionModel;
import de.javagl.viewer.InputEventPredicates;
import de.javagl.viewer.Painter;
import de.javagl.viewer.Viewer;

/**
 * Implementation of the {@link SelectionHandler} interface for a lasso 
 * selection.<br>
 * <br>
 * It allows drawing a selection area that contains element that should 
 * added to (or set as) the selection.<br>
 * 
 * @param <T> The type of the elements
 */
class LassoSelectionHandler<T> implements SelectionHandler<T>
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
     * The current selection path
     */
    private Path2D selectionPath = null;

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
    private final Predicate<MouseEvent> selectPredicate = 
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
            selectionPath = new Path2D.Double(Path2D.WIND_EVEN_ODD);
            selectionPath.moveTo(e.getX(), e.getY());
            viewer.repaint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e)
        {
            if (selectionPath != null)
            {
                selectionPath.lineTo(e.getX(), e.getY());
                viewer.repaint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) 
        {
            if (selectionPath != null)
            {
                AffineTransform worldToScreen = viewer.getWorldToScreen();
                Collection<T> affectedElements = 
                    shapeBasedSelector.computeElementsForShape(
                        selectionPath, worldToScreen);
                selectionModelUpdater.updateSelectionModel(
                    e, selectionModel, affectedElements);
            }
            selectionPath = null;
            viewer.repaint();
        }
    };
    
    
    /**
     * Creates a new selection handler
     * 
     * @param shapeBasedSelector The {@link ShapeBasedSelector}
     */
    LassoSelectionHandler(ShapeBasedSelector<T> shapeBasedSelector)
    {
        this.shapeBasedSelector = Objects.requireNonNull(
            shapeBasedSelector, "The shapeBasedSelector may not be null");
        this.selectionShapePainter = new SelectionShapePainter(
            () -> selectionPath);
        this.selectionModelUpdater = new CollectionSelectionModelUpdater<T>();
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



