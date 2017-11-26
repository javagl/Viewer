package de.javagl.viewer.glyphs.test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import de.javagl.viewer.Painter;

/**
 * A painter to fill the unit square with a background paint
 */
class BackgroundPainter implements Painter
{
    /**
     * A unit rectangle
     */
    private static final Shape UNIT_RECTANGLE = 
        new Rectangle2D.Double(0,0,1,1);
    
    /**
     * The background paint. If this is <code>null</code>, then no background
     * will be painted
     */
    private Paint background = null;

    /**
     * Set the background paint. If this is <code>null</code>, then no 
     * background will be painted
     * 
     * @param background The background
     */
    public void setBackground(Paint background)
    {
        this.background = background;
    }
    
    /**
     * Set the background paint to be a vertical gradient between the given 
     * colors
     * 
     * @param topColor The color at the top
     * @param bottomColor The color at the bottom 
     */
    public void setVerticalGradient(Color topColor, Color bottomColor)
    {
        this.background = new GradientPaint(0, 0, topColor, 0, 1, bottomColor);
    }
    
    @Override
    public void paint(Graphics2D g, 
        AffineTransform worldToScreen, double w, double h)
    {
        if (background != null)
        {
            AffineTransform oldAt = g.getTransform();
            g.transform(worldToScreen);
            g.setPaint(background);
            g.fill(UNIT_RECTANGLE);
            g.setTransform(oldAt);
        }
    }
    
    
}
