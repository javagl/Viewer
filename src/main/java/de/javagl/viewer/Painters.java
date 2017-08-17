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
import java.awt.geom.Rectangle2D;

import de.javagl.geom.AffineTransforms;

/**
 * Methods related to {@link Painter}s and {@link ObjectPainter}s
 */
public class Painters
{
    /**
     * Creates a new {@link Painter} that only paints the given object
     * with the given {@link ObjectPainter}
     * 
     * @param <T> The type of the painted object
     * @param objectPainter The {@link ObjectPainter}
     * @param object The painted object
     * @return The new {@link Painter}
     */
    public static <T> Painter create(
        final ObjectPainter<T> objectPainter, final T object)
    {
        return new Painter()
        {
            @Override
            public void paint(Graphics2D g, AffineTransform worldToScreen, 
                double w, double h)
            {
                objectPainter.paint(g, worldToScreen, w, h, object);
            }
        };
    }
    
    /**
     * Returns a new painter that is the composition of the given painters.
     * 
     * @param delegates The delegates
     * @return The composed painter
     */
    public static Painter compose(Painter ... delegates)
    {
        return new ComposedPainter(delegates);
    }
    
    
    /**
     * Create a {@link Painter} that calls the given delegate with a 
     * world-to-screen transform that was concatenated with the
     * given transform
     * 
     * @param delegate The delegate
     * @param transform The transform
     * @return The new painter
     */
    public static Painter createTransformed(
        Painter delegate, AffineTransform transform)
    {
        return new TransformedPainter(delegate, transform);
    }
    
    /**
     * Create a {@link Painter} that calls the given delegate with a 
     * world-to-screen transform that causes the contents to be 
     * flipped vertically
     * 
     * @param delegate The delegate
     * @return The new painter
     */
    public static Painter createFlippedVertically(Painter delegate)
    {
        return createTransformed(
            delegate, AffineTransform.getScaleInstance(1, -1));
    }
    
    /**
     * Create a {@link Painter} that calls the given delegate with a 
     * world-to-screen transform that causes the specified rectangle
     * to fill the unit square.
     * 
     * @param delegate The delegate
     * @param rectangle The rectangle
     * @return The new painter
     */
    public static Painter createNormalized(Painter delegate, 
        Rectangle2D rectangle)
    {
        return createNormalized(delegate, 
            rectangle.getMinX(), 
            rectangle.getMinY(), 
            rectangle.getMaxX(), 
            rectangle.getMaxY());
    }
    
    /**
     * Create a {@link Painter} that calls the given delegate with a 
     * world-to-screen transform that causes the specified rectangle
     * to fill the unit square.
     * 
     * @param delegate The delegate
     * @param minX The minimum x-coordinate
     * @param minY The minimum y-coordinate
     * @param maxX The maximum x-coordinate 
     * @param maxY The maximum y-coordinate 
     * @return The new painter
     */
    public static Painter createNormalized(Painter delegate, 
        double minX, double minY, double maxX, double maxY)
    {
        AffineTransform transform = 
            AffineTransforms.getScaleInstance(minX, minY, maxX, maxY, null);
        AffineTransforms.invert(transform, transform);
        return createTransformed(delegate, transform);
    }

    
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Painters()
    {
        // Private constructor to prevent instantiation
    }
}
