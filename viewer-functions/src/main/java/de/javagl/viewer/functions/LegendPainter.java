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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import de.javagl.viewer.Painter;

/**
 * Implementation of a {@link Painter} that paints a legend in 
 * a {@link FunctionPanel}
 */
class LegendPainter implements Painter
{
    /**
     * The font that will be used for the legend
     */
    private final Font font = new Font("Dialog", Font.PLAIN, 9);
    
    /**
     * The strings to be painted
     */
    private final List<String> strings;
    
    /**
     * The paints (colors) for the strings
     */
    private final List<Paint> paints;
    
    /**
     * The prototype string, determining the width of the legend
     */
    private String prototypeString;
    
    /**
     * Default constructor
     */
    LegendPainter()
    {
        strings = new ArrayList<String>();
        paints = new ArrayList<Paint>();
    }
    
    /**
     * Set the strings that will be painted
     * 
     * @param string The string
     * @param paint The paint to use for the string
     */
    void addString(String string, Paint paint)
    {
        strings.add(string);
        paints.add(paint);
    }
    
    /**
     * Remove all strings from this legend painter
     */
    void clearStrings()
    {
        strings.clear();
        paints.clear();
    }
    
    /**
     * Set the prototype string, determining the width of the rectangle
     * that should contain the actual strings. If this is <code>null</code>,
     * then only the actual strings will be used.
     * 
     * @param prototypeString The prototype string.
     */
    void setPrototypeString(String prototypeString)
    {
        this.prototypeString = prototypeString;
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        if (strings.size() == 0)
        {
            return;
        }
        g.setFont(font);        
        FontMetrics fontMetrics = g.getFontMetrics();
        
        double maxWidth = 0;
        double sumHeight = 0;
        for (String string : strings)
        {
            Rectangle2D lineBounds = fontMetrics.getStringBounds(string, g);
            maxWidth = Math.max(maxWidth, lineBounds.getWidth());
            sumHeight += lineBounds.getHeight();
        }
        
        if (prototypeString != null)
        {
            Rectangle2D lineBounds = 
                fontMetrics.getStringBounds(prototypeString, g);
            maxWidth = Math.max(maxWidth, lineBounds.getWidth());
        }
        
        double borderH = 5;
        double borderV = 2;
        double totalWidth = maxWidth + borderH + borderH;
        double totalHeight = sumHeight + borderV + borderV;
        Rectangle2D totalBounds = new Rectangle2D.Double(
            w - totalWidth, 0, totalWidth, totalHeight);
        
        g.setStroke(new BasicStroke(1.0f));
        g.setColor(new Color(255,255,255,210));
        g.fill(totalBounds);
        g.setColor(Color.GRAY);
        g.draw(totalBounds);
        g.setColor(Color.BLACK);
        double y = 0;
        double x = w - maxWidth - borderH;
        for (int i=0; i<strings.size(); i++)
        {
            String string = strings.get(i);
            Paint paint = paints.get(i);
            
            Rectangle2D lineBounds = fontMetrics.getStringBounds(string, g);
            y += lineBounds.getHeight();
            
            g.setPaint(paint);
            g.drawString(string, (int)x, (int)y);
        }
    }
}