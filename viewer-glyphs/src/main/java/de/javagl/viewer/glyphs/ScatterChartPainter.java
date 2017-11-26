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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import de.javagl.geom.Lines;
import de.javagl.viewer.ObjectPainter;

/**
 * Implementation of an {@link ObjectPainter} that paints a {@link ScatterChart}
 */
public final class ScatterChartPainter implements ObjectPainter<ScatterChart>
{
    /**
     * A point, used internally
     */
    private static final Point2D TEMP_POINT = new Point2D.Double();

    /**
     * A line, used internally
     */
    private static final Line2D TEMP_LINE = new Line2D.Double();
    
    /**
     * The paint that should be used for the connecting lines.
     * If this is <code>null</code>, then no connecting lines
     * will be painted.
     */
    private Paint linePaint = null;
    
    /**
     * The stroke that should be used for the connecting lines.
     * If this is <code>null</code>, then no connecting lines
     * will be painted.
     */
    private Stroke lineStroke = null;
    
    /**
     * Default constructor
     */
    public ScatterChartPainter()
    {
        // Default constructor
    }
    
    /**
     * Set the paint that should be used for the connecting lines.
     * If this is <code>null</code>, then no connecting lines
     * will be painted.
     * 
     * @param linePaint The paint
     */
    public void setLinePaint(Paint linePaint)
    {
        this.linePaint = linePaint;
    }
    
    /**
     * Set the stroke that should be used for the connecting lines.
     * If this is <code>null</code>, then no connecting lines
     * will be painted.
     * 
     * @param lineStroke The stroke
     */
    public void setLineStroke(Stroke lineStroke)
    {
        this.lineStroke = lineStroke;
    }
    
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, ScatterChart scatterChart)
    {
        if (scatterChart == null)
        {
            return;
        }
        paintLines(g, worldToScreen, w, h, scatterChart);
        paintPoints(g, worldToScreen, w, h, scatterChart);
    }

    
    /**
     * Paint the lines of the chart, if they are enabled
     * 
     * @param g The graphics
     * @param worldToScreen The world to screen transform
     * @param w The width of the painting area
     * @param h The height of the painting area
     * @param scatterChart The {@link ScatterChart}
     */
    private void paintLines(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, ScatterChart scatterChart)
    {
        if (linePaint == null || lineStroke == null)
        {
            return;
        }
        g.setPaint(linePaint);
        g.setStroke(lineStroke);
        
        double prevX = 0;
        double prevY = 0;

        int n = scatterChart.getNumPoints();
        for (int i=0; i<n; i++)
        {
            double x = scatterChart.getPointX(i);
            double y = scatterChart.getPointY(i);
            if (i > 0)
            {
                TEMP_LINE.setLine(prevX, prevY, x, y);
                Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
                g.draw(TEMP_LINE);
            }
            prevX = x;
            prevY = y;
        }
    }
    
    
    /**
     * Paint the lines of the that have a paint and a shape
     * 
     * @param g The graphics
     * @param worldToScreen The world to screen transform
     * @param w The width of the painting area
     * @param h The height of the painting area
     * @param scatterChart The {@link ScatterChart}
     */
    private void paintPoints(Graphics2D g, AffineTransform worldToScreen,
        double w, double h, ScatterChart scatterChart)
    {
        int n = scatterChart.getNumPoints();
        for (int i=0; i<n; i++)
        {
            Paint paint = scatterChart.getPaint(i);
            if (paint == null)
            {
                continue;
            }
            Shape shape = scatterChart.getShape(i);
            if (shape == null)
            {
                continue;
            }
            double x = scatterChart.getPointX(i);
            double y = scatterChart.getPointY(i);
            
            AffineTransform oldAT = g.getTransform();
            TEMP_POINT.setLocation(x, y);
            worldToScreen.transform(TEMP_POINT, TEMP_POINT);
            g.translate(TEMP_POINT.getX(), TEMP_POINT.getY());
            g.setPaint(paint);
            g.draw(shape);
            g.setTransform(oldAT);
        }
    }
    
    
    
}