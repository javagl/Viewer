/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2020 Marco Hutter - http://www.javagl.de
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
package de.javagl.viewer.selection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Objects;
import java.util.function.Supplier;

import de.javagl.viewer.Painter;

/**
 * A painter that paints a selection shape
 */
class SelectionShapePainter implements Painter
{
    /**
     * The supplier for the selection shape 
     */
    private final Supplier<? extends Shape> selectionShapeSupplier;

    /**
     * The stroke width for the selection shape
     */
    private final float strokeWidth = 1.0f;
    
    /**
     * The dashing size for the selection shape
     */
    private final float dash = 5.0f;
    
    /**
     * The first stroke for the selection shape
     */
    private final Stroke strokeA = 
        new BasicStroke(strokeWidth,  BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_MITER, 1.0f, new float[]{dash}, 0.0f);
    /**
     * The second stroke for the selection shape
     */
    private final Stroke strokeB = 
        new BasicStroke(strokeWidth,  BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_MITER, 1.0f, new float[]{dash}, dash);

    /**
     * The fill color for the selection shape
     */
    private final Color fillColor = new Color(0, 255, 0, 32);

    /**
     * Creates a painter for the selection shape
     * 
     * @param selectionShapeSupplier The supplier for the selection shape
     */
    SelectionShapePainter(Supplier<? extends Shape> selectionShapeSupplier)
    {
        this.selectionShapeSupplier = Objects.requireNonNull(
            selectionShapeSupplier, 
            "The selectionShapeSupplier may not be null");
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        Shape selectionShape = selectionShapeSupplier.get();
        if (selectionShape != null)
        {
            Path2D paintedPath = new Path2D.Double(Path2D.WIND_EVEN_ODD);
            paintedPath.append(selectionShape.getPathIterator(null), false);
            paintedPath.closePath();

            g.setColor(fillColor);
            g.fill(paintedPath);
            
            g.setStroke(strokeA);                
            g.setColor(Color.BLACK);
            g.draw(paintedPath);
            
            g.setStroke(strokeB);                
            g.setColor(Color.WHITE);
            g.draw(paintedPath);
        }
    }
}
