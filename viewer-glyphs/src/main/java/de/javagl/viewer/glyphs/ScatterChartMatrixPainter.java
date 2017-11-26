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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import de.javagl.geom.AffineTransforms;
import de.javagl.viewer.ObjectPainter;
import de.javagl.viewer.painters.GeneralLabelPainterPredicate;
import de.javagl.viewer.painters.LabelPainter;

/**
 * Implementation of an {@link ObjectPainter} that paints a 
 * {@link ScatterChartMatrix}
 */
public final class ScatterChartMatrixPainter 
    implements ObjectPainter<ScatterChartMatrix>
{
    /**
     * A transform, used internally for painting
     */
    private static final AffineTransform TEMP_TRANSFORM = new AffineTransform();
    
    /**
     * The paint that should be used for the border of the charts.
     * If this is <code>null</code>, then no borders will be painted.
     */
    private Paint borderPaint = null;
    
    private double borderSpacingTop = 0.08;
    private double borderSpacingBottom = 0.025;
    private double borderSpacingLeft = 0.025;
    private double borderSpacingRight = 0.025;
    
    /**
     * The {@link LabelPainter} that will be used for the labels
     */
    private final LabelPainter labelPainter; 
    
    /**
     * The {@link ScatterChartPainter} that will be used for the charts
     */
    private final ScatterChartPainter scatterChartPainter;
    
    private final GeneralLabelPainterPredicate generalLabelPainterPredicate;
    
    /**
     * Default constructor
     */
    public ScatterChartMatrixPainter()
    {
        this.labelPainter = new LabelPainter();
        this.labelPainter.setLabelAnchor(0, 0);
        this.labelPainter.setLabelLocation(borderSpacingLeft,0);
        this.labelPainter.setPaint(Color.BLACK);
        this.labelPainter.setFont(
            new Font("Dialog", Font.PLAIN, 9).deriveFont(0.06f));
        this.labelPainter.setTransformingLabels(true);
        this.generalLabelPainterPredicate = new GeneralLabelPainterPredicate();
        this.labelPainter.setLabelPaintingCondition(
            generalLabelPainterPredicate);
        this.scatterChartPainter = new ScatterChartPainter();
    }
    
    /**
     * Returns the {@link ScatterChartPainter} that is used for the charts
     * 
     * @return The {@link ScatterChartPainter}
     */
    public ScatterChartPainter getScatterChartPainter()
    {
        return scatterChartPainter;
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, ScatterChartMatrix scatterChartMatrix)
    {
        if (scatterChartMatrix == null)
        {
            return;
        }
//        g.setColor(Color.GREEN);
//        g.draw(worldToScreen.createTransformedShape(
//            new Rectangle2D.Double(0, 0, 1, 1)));

        int n = scatterChartMatrix.getNumCharts();
        double cellSize = 1.0 / n;
        double chartScaleX = 1.0 - borderSpacingLeft - borderSpacingRight;
        double chartScaleY = 1.0 - borderSpacingTop - borderSpacingBottom;
        for (int r=0; r<n; r++)
        {
            for (int c=0; c<n; c++)
            {
                ScatterChart scatterChart = scatterChartMatrix.getChart(r, c);
                if (scatterChart != null)
                {
                    //scatterChartPainter.setPaintingAxisX(r == n - 1);
                    //scatterChartPainter.setPaintingAxisY(c == 0);

                    TEMP_TRANSFORM.setTransform(worldToScreen);
                    TEMP_TRANSFORM.translate(c * cellSize, r * cellSize);
                    TEMP_TRANSFORM.scale(cellSize, cellSize);
                    
                    String label = scatterChartMatrix.getLabel(r, c);
                    if (label != null)
                    {
                        double availableScreenSpaceY = 
                            AffineTransforms.computeDistanceY(
                                TEMP_TRANSFORM, borderSpacingTop);
                        generalLabelPainterPredicate.setMaximumScreenHeight(
                            availableScreenSpaceY);
                        labelPainter.paint(g, TEMP_TRANSFORM, w, h, label);
                    }
                    
                    TEMP_TRANSFORM.translate(
                        borderSpacingLeft, borderSpacingTop);
                    TEMP_TRANSFORM.scale(chartScaleX, chartScaleY);
                    
                    g.setColor(Color.LIGHT_GRAY);
                    g.draw(TEMP_TRANSFORM.createTransformedShape(
                        new Rectangle2D.Double(0, 0, 1, 1)));
                    
                    Rectangle2D bounds = 
                        ScatterCharts.computeBounds(scatterChart);
                    AffineTransform at = 
                        AffineTransforms.getScaleInstance(bounds, null); 
                    AffineTransforms.invert(at, at);
                    TEMP_TRANSFORM.concatenate(at);
                    
                    scatterChartPainter.paint(
                        g, TEMP_TRANSFORM, w, h, scatterChart);
                    
                }
            }
        }
        
    }


    
    
    
}