/*
 * www.javagl.de - Viewer - Glyphs
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
import java.util.function.BiConsumer;

/**
 * Implementation of an {@link ObjectPainter} that delegates the painting
 * to another {@link ObjectPainter}, and allows setting an additional
 * transform. 
 * 
 * @param <T> The type of the painted objects
 */
public final class TransformedObjectPainter<T> implements ObjectPainter<T>
{
    /**
     * The delegate {@link ObjectPainter}
     */
    private final ObjectPainter<T> delegatePainter;

    /**
     * The transform that should be applied
     */
    private final AffineTransform transform;
    
    /**
     * The concatenation of the world-to-screen transform and the transform
     */
    private final AffineTransform delegateWorldToScreen;
    
    /**
     * The consumer that will receive the object to be painted, and the
     * transform that will be applied to the delegate, and that may 
     * update the transform based on the object
     */
    private final BiConsumer<T, AffineTransform> transformUpdate;
    
    /**
     * Creates a new transformed object painter
     *  
     * @param delegatePainter The delegate
     */
    public TransformedObjectPainter(ObjectPainter<T> delegatePainter)
    {
        this(delegatePainter, null);
    }
    
    /**
     * Creates a new transformed object painter
     *  
     * @param delegatePainter The delegate
     * @param transformUpdate The optional transform update
     */
    public TransformedObjectPainter(
        ObjectPainter<T> delegatePainter, 
        BiConsumer<T, AffineTransform> transformUpdate)
    {
        this.delegatePainter = Objects.requireNonNull(
            delegatePainter, "The delegate may not be null");
        this.transformUpdate = transformUpdate;
        this.transform = new AffineTransform();
        this.delegateWorldToScreen = new AffineTransform();
    }
    
    /**
     * Set the transform that this painter should apply to be the same
     * as the given one.<br>
     * <br>
     * Note: If the transform update function that was given in the
     * constructor was not <code>null</code>, then this transform will
     * be ignored. 
     * 
     * @param transform The transform
     */
    public void setTransform(AffineTransform transform)
    {
        this.transform.setTransform(transform);
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, T object)
    {
        delegateWorldToScreen.setTransform(worldToScreen);
        if (transformUpdate != null)
        {
            transformUpdate.accept(object, transform);
        }
        delegateWorldToScreen.concatenate(transform);
        delegatePainter.paint(g, delegateWorldToScreen, w, h, object);
    }

}
