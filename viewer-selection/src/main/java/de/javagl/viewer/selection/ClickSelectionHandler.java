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

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;

import de.javagl.selection.SelectionModel;
import de.javagl.viewer.InputEventPredicates;
import de.javagl.viewer.Viewer;

/**
 * Implementation of the {@link SelectionHandler} interface for a click 
 * selection.<br>
 * <br>
 * It allows drawing a selection area that contains element that should 
 * added to (or set as) the selection.<br>
 * 
 * @param <T> The type of the elements
 */
class ClickSelectionHandler<T> implements SelectionHandler<T>
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
     * The {@link PointBasedSelector}
     */
    private final PointBasedSelector<T> pointBasedSelector;
    
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
        public void mouseClicked(MouseEvent e)
        {
            if (!selectPredicate.test(e))
            {
                return;
            }
            AffineTransform worldToScreen = viewer.getWorldToScreen();
            Collection<T> affectedElements = 
                pointBasedSelector.computeElementsForPoint(
                    e.getPoint(), worldToScreen);
            updateSelectionModel(
                e, selectionModel, affectedElements);
            viewer.repaint();
        }
    };
    
    /**
     * Update the {@link SelectionModel} using the given elements, based
     * on the given input event.
     * 
     * @param e The input event
     * @param selectionModel The {@link SelectionModel}
     * @param affectedElements The affected elements
     */
    void updateSelectionModel(InputEvent e, 
        SelectionModel<T> selectionModel, Collection<T> affectedElements)
    {
        if (!affectedElements.isEmpty())
        {
            for (T element : affectedElements)
            {
                if (e.isControlDown())
                {
                    if (selectionModel.isSelected(element))
                    {
                        selectionModel.removeFromSelection(element);
                    }
                    else
                    {
                        selectionModel.addToSelection(element);
                    }
                }
                else
                {
                    selectionModel.setSelection(
                        Collections.singleton(element));
                }
            }
        }
        else
        {
            if (!e.isControlDown())
            {
                selectionModel.clear();
            }
        }
    }
    
    /**
     * Creates a new selection handler
     * 
     * @param pointBasedSelector The {@link PointBasedSelector} 
     */
    public ClickSelectionHandler(PointBasedSelector<T> pointBasedSelector)
    {
        this.pointBasedSelector = Objects.requireNonNull(
            pointBasedSelector, "The pointBasedSelector may not be null");
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
    }
    
    @Override
    public void disconnect()
    {
        if (viewer != null)
        {
            viewer.removeMouseListener(mouseAdapter);
            viewer.removeMouseMotionListener(mouseAdapter);
            viewer = null;
        }
        selectionModel = null;
    }
    
}



