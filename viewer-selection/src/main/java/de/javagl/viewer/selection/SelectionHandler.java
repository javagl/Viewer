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

import de.javagl.selection.SelectionModel;
import de.javagl.viewer.Viewer;

/**
 * Interface for classes that can handle selections in a viewer.
 *
 * @param <T> The type of the selected elements
 */
public interface SelectionHandler<T>
{
    /**
     * Connect this handler to the given viewer and 
     * {@link SelectionModel}. <br>
     * <br>
     * This will attach mouse- and mouse motion listeners to the given 
     * viewer so that the selection in the given selection model may be
     * modified with the mouse.<br>  
     * <br>
     * If both arguments are <code>null</code>, then this will have 
     * the same effect as calling {@link #disconnect()}.
     *  
     * @param viewer The {@link Viewer}
     * @param selectionModel The {@link SelectionModel}
     * @throws NullPointerException If exactly one of the given arguments 
     * is <code>null</code> 
     */
    void connect(Viewer viewer, SelectionModel<T> selectionModel);

    /**
     * Disconnect this handler from the viewer and 
     * {@link SelectionModel} that it was previously connected
     * to with {@link #connect(Viewer, SelectionModel)}
     */
    void disconnect();
}
