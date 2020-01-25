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
import de.javagl.viewer.painters.CoordinateSystemPainter;
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
    private static final AffineTransform TEMP_TRANSFORM = 
        new AffineTransform();
    
    /**
     * A unit rectangle
     */
    private static final Rectangle2D UNIT_RECTANGLE = 
        new Rectangle2D.Double(0,0,1,1);
    
    /**
     * The paint that should be used for the border of the charts.
     * If this is <code>null</code>, then no borders will be painted.
     */
    private Paint borderPaint = Color.GRAY;

    /**
     * The {@link LabelPainter} that will be used for the labels
     */
    private final LabelPainter labelPainter; 
    
    /**
     * The {@link ScatterChartPainter} that will be used for the charts
     */
    private final ScatterChartPainter scatterChartPainter;
    
    /**
     * The predicate that will determine whether the labels for the
     * scatter charts will be painted
     */
    private final GeneralLabelPainterPredicate generalLabelPainterPredicate;
    
    /**
     * Whether a {@link CoordinateSystemPainter} should be used to paint
     * coordinate axes along the outer borders of the scatter charts
     */
    private boolean showingDefaultCoordinateSystems = true;
    
    /**
     * Default constructor
     */
    public ScatterChartMatrixPainter()
    {
        this.labelPainter = new LabelPainter();
        this.labelPainter.setLabelAnchor(0, 0);
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
     * Set whether a {@link CoordinateSystemPainter} should be used to paint
     * coordinate axes along the outer borders of the scatter charts
     * 
     * @param showingDefaultCoordinateSystems The painting state
     */
    public void setShowingDefaultCoordinateSystems(
        boolean showingDefaultCoordinateSystems)
    {
        this.showingDefaultCoordinateSystems = showingDefaultCoordinateSystems;
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
        
        CoordinateSystemPainter coordinateSystemPainter = null;
        if (showingDefaultCoordinateSystems)
        {
            coordinateSystemPainter = new CoordinateSystemPainter();
            coordinateSystemPainter.setGridColorX(null);
            coordinateSystemPainter.setGridColorY(null);
        }

        int n = scatterChartMatrix.getNumCharts();
        double cellSize = 1.0 / n;
        for (int r = 0; r < n; r++)
        {
            for (int c = 0; c < n; c++)
            {
                ScatterChart scatterChart = scatterChartMatrix.getChart(r, c);
                if (scatterChart == null)
                {
                    continue;
                }
                Rectangle2D cellBounds = 
                    scatterChartMatrix.getRelativeCellBounds(r, c);

                TEMP_TRANSFORM.setTransform(worldToScreen);
                TEMP_TRANSFORM.translate(c * cellSize, r * cellSize);
                TEMP_TRANSFORM.scale(cellSize, cellSize);

                String label = scatterChartMatrix.getLabel(r, c);
                if (label != null)
                {
                    this.labelPainter.setLabelLocation(cellBounds.getX(), 0);
                    double availableScreenSpaceY = 
                        AffineTransforms.computeDistanceY(
                            TEMP_TRANSFORM, cellBounds.getY());
                    generalLabelPainterPredicate.setMaximumScreenHeight(
                        availableScreenSpaceY);
                    labelPainter.paint(g, TEMP_TRANSFORM, w, h, label);
                }

                TEMP_TRANSFORM.translate(
                    cellBounds.getX(), cellBounds.getY());
                TEMP_TRANSFORM.scale(
                    cellBounds.getWidth(), cellBounds.getHeight());

                if (borderPaint != null)
                {
                    g.setPaint(borderPaint);
                    g.draw(TEMP_TRANSFORM.createTransformedShape(
                        UNIT_RECTANGLE));
                }

                
                // Compute the transform to bring the whole scatter
                // chart into the current grid cell
                Rectangle2D bounds = 
                    ScatterCharts.computeBounds(scatterChart);
                sanitize(bounds);
                TEMP_TRANSFORM.scale(
                    1.0 / bounds.getWidth(), 
                    -1.0 / bounds.getHeight());
                TEMP_TRANSFORM.translate(
                    -bounds.getMinX(), -bounds.getMaxY());
                
                if (showingDefaultCoordinateSystems)
                {
                    // Set the axis configuration of the coordinate system 
                    // painter to display the range for the scatter chart
                    coordinateSystemPainter.setAxisLocationY(bounds.getMinX());
                    coordinateSystemPainter.setAxisLocationX(bounds.getMinY());
                    coordinateSystemPainter.setAxisRangeX(
                        bounds.getMinX(), bounds.getMaxX());
                    coordinateSystemPainter.setAxisRangeY(
                        bounds.getMinY(), bounds.getMaxY());
    
                    if (c == 0)
                    {
                        coordinateSystemPainter.setAxisColorY(Color.GRAY);
                    }
                    else
                    {
                        coordinateSystemPainter.setAxisColorY(null);
                    }
                    if (r == (n-1))
                    {
                        coordinateSystemPainter.setAxisColorX(Color.GRAY);
                    }
                    else
                    {
                        coordinateSystemPainter.setAxisColorX(null);
                    }
                    coordinateSystemPainter.paint(g, TEMP_TRANSFORM, w, h);
                }
                
                scatterChartPainter.paint(
                    g, TEMP_TRANSFORM, w, h, scatterChart);

            }
        }
    }
        
    /**
     * Sanitize the given rectangle for scaling operations. If the width or 
     * the height of the given rectangle is smaller than a small epsilon,
     * then it will be set to 1.0, respectively
     * 
     * @param r The rectangle
     */
    private static void sanitize(Rectangle2D r)
    {
        final double epsilon = 1e-8;
        if (r.getWidth() < epsilon)
        {
            r.setRect(r.getX(), r.getY(), 1.0, r.getHeight());
        }
        if (r.getHeight() < epsilon)
        {
            r.setRect(r.getX(), r.getY(),r.getWidth(), 1.0);
        }
    }
}
