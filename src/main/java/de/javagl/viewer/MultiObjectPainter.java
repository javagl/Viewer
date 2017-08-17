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
import java.util.Objects;

/**
 * Implementation of an {@link ObjectPainter} that paints multiple objects
 * using a delegate
 *  
 * @param <T> The type of the painted objects 
 */
public final class MultiObjectPainter<T> 
    implements ObjectPainter<Iterable<? extends T>>
{
    /**
     * The delegate painter
     */
    private final ObjectPainter<T> delegate;
    
    /**
     * The world-to-screen transform that will be passed to the delegates
     */
    private final AffineTransform delegateWorldToScreen;
    
    /**
     * Create a new painter that paints objects using the given delegate
     * 
     * @param delegate The delegate painter
     */
    public MultiObjectPainter(ObjectPainter<T> delegate)
    {
        this.delegate = Objects.requireNonNull(
            delegate, "The delegate may not be null");
        this.delegateWorldToScreen = new AffineTransform(); 
    }

    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, Iterable<? extends T> objects)
    {
        for (T object : objects)
        {
            delegateWorldToScreen.setTransform(worldToScreen);
            delegate.paint(g, delegateWorldToScreen, w, h, object);
        }
    }
}
