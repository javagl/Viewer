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
package de.javagl.viewer.glyphs;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Methods to create simple shape instances.<br>
 * <br>
 * Each of these methods will create a <i>new</i> shape when it is called.
 * For performance reasons, these shapes should be cached and re-used when
 * possible.
 */
public class TickShapes
{
    /**
     * Creates an x-shape, centered at the origin, with the given total size
     * 
     * @param size The size
     * @return The shape
     */
    public static Shape xShape(double size)
    {
        Path2D p = new Path2D.Double();
        double r = size * 0.5;
        p.moveTo(-r, -r);
        p.lineTo( r,  r);
        p.moveTo( r, -r);
        p.lineTo(-r,  r);
        return p;
    }

    /**
     * Creates a cross, centered at the origin, with the given total size
     * 
     * @param size The size
     * @return The shape
     */
    public static Shape cross(double size)
    {
        Path2D p = new Path2D.Double();
        double r = size * 0.5;
        p.moveTo(-r, 0);
        p.lineTo( r, 0);
        p.moveTo(0, -r);
        p.lineTo(0,  r);
        return p;
    }

    /**
     * Creates a diamond, centered at the origin, with the given total size
     * 
     * @param size The size
     * @return The shape
     */
    public static Shape diamond(double size)
    {
        Path2D p = new Path2D.Double();
        double r = size * 0.5;
        p.moveTo(-r, 0);
        p.lineTo(0, -r);
        p.lineTo( r, 0);
        p.lineTo(0,  r);
        p.closePath();
        return p;
    }
    
    /**
     * Creates a circle, centered at the origin, with the given total size
     * 
     * @param size The size
     * @return The shape
     */
    public static Shape circle(double size)
    {
        double r = size * 0.5;
        return new Ellipse2D.Double(-r, -r, size, size);
    }

    /**
     * Creates a square, centered at the origin, with the given total size
     * 
     * @param size The size
     * @return The shape
     */
    public static Shape square(double size)
    {
        double r = size * 0.5;
        return new Rectangle2D.Double(-r, -r, size, size);
    }

    /**
     * Creates a triangle, centered at the origin, with the given total size
     * 
     * @param size The size
     * @return The shape
     */
    public static Shape triangle(double size)
    {
        Path2D p = new Path2D.Double();
        double r = size * 0.5;
        p.moveTo(-r,  r);
        p.lineTo( r,  r);
        p.lineTo( 0, -r);
        p.closePath();
        return p;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private TickShapes()
    {
        // Private constructor to prevent instantiation
    }
}
