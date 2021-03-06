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
 * Package-private interface for classes that can update a selection model
 *
 * @param <T> The type of the selected elements
 */
interface SelectionModelUpdater<T>
{
    /**
     * Update the {@link SelectionModel} using the given elements, based
     * on the given input event.
     * 
     * @param e The input event
     * @param selectionModel The {@link SelectionModel}
     * @param affectedElements The affected elements
     */
    void updateSelectionModel(InputEvent e, 
        SelectionModel<T> selectionModel, Collection<T> affectedElements);
}
