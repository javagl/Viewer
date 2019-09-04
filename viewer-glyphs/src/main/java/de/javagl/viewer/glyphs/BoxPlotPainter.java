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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.javagl.geom.AffineTransforms;
import de.javagl.geom.Lines;
import de.javagl.viewer.ObjectPainter;

/**
 * Implementation of an {@link ObjectPainter} that paints a {@link BoxPlot}
 */
public final class BoxPlotPainter implements ObjectPainter<BoxPlot>
{
    /**
     * A shape for a dot
     */
    private static final Shape DOT_SHAPE = TickShapes.circle(4.0);
    
    /**
     * A rectangle, used internally for painting
     */
    private static final Rectangle2D TEMP_RECTANGLE = new Rectangle2D.Double();

    /**
     * A line, used internally for painting
     */
    private static final Line2D TEMP_LINE = new Line2D.Double();
    
    /**
     * A point, used internally for painting
     */
    private static final Point2D TEMP_POINT = new Point2D.Double();
    
    /**
     * The default stroke
     */
    private static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);
    
    /**
     * A bold stroke
     */
    private static final Stroke BOLD_STROKE = new BasicStroke(2.0f);

    /**
     * The paint for filling the box
     */
    private Paint boxFillPaint = new Color(240,240,240);
    
    /**
     * The paint for drawing the box
     */
    private Paint boxDrawPaint = new Color(100,100,100);
    
    /**
     * The paint for the whiskers
     */
    private Paint whiskerPaint = new Color(100,100,100);
    
    /**
     * The paint for the median
     */
    private Paint medianPaint = new Color(40,40,40);
    
    /**
     * The point for the mean
     */
    private Paint meanPaint = new Color(40,40,40);

    /**
     * The relative width of the box
     */
    private final double relativeBoxWidth = 0.8;

    /**
     * The relative width of the whisker
     */
    private final double relativeWhiskerWidth = 0.5;

    /**
     * The total width of the box plot
     */
    private double width = 1.0;
    
    /**
     * Default constructor
     */
    public BoxPlotPainter()
    {
        // Default constructor
    }
    
    /**
     * Set the paint that will be used for filling the box
     * 
     * @param boxFillPaint The paint
     */
    public void setBoxFillPaint(Paint boxFillPaint)
    {
        this.boxFillPaint = boxFillPaint;
    }

    /**
     * Set the paint that will be used for drawing the box
     * 
     * @param boxDrawPaint The paint
     */
    public void setBoxDrawPaint(Paint boxDrawPaint)
    {
        this.boxDrawPaint = boxDrawPaint;
    }

    /**
     * Set the paint that will be used for drawing the whisker
     * 
     * @param whiskerPaint The paint
     */
    public void setWhiskerPaint(Paint whiskerPaint)
    {
        this.whiskerPaint = whiskerPaint;
    }

    /**
     * Set the paint that will be used for drawing the median line
     * 
     * @param medianPaint The paint
     */
    public void setMedianPaint(Paint medianPaint)
    {
        this.medianPaint = medianPaint;
    }

    /**
     * Set the paint that will be used for filling the mean dot
     * 
     * @param meanPaint The paint
     */
    public void setMeanPaint(Paint meanPaint)
    {
        this.meanPaint = meanPaint;
    }

    /**
     * Set the width of the box plot
     * 
     * @param width The width
     */
    public void setWidth(double width)
    {
        this.width = width;
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, BoxPlot boxPlot)
    {
        Stroke oldStroke = g.getStroke();
        g.setStroke(DEFAULT_STROKE);
        
        double minimum = boxPlot.getMinimum();
        double maximum = boxPlot.getMaximum();
        double lowerQuantile = boxPlot.getLowerQuantile();
        double upperQuantile = boxPlot.getUpperQuantile();
        double median = boxPlot.getMedian();
        double mean = boxPlot.getMean();
        
        double boxOffsetX = (width - relativeBoxWidth * width) * 0.5;
        TEMP_RECTANGLE.setRect(boxOffsetX, lowerQuantile, 
            relativeBoxWidth * width, upperQuantile - lowerQuantile);
        if (boxFillPaint != null || boxDrawPaint != null)
        {
            Shape s = AffineTransforms.createTransformedShape(
                worldToScreen, TEMP_RECTANGLE);
            if (boxFillPaint != null)
            {
                g.setPaint(boxFillPaint);
                g.fill(s);
            }
            if (boxDrawPaint != null)
            {
                g.setPaint(boxDrawPaint);
                g.draw(s);
            }
        }
        
        if (whiskerPaint != null)
        {
            g.setPaint(whiskerPaint);

            double centerX = width * 0.5;
            double whiskerX0 = centerX - relativeWhiskerWidth * 0.5 * width;
            double whiskerX1 = centerX + relativeWhiskerWidth * 0.5 * width;
            AffineTransform at = worldToScreen;
            drawLine(g, at, centerX, minimum, centerX, lowerQuantile);
            drawLine(g, at, whiskerX0, minimum, whiskerX1, minimum);
            drawLine(g, at, centerX, maximum, centerX, upperQuantile);
            drawLine(g, at, whiskerX0, maximum, whiskerX1, maximum);
        }
        
        if (medianPaint != null)
        {
            g.setPaint(medianPaint);
            g.setStroke(BOLD_STROKE);
            drawLine(g, worldToScreen, 
                boxOffsetX, median, width - boxOffsetX, median);
        }

        if (meanPaint != null)
        {
            double meanX = width * 0.5;
            AffineTransform oldAT = g.getTransform();
            TEMP_POINT.setLocation(meanX, mean);
            worldToScreen.transform(TEMP_POINT, TEMP_POINT);
            g.translate(TEMP_POINT.getX(), TEMP_POINT.getY());
            g.setPaint(meanPaint);
            g.setStroke(DEFAULT_STROKE);
            g.draw(DOT_SHAPE);
            g.setTransform(oldAT);
        }
        
        g.setStroke(oldStroke);
    }
    
    /**
     * Draw the specified line into the given graphics, transformed with
     * the given transform
     * 
     * @param g The graphics
     * @param worldToScreen The world-to-screen transform
     * @param x0 The first x-coordinate
     * @param y0 The first y-coordinate 
     * @param x1 The second x-coordinate
     * @param y1 The second y-coordinate
     */
    private static void drawLine(Graphics2D g, AffineTransform worldToScreen, 
        double x0, double y0, double x1, double y1)
    {
        TEMP_LINE.setLine(x0, y0, x1, y1);
        Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
        g.draw(TEMP_LINE);
    }

}