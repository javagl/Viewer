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
import java.util.Collection;
import java.util.Collections;

import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.Selectors;
import de.javagl.viewer.selection.ShapeBasedSelector;

/**
 * Implementation of a class that determines elements that are selected
 * in a {@link ScatterChart}, and returns the indices of the selected
 * points.
 */
class ScatterChartSelector 
    implements PointBasedSelector<Integer>, ShapeBasedSelector<Integer>
{
    /**
     * The {@link ScatterChart} for the selection
     */
    private ScatterChart scatterChart;
    
    /**
     * Creates a new instance
     */
    ScatterChartSelector()
    {
        // Default constructor
    }
    
    /**
     * Set the {@link ScatterChart} for the selection, or <code>null</code>
     * if no selection should be possible
     * 
     * @param scatterChart The {@link ScatterChart}
     */
    void setScatterChart(ScatterChart scatterChart)
    {
        this.scatterChart = scatterChart;
    }
    
    @Override
    public Collection<Integer> computeElementsForShape(Shape shape,
        AffineTransform worldToScreen)
    {
        if (scatterChart == null)
        {
            return Collections.emptySet();
        }
        return Selectors.computePointIndicesForShape(
            shape, worldToScreen, scatterChart.getNumPoints(), 
            scatterChart::getPointX,
            scatterChart::getPointY);
    }

    @Override
    public Collection<Integer> computeElementsForPoint(Point2D point,
        AffineTransform worldToScreen)
    {
        if (scatterChart == null)
        {
            return Collections.emptySet();
        }
        return Selectors.computeShapeIndicesForPoint(point, worldToScreen, 
            scatterChart.getNumPoints(), 
            scatterChart::getPointX,
            scatterChart::getPointY,
            scatterChart::getShape);
    }

}
