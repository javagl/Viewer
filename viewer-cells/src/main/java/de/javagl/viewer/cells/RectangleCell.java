/*
 * www.javagl.de - Cells
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
package de.javagl.viewer.cells;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.javagl.geom.AffineTransforms;

/**
 * Implementation of a rectangle {@link Cell}
 */
class RectangleCell implements Cell
{
    /**
     * The x-coordinate of this cell
     */
    private final int x;

    /**
     * The y-coordinate of this cell
     */
    private final int y;

    /**
     * The origin of this cell
     */
    private final Point2D origin;
    
    /**
     * The center of this cell
     */
    private final Point2D center;
    
    /**
     * The shape of this cell
     */
    private final Shape shape;
    
    /**
     * The transform that has to be concatenated with the
     * cell transform in order to obtain the content transform
     */
    private final AffineTransform contentTransformPart;

    /**
     * Creates a new cell
     * 
     * @param x The x-coordinate of this cell
     * @param y The y-coordinate of this cell
     * @param prototypeShape The prototype shape for this cell. A transformed
     * version of this shape will become the shape of this cell
     * @param origin The origin of this cell. A reference will be stored.
     * @param cellSizeX The cell size, in x-direction
     * @param cellSizeY The cell size, in y-direction
     */
    RectangleCell(int x, int y, Shape prototypeShape, Point2D origin,
        double cellSizeX, double cellSizeY)
    {
        this.x = x;
        this.y = y;
        this.origin = origin;
        this.contentTransformPart = 
            AffineTransform.getScaleInstance(cellSizeX, cellSizeY);
        AffineTransform at = AffineTransform.getTranslateInstance(
            origin.getX(), origin.getY());
        this.shape = AffineTransforms.createTransformedShape(
            at, prototypeShape);
        
        Rectangle2D bounds = shape.getBounds2D();
        this.center = new Point2D.Double(
            bounds.getCenterX(), 
            bounds.getCenterY());
    }

    @Override
    public int getX()
    {
        return x;
    }

    @Override
    public int getY()
    {
        return y;
    }
    
    @Override
    public Shape getShape()
    {
        return shape;
    }
    
    @Override
    public double getOriginX()
    {
        return origin.getX();
    }

    @Override
    public double getOriginY()
    {
        return origin.getY();
    }

    @Override
    public double getCenterX()
    {
        return center.getX();
    }

    @Override
    public double getCenterY()
    {
        return center.getY();
    }
    
    @Override
    public AffineTransform getTransform(AffineTransform at)
    {
        if (at == null)
        {
            return AffineTransform.getTranslateInstance(
                origin.getX(), origin.getY());
        }
        at.setToTranslation(origin.getX(), origin.getY());
        return at;
    }
    
    @Override
    public AffineTransform concatenateWithTransform(
        AffineTransform at, AffineTransform result)
    {
        if (result == null)
        {
            result = new AffineTransform(at);
        }
        else
        {
            result.setTransform(at);
        }
        result.translate(origin.getX(), origin.getY());
        return result;
    }

    @Override
    public AffineTransform getContentTransform(AffineTransform at)
    {
        at = getTransform(at);
        at.concatenate(contentTransformPart);
        return at;
    }
    
    @Override
    public AffineTransform concatenateWithContentTransform(
        AffineTransform at, AffineTransform result)
    {
        if (result == null)
        {
            result = new AffineTransform(at);
        }
        result = concatenateWithTransform(at, result);
        result.concatenate(contentTransformPart);
        return result;
    }

    @Override
    public String toString()
    {
        return "RectangleCell["+x+","+y+"]";
    }
}
