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
 * Implementation of a {@link Painter} that calls a delegate painter
 * after applying a transform to the given world-to-screen transform
 */
public class TransformedPainter implements Painter
{
    /**
     * The delegate {@link Painter}
     */
    private final Painter delegatePainter;
    
    /**
     * The transform that should be applied
     */
    private final AffineTransform transform;
    
    /**
     * The concatenation of the world-to-screen transform and the transform
     */
    private final AffineTransform delegateWorldToScreen;
    
    /**
     * Create a new transformed painter with the given delegate and transform.
     * If the given transform is <code>null</code>, then the identity 
     * transform will be used initially.
     * 
     * @param delegatePainter The delegate painter
     * @param transform The transform
     */
    public TransformedPainter(
        Painter delegatePainter, AffineTransform transform)
    {
        this.delegatePainter = Objects.requireNonNull(
            delegatePainter, "The delegatePainter may not be null");
        this.transform = new AffineTransform();
        if (transform != null)
        {
            this.transform.setTransform(transform);
        }
        this.delegateWorldToScreen = new AffineTransform(); 
    }
    
    /**
     * Set the transform of this painter 
     * 
     * @param transform The transform
     */
    public void setTransform(AffineTransform transform)
    {
        this.transform.setTransform(transform);
    }
    

    @Override
    public void paint(Graphics2D g, 
        AffineTransform worldToScreen, double w, double h)
    {
        delegateWorldToScreen.setTransform(worldToScreen);
        delegateWorldToScreen.concatenate(transform);
        delegatePainter.paint(g, delegateWorldToScreen, w, h);
    }
    
}
