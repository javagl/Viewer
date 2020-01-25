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

import java.awt.BasicStroke;
import java.awt.Stroke;

import de.javagl.selection.SelectionModel;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.BasicScatterChart;
import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.SelectionHandler;
import de.javagl.viewer.selection.SelectionHandlers;
import de.javagl.viewer.selection.ShapeBasedSelector;

/**
 * Convenience class to connect a viewer and a {@link ScatterChart} to 
 * a selection model.<br>
 * <br>
 * This class is mainly used internally, and should not be considered to
 * be part of the public API. 
 */
public class ScatterChartSelectionHandlers
{
    /**
     * Connect the given viewer and the given {@link ScatterChart} to the
     * given selection model. <br>
     * <br>
     * This method establishes some unspecified defaults: It enables selecting 
     * the scatter chart points via clicks or a lasso selection, and indicates 
     * selected elements in the chart with a thick stroke (while assuming that 
     * the stroke paint for the chart is not <code>null</code>).
     * <br> 
     * None of the parameters may be <code>null</code>. 
     * 
     * @param viewer The viewer
     * @param scatterChart The {@link ScatterChart}
     * @param selectionModel The selection model
     * @return The {@link ScatterChartSelectionHandler}
     */
    public static ScatterChartSelectionHandler createDefault(
        Viewer viewer, BasicScatterChart scatterChart, 
        SelectionModel<Integer> selectionModel)
    {
        // Set the function that determines the stroke for painting
        // the scatter chart points: The selected points will have
        // a border (unselected points won't have a border)
        Stroke selectedStroke = new BasicStroke(3.0f);
        scatterChart.setDrawStrokeFunction(i -> 
        {
            if (selectionModel.isSelected(i))
            {
                return selectedStroke;
            }
            return null;
        });
        
        ScatterChartSelector scatterChartSelector = new ScatterChartSelector();
        scatterChartSelector.setScatterChart(scatterChart);
        
        // The selector implements both selector interfaces.
        // Make this clear by assigning it to respective variables
        PointBasedSelector<Integer> pointBasedSelector = scatterChartSelector;
        ShapeBasedSelector<Integer> shapeBasedSelector = scatterChartSelector;

        // Create a selection handler for clicks, and use it to connect
        // the viewer and the selection model
        SelectionHandler<Integer> clickSelectionHandler = 
            SelectionHandlers.createClick(pointBasedSelector);
        clickSelectionHandler.connect(viewer, selectionModel);
        
        // Create a selection handler for a lasso selection, and use it to 
        // connect the viewer and the selection model
        SelectionHandler<Integer> lassoSelectionHandler = 
            SelectionHandlers.createLasso(shapeBasedSelector);
        lassoSelectionHandler.connect(viewer, selectionModel);
        
        return new ScatterChartSelectionHandler()
        {
            @Override
            public void disconnect()
            {
                clickSelectionHandler.disconnect();
                lassoSelectionHandler.disconnect();
            }
        };
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ScatterChartSelectionHandlers()
    {
        // Private constructor to prevent instantiation
    }
    
}
