/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import de.javagl.viewer.Painter;

/**
 * Implementation of the {@link Painter} interface that
 * shows some painting of transformed Shapes
 */
class ViewerTestPainterShapes implements Painter
{
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        Path2D grid = new Path2D.Double();
        for (int x=0; x<100; x+=10)
        {
            for (int y=50; y<100; y+=10)
            {
                grid.moveTo(x, y);
                grid.lineTo(x+10, y);
                grid.moveTo(x, y);
                grid.lineTo(x, y+10);
            }
        }
        Rectangle2D border = new Rectangle2D.Double(0,50,100,50);
       
        g.setColor(Color.LIGHT_GRAY);
        g.draw(worldToScreen.createTransformedShape(grid));
        g.setColor(Color.BLACK);
        g.draw(worldToScreen.createTransformedShape(border));
        g.setColor(Color.RED);
       
        AffineTransform at = worldToScreen;
        g.fill(createText("This is painted", g.getFont(), at, 10, 65));
        g.fill(createText("by transforming", g.getFont(), at, 10, 80));
        g.fill(createText("the shapes", g.getFont(), at, 10, 95));
    }

    /**
     * Create a shape of the specified text.
     *
     * @param text The text
     * @param font The font
     * @param at The AffineTransform
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The shape of the text
     */
    private static Shape createText(
        String text, Font font, AffineTransform at, int x, int y)
    {
        FontRenderContext fontRenderContext =
            new FontRenderContext(null, true, true);      
        GlyphVector glyphVector =
            font.createGlyphVector(fontRenderContext, text);
        AffineTransform fullAt = new AffineTransform(at);
        fullAt.translate(x, y);
        return fullAt.createTransformedShape(glyphVector.getOutline());
    }
}