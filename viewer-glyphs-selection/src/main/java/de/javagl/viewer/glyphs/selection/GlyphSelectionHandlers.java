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

import de.javagl.selection.SelectionModel;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.glyphs.ScatterChartMatrix;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.SelectionHandler;
import de.javagl.viewer.selection.SelectionHandlers;
import de.javagl.viewer.selection.ShapeBasedSelector;

/**
 * Convenience class to create {@link GlyphSelectionHandler} instances.<br>
 * <br>
 */
public class GlyphSelectionHandlers
{
    /**
     * Create a new {@link GlyphSelectionHandler}. The handler will
     * allow to {@link SelectionHandler#connect(Viewer, SelectionModel)
     * connect} a viewer to a selection model, and may receive the 
     * {@link ScatterChart} for which the selection should be handled.<br> 
     * <br>
     * This method establishes some unspecified defaults: It enables selecting 
     * the scatter chart points via clicks or a lasso selection.
     * 
     * @return The {@link GlyphSelectionHandler}
     */
    public static GlyphSelectionHandler<ScatterChart, Integer> 
        createDefaultScatterChart()
    {
        ScatterChartSelector scatterChartSelector = new ScatterChartSelector();
        
        // The selector implements both selector interfaces.
        // Make this clear by assigning it to respective variables
        PointBasedSelector<Integer> pointBasedSelector = scatterChartSelector;
        ShapeBasedSelector<Integer> shapeBasedSelector = scatterChartSelector;

        // Create a selection handler for clicks, and use it to connect
        // the viewer and the selection model
        SelectionHandler<Integer> clickSelectionHandler = 
            SelectionHandlers.createClick(pointBasedSelector);
        
        // Create a selection handler for a lasso selection, and use it to 
        // connect the viewer and the selection model
        SelectionHandler<Integer> lassoSelectionHandler = 
            SelectionHandlers.createLasso(shapeBasedSelector);
        
        return new GlyphSelectionHandler<ScatterChart, Integer>()
        {
            @Override
            public void disconnect()
            {
                clickSelectionHandler.disconnect();
                lassoSelectionHandler.disconnect();
            }

            @Override
            public void connect(Viewer viewer,
                SelectionModel<Integer> selectionModel)
            {
                clickSelectionHandler.connect(viewer, selectionModel);
                lassoSelectionHandler.connect(viewer, selectionModel);
            }

            @Override
            public void setGlyph(ScatterChart glyph)
            {
                scatterChartSelector.setScatterChart(glyph);
            }
        };
    }

    /**
     * Create a new {@link GlyphSelectionHandler}. The handler will
     * allow to {@link SelectionHandler#connect(Viewer, SelectionModel)
     * connect} a viewer to a selection model, and may receive the 
     * {@link ScatterChartMatrix} for which the selection should be 
     * handled.<br> 
     * <br>
     * This method establishes some unspecified defaults: It enables selecting 
     * the scatter chart points via clicks or a lasso selection.
     * 
     * @return The {@link GlyphSelectionHandler}
     */
    public static GlyphSelectionHandler<ScatterChartMatrix, Integer> 
        createDefaultScatterChartMatrix()
    {
        ScatterChartMatrixSelector scatterChartMatrixSelector = 
            new ScatterChartMatrixSelector();
        
        // The selector implements both selector interfaces.
        // Make this clear by assigning it to respective variables
        PointBasedSelector<Integer> pointBasedSelector = 
            scatterChartMatrixSelector;
        ShapeBasedSelector<Integer> shapeBasedSelector = 
            scatterChartMatrixSelector;

        // Create a selection handler for clicks, and use it to connect
        // the viewer and the selection model
        SelectionHandler<Integer> clickSelectionHandler = 
            SelectionHandlers.createClick(pointBasedSelector);
        
        // Create a selection handler for a lasso selection, and use it to 
        // connect the viewer and the selection model
        SelectionHandler<Integer> lassoSelectionHandler = 
            SelectionHandlers.createLasso(shapeBasedSelector);
        
        return new GlyphSelectionHandler<ScatterChartMatrix, Integer>()
        {
            @Override
            public void disconnect()
            {
                clickSelectionHandler.disconnect();
                lassoSelectionHandler.disconnect();
            }

            @Override
            public void connect(Viewer viewer,
                SelectionModel<Integer> selectionModel)
            {
                clickSelectionHandler.connect(viewer, selectionModel);
                lassoSelectionHandler.connect(viewer, selectionModel);
            }

            @Override
            public void setGlyph(ScatterChartMatrix glyph)
            {
                scatterChartMatrixSelector.setScatterChartMatrix(glyph);
            }
        };
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private GlyphSelectionHandlers()
    {
        // Private constructor to prevent instantiation
    }
    
}
