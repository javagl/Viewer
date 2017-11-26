/*
 * www.javagl.de - Viewer - Functions
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
package de.javagl.viewer.functions;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.function.DoubleFunction;

import de.javagl.geom.AffineTransforms;
import de.javagl.geom.Rectangles;
import de.javagl.viewer.Painter;

/**
 * Implementation of the {@link Painter} interface that paints a function
 */
class FunctionPainter implements Painter
{
    /**
     * A transform, used internally for painting
     */
    private final AffineTransform TEMP_TRANSFORM = new AffineTransform();
    
    /**
     * The functions to be painted
     */
    private final DoubleFunction<? extends Number> function;
    
    /**
     * The paint (color) in which the function should be painted
     */
    private final Paint paint;
    
    /**
     * The stroke with which the function should be painted
     */
    private final Stroke stroke;
    
    /**
     * Creates a new painter for the given function, with the given paint
     * (color) and a default stroke 
     * 
     * @param function The function 
     * @param paint The paint (color)
     */
    FunctionPainter(
        DoubleFunction<? extends Number> function, Paint paint)
    {
        this(function, paint, 2.0f);
    }
    
    /**
     * Creates a new painter for the given function with the given paint
     * (color) and a basic stroke with the given line width 
     * 
     * @param function The function 
     * @param paint The paint (color)
     * @param lineWidth The line width
     */
    FunctionPainter(
        DoubleFunction<? extends Number> function, Paint paint, float lineWidth)
    {
        this(function, paint, new BasicStroke(lineWidth));
    }
    
    /**
     * Creates a new painter for the given function
     * 
     * @param function The function 
     * @param paint The paint (color)
     * @param stroke The stroke
     */
    FunctionPainter(
        DoubleFunction<? extends Number> function, Paint paint, Stroke stroke)
    {
        this.function = function;
        this.paint = paint;
        this.stroke = stroke;
    }
    
    /**
     * Returns the function that is painted
     * 
     * @return The function
     */
    DoubleFunction<? extends Number> getFunction()
    {
        return function;
    }
    
    /**
     * Returns the paint (color) that is used for the function
     * 
     * @return The paint
     */
    Paint getPaint()
    {
        return paint;
    }
    
    /**
     * Returns the stroke that is used for the function
     * 
     * @return The stroke
     */
    Stroke getStroke()
    {
        return stroke;
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        Object oldAntialiasingHint = 
            g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        TEMP_TRANSFORM.setTransform(worldToScreen);
        //TEMP_TRANSFORM.scale(1.0, -1.0);
        
        Rectangle2D boundsWorld = Rectangles.computeBounds(
            AffineTransforms.invert(TEMP_TRANSFORM, null), 
            new Rectangle2D.Double(0,0,w,h), null);
        double w0 = boundsWorld.getMinX();
        double w1 = boundsWorld.getMaxX();

        Path2D path = new Path2D.Double();
        int steps = (int)w;
        double wStep = (w1-w0)/steps;
        Point2D previousScreenPoint = null;
        for (int x=0; x<=steps; x++)
        {
            double wx = w0 + wStep * x;
            Number wy = function.apply(wx);
            Point2D screenPoint = null;
            if (wy != null && Double.isFinite(wy.doubleValue()))
            {
                screenPoint = TEMP_TRANSFORM.transform(
                    new Point2D.Double(wx, wy.doubleValue()), null);
            }
            else
            {
                previousScreenPoint = null;
            }
            if (screenPoint != null)
            {
                if (previousScreenPoint == null)
                {
                    path.moveTo(screenPoint.getX(), screenPoint.getY());
                }
                else
                {
                    path.lineTo(screenPoint.getX(), screenPoint.getY());
                }
            }
            previousScreenPoint = screenPoint;
        }
        g.setStroke(stroke);
        g.setPaint(paint);
        g.draw(path);
        
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, 
            oldAntialiasingHint);
    }
    
}

