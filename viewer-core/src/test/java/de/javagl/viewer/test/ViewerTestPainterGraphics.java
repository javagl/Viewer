/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import de.javagl.viewer.Painter;

/**
 * Implementation of the {@link Painter} interface that
 * shows some painting in a transformed Graphics2D
 */
class ViewerTestPainterGraphics implements Painter
{
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        Path2D grid = new Path2D.Double();
        for (int x=0; x<100; x+=10)
        {
            for (int y=0; y<50; y+=10)
            {
                grid.moveTo(x, y);
                grid.lineTo(x+10, y);
                grid.moveTo(x, y);
                grid.lineTo(x, y+10);
            }
        }
        Rectangle2D border = new Rectangle2D.Double(0,0,100,50);
       
        AffineTransform oldAT = g.getTransform();
        g.transform(worldToScreen);
        
        g.setColor(Color.LIGHT_GRAY);
        g.draw(grid);
        g.setColor(Color.BLACK);
        g.draw(border);
        g.setColor(Color.RED);
        g.drawString("This is painted", 10, 15);
        g.drawString("by transforming", 10, 30);
        g.drawString("the Graphics2D", 10, 45);

        g.setTransform(oldAT);
    }
}