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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Utility methods for geometry computations. These methods are equivalent to 
 * methods offered in the "Geom" library. If the number of methods increases, 
 * the Geom library may be added as a dependency, and these methods may be 
 * replaced.
 */
class GeomUtils
{
    /**
     * Compute the bounding rectangle of the given rectangle after it
     * has been transformed with the given transform, and stores it
     * in the given destination. If the given destination is <code>null</code>
     * then a new rectangle will be created and returned
     * 
     * @param at The transform
     * @param rSrc The source rectangle
     * @param rDst The destination rectangle
     * @return The destination rectangle
     */
    static Rectangle2D computeBounds(
        AffineTransform at, Rectangle2D rSrc, Rectangle2D rDst)
    {
        double xMin = rSrc.getMinX();
        double yMin = rSrc.getMinY();
        double xMax = rSrc.getMaxX();
        double yMax = rSrc.getMaxY();
        
        double x0 = computeX(at, xMin, yMin);
        double y0 = computeY(at, xMin, yMin);
        double x1 = computeX(at, xMax, yMin);
        double y1 = computeY(at, xMax, yMin);
        double x2 = computeX(at, xMax, yMax);
        double y2 = computeY(at, xMax, yMax);
        double x3 = computeX(at, xMin, yMax);
        double y3 = computeY(at, xMin, yMax);

        double minX = min(x0, x1, x2, x3);
        double minY = min(y0, y1, y2, y3);
        double maxX = max(x0, x1, x2, x3);
        double maxY = max(y0, y1, y2, y3);
        
        if (rDst == null)
        {
            return new Rectangle2D.Double(minX, minY, maxX-minX, maxY-minY);
        }
        rDst.setRect(minX, minY, maxX-minX, maxY-minY);
        return rDst;
    }

    /**
     * Returns the minimum of the given numbers 
     * 
     * @param d0 The first number
     * @param d1 The second number
     * @param d2 The third number
     * @param d3 The fourth number
     * @return The result
     */
    private static double min(double d0, double d1, double d2, double d3)
    {
        double min = d0;
        if (d1 < min)
        {
            min = d1;
        }
        if (d2 < min)
        {
            min = d2;
        }
        if (d3 < min)
        {
            min = d3;
        }
        return min;
    }
    
    /**
     * Returns the maximum of the given numbers 
     * 
     * @param d0 The first number
     * @param d1 The second number
     * @param d2 The third number
     * @param d3 The fourth number
     * @return The result
     */
    private static double max(double d0, double d1, double d2, double d3)
    {
        double max = d0;
        if (d1 > max)
        {
            max = d1;
        }
        if (d2 > max)
        {
            max = d2;
        }
        if (d3 > max)
        {
            max = d3;
        }
        return max;
    }

    /**
     * Computes the x-coordinate of the given point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param p The point
     * @return The x-coordinate of the transformed point
     */
    static double computeX(AffineTransform at, Point2D p)
    {
        return computeX(at, p.getX(), p.getY());
    }

    /**
     * Computes the x-coordinate of the specified point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return The x-coordinate of the transformed point
     */
    static double computeX(AffineTransform at, double x, double y)
    {
        return 
            at.getScaleX() * x + 
            at.getShearX() * y + 
            at.getTranslateX();
    }
    
    /**
     * Computes the y-coordinate of the given point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param p The point
     * @return The y-coordinate of the transformed point
     */
    static double computeY(AffineTransform at, Point2D p)
    {
        return computeY(at, p.getX(), p.getY());
    }

    /**
     * Computes the y-coordinate of the specified point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return The y-coordinate of the transformed point
     */
    static double computeY(AffineTransform at, double x, double y)
    {
        return 
            at.getShearY() * x + 
            at.getScaleY() * y + 
            at.getTranslateY();
    }
    
    /**
     * Computes the distance that two points have when they are transformed
     * with the given affine transform, and initially had the given distance
     * in x-direction
     * 
     * @param at The affine transform
     * @param distanceX The distance
     * @return The distance after the transform
     */
    static double computeDistanceX(AffineTransform at, double distanceX)
    {
        double dx = at.getScaleX() * distanceX;
        double dy = at.getShearY() * distanceX;
        double result = Math.sqrt(dx*dx+dy*dy);
        return result;
    }

    /**
     * Computes the distance that two points have when they are transformed
     * with the given affine transform, and initially had the given distance
     * in y-direction
     * 
     * @param at The affine transform
     * @param distanceY The distance
     * @return The distance after the transform
     */
    static double computeDistanceY(AffineTransform at, double distanceY)
    {
        double dx = at.getShearX() * distanceY;
        double dy = at.getScaleY() * distanceY;
        double result = Math.sqrt(dx*dx+dy*dy);
        return result;
    }

    /**
     * Transforms the given point with the inverse of the given transform,
     * and stores the result in the given destination point. If the given
     * destination point is <code>null</code>, a new point will be created
     * and returned.
     * 
     * @param at The affine transform
     * @param pSrc The source point
     * @param pDst The destination point
     * @return The destination point
     * @throws IllegalArgumentException If the given transform is not
     * invertible
     */
    static Point2D inverseTransform(
        AffineTransform at, Point2D pSrc, Point2D pDst)
    {
        try
        {
            return at.inverseTransform(pSrc, pDst);
        }
        catch (NoninvertibleTransformException e)
        {
            throw new IllegalArgumentException(
                "Non-invertible transform", e);
        }
    }
    
    /**
     * Make sure that the given determinant is non-null and has a 
     * valid (and non-zero) determinant
     * 
     * @param at The affine transform
     * @throws NullPointerException If the given transform is <code>null</code>
     * @throws IllegalArgumentException If the determinant of the given 
     * transform is 0.0, or NaN, or infinite
     */
    static void validate(AffineTransform at)
    {
        double determinant = at.getDeterminant();
        if (determinant == 0.0 || 
            Double.isNaN(determinant) ||
            Double.isInfinite(determinant))
        {
            throw new IllegalArgumentException("Determinant is "+determinant);
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private GeomUtils()
    {
        // Private constructor to prevent instantiation
    }
    
}
