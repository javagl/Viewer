/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2020 Marco Hutter - http://www.javagl.de
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
package de.javagl.viewer.glyphs.selection;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.glyphs.ScatterChartMatrix;
import de.javagl.viewer.glyphs.ScatterCharts;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.ShapeBasedSelector;

/**
 * Implementation of a class that determines elements that are selected
 * in a {@link ScatterChartMatrix}, and returns the indices of the selected
 * points.
 */
public class ScatterChartMatrixSelector 
    implements PointBasedSelector<Integer>, ShapeBasedSelector<Integer>
{
    /**
     * The {@link ScatterChartMatrix} for the selection
     */
    private ScatterChartMatrix scatterChartMatrix;
    
    /**
     * The {@link ScatterChartSelector}
     */
    private final ScatterChartSelector scatterChartSelector;
    
    /**
     * Creates a new instance
     */
    public ScatterChartMatrixSelector()
    {
        this.scatterChartSelector = new ScatterChartSelector();
    }
    
    /**
     * Set the {@link ScatterChartMatrix} for the selection, or 
     * <code>null</code> if no selection should be possible
     * 
     * @param scatterChartMatrix The {@link ScatterChartMatrix}
     */
    public void setScatterChartMatrix(ScatterChartMatrix scatterChartMatrix)
    {
        this.scatterChartMatrix = scatterChartMatrix;
    }
    
    @Override
    public Collection<Integer> computeElementsForShape(Shape shape,
        AffineTransform worldToScreen)
    {
        return computeElements(null, shape, worldToScreen);
    }

    @Override
    public Collection<Integer> computeElementsForPoint(Point2D point,
        AffineTransform worldToScreen)
    {
        return computeElements(point, null, worldToScreen);
    }

    /**
     * Implementation of the interface methods. 
     * 
     * TODO This is not very elegant, and should be refactored and commented!
     * 
     * @param point The point
     * @param shape The shape
     * @param worldToScreen The world-to-screen transform
     * @return The elements
     */
    private Collection<Integer> computeElements(
        Point2D point, Shape shape, AffineTransform worldToScreen)
    {
        if (scatterChartMatrix == null)
        {
            return Collections.emptySet();
        }
        Set<Integer> indices = new LinkedHashSet<Integer>();
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
                
                AffineTransform cellTransform = new AffineTransform();
                cellTransform.setTransform(worldToScreen);
                cellTransform.translate(c * cellSize, r * cellSize);
                cellTransform.scale(cellSize, cellSize);
                cellTransform.translate(
                    cellBounds.getX(), cellBounds.getY());
                cellTransform.scale(
                    cellBounds.getWidth(), cellBounds.getHeight());
                Rectangle2D bounds = 
                    ScatterCharts.computeBounds(scatterChart);
                cellTransform.scale(
                    1.0 / bounds.getWidth(), 
                    -1.0 / bounds.getHeight());
                cellTransform.translate(
                    -bounds.getMinX(), -bounds.getMaxY());
                
                scatterChartSelector.setScatterChart(scatterChart);
                if (point != null)
                {
                    Collection<Integer> cellElements = 
                        scatterChartSelector.computeElementsForPoint(
                            point, cellTransform);
                    indices.addAll(cellElements);
                }
                if (shape != null)
                {
                    Collection<Integer> cellElements = 
                        scatterChartSelector.computeElementsForShape(
                            shape, cellTransform);
                    indices.addAll(cellElements);
                }
            }
        }
        return indices;
    }
    
}
