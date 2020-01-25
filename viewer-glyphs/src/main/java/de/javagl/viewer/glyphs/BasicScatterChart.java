/*
 * www.javagl.de - Viewer - Glyphs
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
package de.javagl.viewer.glyphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;

/**
 * Basic implementation of a {@link ScatterChart} where all visual properties
 * may be set externally.
 */
public class BasicScatterChart implements ScatterChart
{
    /**
     * The supplier for the number of points
     */
    private final IntSupplier numPointsSupplier;
    
    /**
     * The function for the x-coordinates of the points
     */
    private final IntToDoubleFunction pointXFunction;
    
    /**
     * The function for the y-coordinates of the points
     */
    private final IntToDoubleFunction pointYFunction;
    
    /**
     * The function for the shapes
     */
    private IntFunction<? extends Shape> shapeFunction;
    
    /**
     * The function for the fill paint
     */
    private IntFunction<? extends Paint> fillPaintFunction;
    
    /**
     * The function for the draw paint
     */
    private IntFunction<? extends Paint> drawPaintFunction;
    
    /**
     * The function for the draw stroke
     */
    private IntFunction<? extends Stroke> drawStrokeFunction;
    
    /**
     * Creates a new instance.<br>
     * <br>
     * The given coordinate functions may be called with arguments that
     * range from 0 to numPoints-1. 
     * 
     * @param numPointsSupplier The supplier for the number of points
     * @param pointXFunction The function for the x-coordinates of the points
     * @param pointYFunction The function for the y-coordinates of the points
     */
    public BasicScatterChart(
        IntSupplier numPointsSupplier,
        IntToDoubleFunction pointXFunction,
        IntToDoubleFunction pointYFunction)
    {
        this.numPointsSupplier = Objects.requireNonNull(
            numPointsSupplier, "The numPointsSupplier may not be null");
        this.pointXFunction = Objects.requireNonNull(
            pointXFunction, "The pointXFunction may not be null");
        this.pointYFunction = Objects.requireNonNull(
            pointYFunction, "The pointYFunction may not be null");
        
        setShape(TickShapes.square(3));
        setFillPaint(Color.BLACK);
        setDrawPaint(Color.RED);
        setDrawStroke(new BasicStroke(1.0f));
    }
    
    /**
     * Set the shape that should be used for all points
     * 
     * @param shape The shape
     */
    public void setShape(Shape shape)
    {
        setShapeFunction(i -> shape);
    }
    
    /**
     * Set the function that provides the shapes
     * 
     * @param shapeFunction The shape function
     */
    public void setShapeFunction(IntFunction<? extends Shape> shapeFunction)
    {
        this.shapeFunction = optional(shapeFunction);
    }
    
    /**
     * Set the fill paint for all points
     * 
     * @param paint The fill paint
     */
    public void setFillPaint(Paint paint)
    {
        setFillPaintFunction(i -> paint);
    }

    /**
     * Set the function that provides the fill paint
     * 
     * @param fillPaintFunction The fill paint function
     */
    public void setFillPaintFunction(
        IntFunction<? extends Paint> fillPaintFunction)
    {
        this.fillPaintFunction = optional(fillPaintFunction);
    }

    /**
     * Set the draw paint for all points
     * 
     * @param paint The draw paint
     */
    public void setDrawPaint(Paint paint)
    {
        setDrawPaintFunction(i -> paint);
    }
    
    /**
     * Set the function that provides the draw paint
     * 
     * @param drawPaintFunction The draw paint function
     */
    public void setDrawPaintFunction(
        IntFunction<? extends Paint> drawPaintFunction)
    {
        this.drawPaintFunction = optional(drawPaintFunction);
    }
    
    /**
     * Set the draw stroke for all points
     * 
     * @param stroke The draw stroke
     */
    public void setDrawStroke(Stroke stroke)
    {
        setDrawStrokeFunction(i -> stroke);
    }

    /**
     * Set the function that provides the draw stroke
     * 
     * @param drawStrokeFunction The draw stroke function
     */
    public void setDrawStrokeFunction(
        IntFunction<? extends Stroke> drawStrokeFunction)
    {
        this.drawStrokeFunction = optional(drawStrokeFunction);
    }
    
    @Override
    public int getNumPoints()
    {
        return numPointsSupplier.getAsInt();
    }

    @Override
    public double getPointX(int index)
    {
        return pointXFunction.applyAsDouble(index);
    }

    @Override
    public double getPointY(int index)
    {
        return pointYFunction.applyAsDouble(index);
    }

    @Override
    public Shape getShape(int index)
    {
        return shapeFunction.apply(index);
    }

    @Override
    public Paint getFillPaint(int index)
    {
        return fillPaintFunction.apply(index);
    }

    @Override
    public Paint getDrawPaint(int index)
    {
        return drawPaintFunction.apply(index);
    }

    @Override
    public Stroke getDrawStroke(int index)
    {
        return drawStrokeFunction.apply(index);
    }

    
    /**
     * If the given function is not <code>null</code>, it is returned.
     * Otherwise, a function that always returns <code>null</code> is
     * returned.
     * 
     * @param f The function
     * @return The function, or a constant <code>null</code> function
     */
    private static <T> IntFunction<T> optional(IntFunction<T> f)
    {
        if (f != null) 
        {
            return f;
        }
        return i -> null;
    }
    
    
}
