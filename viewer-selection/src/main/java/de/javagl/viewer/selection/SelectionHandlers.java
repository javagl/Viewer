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

/**
 * Methods to create {@link SelectionHandler} instances
 */
public class SelectionHandlers
{
    /**
     * Creates a new {@link SelectionHandler} that responds to mouse 
     * clicks.<br>
     * <br>
     * When it is {@link SelectionHandler#connect connected} to a viewer and
     * a selection model, it will use the given {@link PointBasedSelector}
     * to determine the elements that are affected by the mouse input. 
     * 
     * @param <T> The type of the elements
     * 
     * @param pointBasedSelector The {@link PointBasedSelector}. 
     * May not be <code>null</code>.
     * @return The {@link SelectionHandler}
     */
    public static <T> SelectionHandler<T> createClick(
        PointBasedSelector<T> pointBasedSelector)
    {
        return new ClickSelectionHandler<T>(pointBasedSelector);
    }
    
    /**
     * Creates a new {@link SelectionHandler} that allows selecting elements
     * with a lasso.<br>
     * <br>
     * When it is {@link SelectionHandler#connect connected} to a viewer and
     * a selection model, it will use the given {@link ShapeBasedSelector}
     * to determine the elements that are affected by the mouse input. 
     * 
     * @param <T> The type of the elements
     * 
     * @param shapeBasedSelector The {@link ShapeBasedSelector}. 
     * May not be <code>null</code>.
     * @return The {@link SelectionHandler}
     */
    public static <T> SelectionHandler<T> createLasso(
        ShapeBasedSelector<T> shapeBasedSelector)
    {
        return new LassoSelectionHandler<T>(shapeBasedSelector);
    }
    
    /**
     * Creates a new {@link SelectionHandler} that allows selecting elements
     * with a rectangle.<br>
     * <br>
     * When it is {@link SelectionHandler#connect connected} to a viewer and
     * a selection model, it will use the given {@link ShapeBasedSelector}
     * to determine the elements that are affected by the mouse input. 
     * 
     * @param <T> The type of the elements
     * 
     * @param shapeBasedSelector The {@link ShapeBasedSelector}. 
     * May not be <code>null</code>.
     * @return The {@link SelectionHandler}
     */
    public static <T> SelectionHandler<T> createRectangle(
        ShapeBasedSelector<T> shapeBasedSelector)
    {
        return new RectangleSelectionHandler<T>(shapeBasedSelector);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private SelectionHandlers()
    {
        // Private constructor to prevent instantiation
    }
}
