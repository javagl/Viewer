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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import de.javagl.viewer.ObjectPainter;

/**
 * Implementation of an {@link ObjectPainter} that paints a {@link LineChart}
 */
public final class LineChartPainter implements ObjectPainter<LineChart>
{
    /**
     * A transform, used internally for painting
     */
    private static final Point2D TEMP_POINT = new Point2D.Double();

    /**
     * A default stroke
     */
    private static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);

    /**
     * Create default line chart painter
     */
    public LineChartPainter()
    {
        // Default constructor
    }

    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, LineChart lineChart)
    {
        if (lineChart == null)
        {
            return;
        }

        Paint paint = lineChart.getPaint();
        if (paint == null)
        {
            return;
        }
        g.setPaint(paint);

        paintLine(g, worldToScreen, lineChart);
        paintTickMarks(g, worldToScreen, lineChart);
    }

    /**
     * Paint the actual line of the given line chart into the given graphics
     * 
     * @param g The graphics
     * @param worldToScreen The world-to-screen transform
     * @param lineChart The line chart
     */
    private void paintLine(
        Graphics2D g, AffineTransform worldToScreen, LineChart lineChart)
    {
        Stroke stroke = lineChart.getStroke();
        if (stroke != null)
        {
            Path2D p = new Path2D.Double();
            for (int i = 0; i < lineChart.getNumPoints(); i++)
            {
                double worldX = i;
                double value = lineChart.getValue(i);
                double worldY = value;
                if (i == 0)
                {
                    p.moveTo(worldX, worldY);
                }
                else
                {
                    p.lineTo(worldX, worldY);
                }
            }
            g.setStroke(stroke);
            g.draw(worldToScreen.createTransformedShape(p));
        }
    }
    
    /**
     * Paint the tick marks of the given line chart into the given graphics
     * 
     * @param g The graphics
     * @param worldToScreen The world-to-screen transform
     * @param lineChart The line chart
     */
    private void paintTickMarks(
        Graphics2D g, AffineTransform worldToScreen, LineChart lineChart)
    {
        Shape tickShape = lineChart.getTickShape();
        if (tickShape != null)
        {
            Stroke stroke = lineChart.getStroke();
            if (stroke != null)
            {
                g.setStroke(stroke);
            }
            else
            {
                g.setStroke(DEFAULT_STROKE);
            }

            for (int i = 0; i < lineChart.getNumPoints(); i++)
            {
                double worldX = i;
                double value = lineChart.getValue(i);
                double worldY = value;
                AffineTransform oldAT = g.getTransform();
                TEMP_POINT.setLocation(worldX, worldY);
                worldToScreen.transform(TEMP_POINT, TEMP_POINT);
                g.translate(TEMP_POINT.getX(), TEMP_POINT.getY());
                g.draw(tickShape);
                g.setTransform(oldAT);
            }
        }
    }
    

}