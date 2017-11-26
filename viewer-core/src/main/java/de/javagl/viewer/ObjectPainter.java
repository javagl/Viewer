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
 * Interface for all classes that may paint some object. Instances of
 * classes implementing this interface may serve as a delegate for 
 * general painting operations, for example, in a {@link SimpleObjectPainter}.
 *  
 * @param <T> The type of the object
 */
public interface ObjectPainter<T>
{
    /**
     * Perform the painting operations for the given object on the given 
     * Graphics.<br>
     * <br>
     * See {@link Painter#paint(Graphics2D, AffineTransform, double, double)}
     * for more information about how the Graphics object is handled among
     * painters.
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform. This transform
     * encapsulates the translation, rotation and scaling of the viewer
     * to which this painter belongs. 
     * @param w The width of the area, in screen coordinates, in which
     * this painter operates. This is the screen size of the viewer to
     * which this painter belongs.
     * @param h The height of the area, in screen coordinates, in which
     * this painter operates. This is the screen size of the viewer to
     * which this painter belongs.
     * @param object The object to paint. One has to assume that
     * this object may be <code>null</code>, in which case the
     * painting operation will usually be skipped.
     */
    void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, T object);
}