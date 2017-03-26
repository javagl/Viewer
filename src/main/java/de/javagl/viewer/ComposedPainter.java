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
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of a {@link Painter} that composes other painters
 */
class ComposedPainter implements Painter
{
    /**
     * The delegate {@link Painter}s
     */
    private final List<Painter> delegates;
    
    /**
     * The world-to-screen transform that will be passed to the delegates
     */
    private final AffineTransform delegateWorldToScreen;
    
    /**
     * Create a new painter that is a composition of the given painters
     * 
     * @param delegates The delegate painters
     */
    ComposedPainter(Painter ... delegates)
    {
        this.delegates = Arrays.asList(delegates);
        this.delegateWorldToScreen = new AffineTransform(); 
    }

    @Override
    public void paint(Graphics2D g, 
        AffineTransform worldToScreen, double w, double h)
    {
        for (Painter delegate : delegates)
        {
            if (delegate != null)
            {
                delegateWorldToScreen.setTransform(worldToScreen);
                delegate.paint(g, delegateWorldToScreen, w, h);
            }
        }
    }
    
}
