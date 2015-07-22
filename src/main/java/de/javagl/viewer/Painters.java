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
 * Methods related to {@link Painter}s and {@link ObjectPainter}s
 */
public class Painters
{
    /**
     * Creates a new {@link Painter} that only paints the given object
     * with the given {@link ObjectPainter}
     * 
     * @param <T> The type of the painted object
     * @param objectPainter The {@link ObjectPainter}
     * @param object The painted object
     * @return The new {@link Painter}
     */
    public static <T> Painter create(
        final ObjectPainter<T> objectPainter, final T object)
    {
        return new Painter()
        {
            @Override
            public void paint(Graphics2D g, AffineTransform worldToScreen, 
                double w, double h)
            {
                objectPainter.paint(g, worldToScreen, w, h, object);
            }
        };
    }
    
//    /**
//     * Draw the given string into the given graphics if there is enough
//     * space for the string. The specified anchor point is a point in
//     * [0,1]x[0,1] that describes the point (on the bounding box of the
//     * string, referring to the upper left point of the bounding box) 
//     * that should be located at the given (x,y) position.
//     * 
//     * @param g The graphics
//     * @param worldToScreen The world-to-screen transform
//     * @param string The string to draw
//     * @param stringAnchorX The x-coordinate of the string anchor
//     * @param stringAnchorY The y-coordinate of the string anchor
//     * @param x The x-coordinate, in world coordinates, where the string 
//     * anchor should be placed 
//     * @param y The y-coordinate, in world coordinates, where the string 
//     * anchor should be placed 
//     * @param worldSpaceX The available space in world coordinates, x-direction
//     * @param worldSpaceY The available space in world coordinates, y-direction
//     */
//    public static void conditionallyDrawStringInWorld(
//        Graphics2D g, AffineTransform worldToScreen, String string, 
//        double stringAnchorX, double stringAnchorY, 
//        double x, double y, 
//        double worldSpaceX, double worldSpaceY)
//    {
//        double screenSpaceX = 
//            GeomUtils.computeDistanceX(worldToScreen, worldSpaceX);
//        double screenSpaceY = 
//            GeomUtils.computeDistanceY(worldToScreen, worldSpaceY);
//        conditionallyDrawStringInScreen(g, worldToScreen, string, 
//            stringAnchorX, stringAnchorY, x, y, screenSpaceX, screenSpaceY);
//    }
//    
//    /**
//     * Draw the given string into the given graphics if there is enough
//     * space for the string. The specified anchor point is usually a point in
//     * [0,1]x[0,1] that describes the point (on the bounding box of the
//     * string, referring to the upper left point of the bounding box) 
//     * that should be located at the given (x,y) position.
//     * 
//     * @param g The graphics
//     * @param worldToScreen The world-to-screen transform
//     * @param string The string to draw
//     * @param stringAnchorX The x-coordinate of the string anchor. Usually
//     * a value in [0,1]
//     * @param stringAnchorY The y-coordinate of the string anchor. Usually
//     * a value in [0,1]
//     * @param x The x-coordinate, in world coordinates, where the string 
//     * anchor should be placed 
//     * @param y The y-coordinate, in world coordinates, where the string 
//     * anchor should be placed 
//     * @param screenSpaceX The available space on the screen, in x-direction
//     * @param screenSpaceY The available space on the screen, in y-direction
//     */
//    public static void conditionallyDrawStringInScreen(
//        Graphics2D g, AffineTransform worldToScreen, String string, 
//        double stringAnchorX, double stringAnchorY, 
//        double x, double y, 
//        double screenSpaceX, double screenSpaceY)
//    {
//        FontMetrics fontMetrics = g.getFontMetrics();
//        
//        // Compute an estimate of the string bounds
//        double stringWidth = 0;
//        for (int i=0; i<string.length(); i++)
//        {
//            stringWidth += fontMetrics.charWidth(string.charAt(i));
//        }
//        double stringHeight = fontMetrics.getHeight();
//        
//        // More precise string bounds, but much more expensive
//        // (although still less precise and expensive than TextLayout)
//        //Rectangle2D stringBounds = fontMetrics.getStringBounds(string, g);
//        //double stringWidth = stringBounds.getWidth();
//        //double stringHeight = stringBounds.getHeight();
//        
//        if (screenSpaceX > 1.05 * stringWidth && 
//            screenSpaceY > 1.05 * stringHeight)
//        {
//            double cx = GeomUtils.computeX(worldToScreen, x, y);
//            double cy = GeomUtils.computeY(worldToScreen, x, y);
//            int sx = (int)(cx - stringAnchorX * stringWidth);
//            int sy = (int)(cy - stringAnchorY * stringHeight + stringHeight);
//            g.drawString(string, sx, sy);
//            
////            double sx = (cx - stringAnchorX * stringWidth);
////            double sy = (cy - stringAnchorY * stringHeight + stringHeight);
////            g.translate(sx, sy);
////            g.drawString(string, 0, 0);
////            g.translate(-sx, -sy);
//            
//        }
//    }
    
    
//    /**
//     * Creates an affine transform that is the concatenation of the
//     * given transform and a scaling operation about the given factor
//     * at the center of the given bounds. If the given input transform 
//     * is <code>null</code>, then the identity transform will be used. 
//     * If the given result transform is <code>null</code>, then a new 
//     * transform will be created and returned.
//     * 
//     * @param affineTransform The input transform
//     * @param bounds The bounds
//     * @param scaling The scaling factor
//     * @param result The transform that will store the result
//     * @return The transform
//     */
//    public static AffineTransform scaleAboutCenter(
//        AffineTransform affineTransform, Rectangle2D bounds, 
//        double scaling, AffineTransform result)
//    {
//        double centerX = bounds.getCenterX();
//        double centerY = bounds.getCenterY();
//        if (result == null)
//        {
//            result = new AffineTransform();
//        }
//        if (affineTransform != null)
//        {
//            result.setTransform(affineTransform);
//        }
//        result.translate(centerX, centerY);
//        result.scale(scaling, scaling);
//        result.translate(-centerX, -centerY);
//        return result;
//    }
    
    
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Painters()
    {
        // Private constructor to prevent instantiation
    }
}
