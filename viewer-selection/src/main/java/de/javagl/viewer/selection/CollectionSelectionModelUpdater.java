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
import java.util.Collection;

import de.javagl.selection.SelectionModel;

/**
 * Implementation of a {@link SelectionModelUpdater} that encapsulates a 
 * default selection behavior for multiple elements: When CTRL is held down, 
 * then the elements should be added to the selection. When SHIFT is held 
 * down, then the elements should be removed from the selection. Otherwise, 
 * the elements should be set as the selection.
 *
 * @param <T> The type of the elements
 */
class CollectionSelectionModelUpdater<T>
    implements SelectionModelUpdater<T>
{
    @Override
    public void updateSelectionModel(InputEvent e, 
        SelectionModel<T> selectionModel, Collection<T> affectedElements)
    {
        boolean addToSelection = e.isControlDown();
        boolean removeFromSelection = e.isShiftDown();
        if (addToSelection)
        {
            selectionModel.addToSelection(affectedElements);            
        }
        else if (removeFromSelection)
        {
            selectionModel.removeFromSelection(affectedElements);
        }
        else
        {
            selectionModel.setSelection(affectedElements);
        }
    }

}
