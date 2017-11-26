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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.function.Predicate;

import de.javagl.geom.AffineTransforms;
import de.javagl.geom.Lines;
import de.javagl.viewer.ObjectPainter;
import de.javagl.viewer.painters.GeneralLabelPainterPredicate;
import de.javagl.viewer.painters.LabelPainter;


/**
 * Implementation of an {@link ObjectPainter} that paints a {@link BarChart}
 */
public final class BarChartPainter implements ObjectPainter<BarChart>
{
    /**
     * A rectangle, used internally for painting
     */
    private static final Rectangle2D TEMP_RECTANGLE = new Rectangle2D.Double();

    /**
     * A line, used internally for painting
     */
    private static final Line2D TEMP_LINE = new Line2D.Double();

    /**
     * The color for the labels
     */
    private final Color labelColor = Color.BLACK;
    
    /**
     * The relative width of the bars referring to the space that is
     * available for each bar, based on the number of bars. If there
     * are 10 bars, then the space for each bar will be 0.1. This
     * is multiplied by the relativeBarWidth in order to obtain the
     * actual width of the bars.
     */
    private final double relativeBarWidth = 0.9;

    /**
     * The {@link LabelPainter} for the value strings 
     */
	private final LabelPainter valueStringLabelPainter;

	/**
	 * The {@link LabelPainter} for the bar labels
	 */
    private final LabelPainter labelPainter;
    
    /**
     * A predicate to paint labels when their width is not greater than
     * a specified maximum. The 
     * {@link LabelPainter#setLabelPaintingCondition(Predicate)} is 
     * initialized with this predicate, which is then updated based
     * on the width that is available for each bar on the screen.
     */
    private final GeneralLabelPainterPredicate widthPredicate = 
        new GeneralLabelPainterPredicate();
    
    /**
     * Creates a default bar chart painter
     */
    public BarChartPainter()
    {
        Font font = new Font("Dialog", Font.PLAIN, 9);

        valueStringLabelPainter = new LabelPainter();
        valueStringLabelPainter.setTransformingLabels(false);
        valueStringLabelPainter.setFont(font);
        valueStringLabelPainter.setLabelAnchor(0.5, 1.0);
        valueStringLabelPainter.setLabelPaintingCondition(widthPredicate);
        
        labelPainter = new LabelPainter();
        labelPainter.setTransformingLabels(false);
        labelPainter.setFont(font);
        labelPainter.setLabelAnchor(0.5, 0.0);
        labelPainter.setLabelPaintingCondition(widthPredicate);
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, BarChart barChart)
    {
        if (barChart == null)
        {
            return;
        }
        
        // Compute the width information of the bars
        int n = barChart.getNumBars();
        double spacePerBarX = 1.0;
        double offsetX = (1 - relativeBarWidth) * spacePerBarX * 0.5;
        double barWidth = spacePerBarX - offsetX - offsetX;

        for (int i=0; i<n; i++)
        {
            double value = barChart.getValue(i);
            
            double barRectMinY = 0.0;
        	double barRectMaxY = value;
        	if (value < 0.0)
        	{
        		barRectMinY = value;
        		barRectMaxY = 0.0;
        	}
        	double barRectMinX = i * spacePerBarX + offsetX;
        	double barRectHeight = barRectMaxY - barRectMinY;
            TEMP_RECTANGLE.setRect(
                barRectMinX, Math.min(value, 0.0),
                barWidth, barRectHeight);

            Paint barPaint = barChart.getBarPaint(i);
            if (barPaint != null)
            {
                g.setPaint(barPaint);
                
                // If the bar has a height of nearly 0, then only draw a line
                if (barRectHeight < 1e-5)
                {
                    TEMP_LINE.setLine(
                        barRectMinX, barRectMinY, 
                        barRectMinX + barWidth, barRectMinY);
                    Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
                    g.draw(TEMP_LINE);
                }
                else
                {
                    Shape s = AffineTransforms.createTransformedShape(
                        worldToScreen, TEMP_RECTANGLE);
                    g.fill(s);
                }
            }
            
            double spacePerBarScreenX = 
                AffineTransforms.computeDistanceX(
                    worldToScreen, spacePerBarX);
            widthPredicate.setMaximumScreenWidth(spacePerBarScreenX);

            if (spacePerBarScreenX > 5)
            {
                String label = barChart.getLabel(i);
                if (label != null)
                {
                    g.setColor(labelColor);
                    labelPainter.setLabelLocation(
                        TEMP_RECTANGLE.getCenterX(), 0.0);
                    labelPainter.paint(
                        g, worldToScreen, w, h, label);
                }
                String valueString = barChart.getValueString(i);
                if (valueString != null)
                {
                    g.setColor(labelColor);
                    valueStringLabelPainter.setLabelLocation(
                        TEMP_RECTANGLE.getCenterX(), value);
                    valueStringLabelPainter.paint(
                        g, worldToScreen, w, h, valueString);
                }
            }
        }
    }
}