/*
 * www.javagl.de - Viewer
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
package de.javagl.viewer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Implementation of a {@link Painter} that allows setting an object
 * that should be painted with a delegate {@link ObjectPainter}
 *
 * @param <T> The type of the painted object
 */
public final class SimpleObjectPainter<T> implements Painter
{
    /**
     * The object to be painted
     */
    private T object;
    
    /**
     * The delegate {@link ObjectPainter}
     */
    private final ObjectPainter<? super T> objectPainter;
    
    /**
     * Creates a new simple object painter using the given delegate
     * 
     * @param objectPainter The delegate {@link ObjectPainter}
     */
    public SimpleObjectPainter(ObjectPainter<? super T> objectPainter)
    {
        this.objectPainter = objectPainter;
    }
    
    /**
     * Set the object that should be painted. This object may be 
     * <code>null</code>, usually to indicate that nothing should
     * be painted.
     * 
     * @param object The object
     */
    public void setObject(T object)
    {
        this.object = object;
    }
    
    /**
     * Returns the object that should be painted. This object may
     * be <code>null</code>.
     * 
     * @return The object that should be painted.
     */
    public T getObject()
    {
        return object;
    }

    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        objectPainter.paint(g, worldToScreen, w, h, object);
    }
}
