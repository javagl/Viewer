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
 * Interface for all classes that may perform painting operations. 
 * Instances of classes implementing this interface may be passed 
 * to the {@link Viewer#addPainter(Painter)} method. <br>
 * <br>
 * By default, a {@link Painter} just offers a method that should perform 
 * an arbitrary painting operation. In order to create painters that can 
 * paint specific objects, the {@link ObjectPainter} interface may be 
 * used: The {@link SimpleObjectPainter} is an implementation of the 
 * {@link Painter} interface that allows setting the object that should
 * be painted, and delegates to an {@link ObjectPainter}.
 */
public interface Painter
{
    /**
     * Perform the painting operations on the given Graphics. <br>
     * <br>
     * Note that the same <code>Graphics</code> instance will be passed to 
     * all painters. No painter should make any assumptions about the state of 
     * the Graphics object. Changes in the given Graphics object will affect 
     * painters that are called subsequently. This refers to configuration 
     * settings, like the rendering hints, but also to the 
     * <code>transform</code> of the Graphics.<br> 
     * <br> 
     * Settings like the rendering hints will usually be done globally by the 
     * {@link Viewer}. If a painter modifies these settings, then it should 
     * either restore the original settings afterwards, or create a local 
     * copy of the Graphics object in order to avoid interferences with 
     * other painters.<br>
     * <br>
     * If the painter needs to transform the Graphics, then it should either
     * use a local copy of the graphics object, or restore the original 
     * transform after it finished painting:
     * <pre><code>
     * public void paint(
     *     Graphics2D g, AffineTransform worldToScreen, double w, double h)
     * {
     *     // Store the original transform
     *     AffineTransform oldAT = g.getTransform();
     *     
     *     // Perform custom transforms and painting
     *     g.translate(100,100);
     *     g.drawLine(10,20,30,40);
     *     ...
     *     
     *     // Restore the original transform
     *     g.setTransform(oldAT);
     * }
     * </code></pre>
     * 
     * The <code>AffineTransform</code> that is passed to this method
     * may also be the same for multiple painters. Usually, the caller
     * should make sure that modifications of this transform do not 
     * affect painters that are called subsequently, but in general,
     * it is not recommended to internally store or modify the given 
     * affine transform object. <br>
     * <br>
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
     */
    void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h);
}