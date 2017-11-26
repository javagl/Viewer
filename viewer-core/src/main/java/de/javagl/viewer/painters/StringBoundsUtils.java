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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Utility methods for computing string bounds
 */
public class StringBoundsUtils
{
    /**
     * A default graphics instance
     */
    private static final Graphics2D DEFAULT_GRAPHICS;
    static
    {
        BufferedImage bi = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        DEFAULT_GRAPHICS = bi.createGraphics();
        DEFAULT_GRAPHICS.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    /**
     * Compute the bounds of the given string when rendered with the given
     * font into a default graphics object that uses fractional metrics.
     * 
     * @param string The string
     * @param font The font
     * @return The string bounds
     */
    public static Rectangle2D computeStringBounds(String string, Font font)
    {
        return computeStringBounds(string, font, new Rectangle2D.Double());
    }
    
    /**
     * Compute the bounds of the given string when rendered with the given
     * font into a default graphics object that uses fractional metrics.<br>
     * <br>
     * The bounds will be stored in the given rectangle, which is then
     * returned. If the given result rectangle is <code>null</code>,
     * then a new rectangle will be created and returned. 
     * 
     * @param string The string
     * @param font The font
     * @param result The rectangle that will store the result
     * @return The string bounds
     */
    public static Rectangle2D computeStringBounds(
        String string, Font font, Rectangle2D result)
    {
        final float helperFontSize = 1000.0f;
        final float fontSize = font.getSize2D();
        final float scaling = fontSize / helperFontSize;
        Font helperFont = font.deriveFont(helperFontSize);
        FontMetrics fontMetrics = DEFAULT_GRAPHICS.getFontMetrics(helperFont);
        double stringWidth = fontMetrics.stringWidth(string) * scaling;
        double stringHeight = fontMetrics.getHeight() * scaling;
        if (result == null)
        {
            result = new Rectangle2D.Double();
        }
        result.setRect(
            0, -fontMetrics.getAscent() * scaling, 
            stringWidth, stringHeight);
        return result;
 
    }
    
    

    /**
     * Private constructor to prevent instantiation
     */
    private StringBoundsUtils()
    {
        // Private constructor to prevent instantiation
    }
    
}
