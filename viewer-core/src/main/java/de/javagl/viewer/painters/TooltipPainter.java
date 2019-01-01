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
package de.javagl.viewer.painters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.javagl.geom.Rectangles;
import de.javagl.viewer.Painter;

/**
 * A class that can be used to paint a tooltip at a certain location, given
 * in world coordinates
 */
public class TooltipPainter implements Painter
{
    /**
     * A small dot indicating the position
     */
    private final Ellipse2D.Double dot = new Ellipse2D.Double();
    
    /**
     * A line from the anchor to the position
     */
    private final Line2D.Double line = new Line2D.Double();
    
    /**
     * The background paint
     */
    private Paint backgroundPaint = new Color(255,255,255,220);
    
    /**
     * The border paint
     */
    private Paint borderPaint = new Color(128,128,128,220);
    
    /**
     * The text paint
     */
    private Paint textPaint = Color.BLACK;
    
    /**
     * The world position of the tooltip
     */
    private final Point2D worldPosition = new Point2D.Double();
    
    /**
     * The screen position of the tooltip
     */
    private final Point2D screenPosition = new Point2D.Double();
    
    /**
     * The anchor point
     */
    private final Point2D anchor = new Point2D.Double();
    
    /**
     * The text of the tooltip
     */
    private String text = null;
    
    /**
     * Default constructor
     */
    public TooltipPainter()
    {
        // Default constructor
    }
    
    /**
     * Set the anchor point for the tooltip. The coordinates will be relative
     * to the size of the tooltip. This means that for (0,0), the anchor point
     * is at the upper left of the tooltip, and for (1,1), the anchor point
     * will be at the lower right. Values outside the range [0,1] are 
     * possible, and will cause a line to be drawn from the anchor point 
     * to the closest corner. 
     * 
     * @param x The x-coordinate of the anchor point
     * @param y The y-coordinate of the anchor point
     */
    public void setAnchor(double x, double y)
    {
        anchor.setLocation(x, y);
    }
    
    /**
     * Set the tooltip that should be painted. If the given text is 
     * <code>null</code>, then nothing will be painted
     * 
     * @param worldX The x-coordinate of the world position
     * @param worldY The y-coordinate of the world position
     * @param text The text
     */
    public void set(double worldX, double worldY, String text)
    {
        this.worldPosition.setLocation(worldX, worldY);
        this.text = text;
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen,
        double w, double h)
    {
        if (text == null)
        {
            return;
        }
        AffineTransform oldAt = g.getTransform();
        
        Rectangle2D bounds = 
            StringBoundsUtils.computeStringBounds(text, g.getFont());
        Rectangles.scale(bounds, 1.1, bounds);
        
        double textX = -bounds.getMinX();
        double textY = -bounds.getMinY();
        
        Rectangles.translate(
            bounds, -bounds.getMinX(), -bounds.getMinY(), bounds);

        double dx = bounds.getWidth() * anchor.getX();
        double dy = bounds.getHeight() * anchor.getY();
        
        worldToScreen.transform(worldPosition, screenPosition);
        g.translate(screenPosition.getX(), screenPosition.getY());
        
        g.translate(-dx, -dy);
        g.setPaint(backgroundPaint);
        g.fill(bounds);
        
        g.setPaint(borderPaint);
        g.draw(bounds);

        if (anchor.getX() <= 0 || anchor.getX() >= 1.0 ||
            anchor.getY() <= 0 || anchor.getY() >= 1.0) 
        {
            double relX = Math.max(0.0, Math.min(1.0, anchor.getX()));
            double relY = Math.max(0.0, Math.min(1.0, anchor.getY()));
            double dotX = relX * bounds.getWidth();
            double dotY = relY * bounds.getHeight();
            dot.setFrame(dotX - 2, dotY - 2, 5, 5);
            g.fill(dot);
            line.setLine(dx, dy, dotX, dotY);
            g.draw(line);
        }
        
        g.setPaint(textPaint);
        g.translate(textX, textY);
        g.drawString(text, 0, 0);
        
        g.setTransform(oldAt);
        
    }
    
}
