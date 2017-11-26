/*
 * www.javagl.de - Viewer - Functions
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
package de.javagl.viewer.functions;

import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

import de.javagl.geom.Points;
import de.javagl.viewer.MouseControls;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.painters.CoordinateSystemPainter;

/**
 * A panel that paints functions
 */
public class FunctionPanel extends Viewer
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -7414790459688389145L;

    /**
     * The functions that are currently painted
     */
    private final List<DoubleFunction<? extends Number>> functions;
    
    /**
     * The {@link FunctionPainter}s for the functions
     */
    private final List<FunctionPainter> functionPainters; 

    /**
     * The {@link LegendProvider}s for the functions, which provide the
     * strings that will be painted by the {@link LegendPainter}
     */
    private final List<LegendProvider> legendProviders;
    
    /**
     * The {@link LegendPainter} 
     */
    private final LegendPainter legendPainter;
    
    /**
     * Default constructor. This will create a function panel with
     * {@link MouseControls#createDefault(Viewer, boolean, boolean)
     * default mouse controls}, where non-uniform scaling is allowed,
     * but rotation is not allowed. The viewer will be 
     * {@link #setFlippedVertically(boolean) flipped vertically} 
     * and {@link #setMaintainAspectRatio(boolean) not maintain the 
     * aspect ratio}. 
     */
    public FunctionPanel()
    {
        setMouseControl(MouseControls.createDefault(this, false, true));
        setFlippedVertically(true);
        setMaintainAspectRatio(false);

        CoordinateSystemPainter coordinateSystemPainter = 
            new CoordinateSystemPainter();
        addPainter(coordinateSystemPainter);
        
        this.functions = new ArrayList<DoubleFunction<? extends Number>>();
        this.functionPainters = new ArrayList<FunctionPainter>();
        this.legendProviders = new ArrayList<LegendProvider>();

        this.legendPainter = new LegendPainter();
        addPainter(legendPainter, 1);
        
        
        addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                updateLegend(e.getX(), e.getY());
            }
        });
    }

    /**
     * Add the given function
     * 
     * @param function The function that should be painted
     * @param paint The paint (color)
     * @throws NullPointerException If the function or the paint is 
     * <code>null</code>
     */
    public final void addFunction(
        DoubleFunction<? extends Number> function, Paint paint)
    {
        addFunction(function, paint, (LegendProvider)null);
    }
    
    /**
     * Add the given function 
     * 
     * @param function The function that should be painted
     * @param paint The paint (color)
     * @param name The name of the function to appear in the legend
     * @throws NullPointerException If the function or the paint is 
     * <code>null</code>
     */
    public final void addFunction(
        DoubleFunction<? extends Number> function, Paint paint, String name)
    {
        addFunction(function, paint, LegendProviders.createDefault(name));
    }
    

    /**
     * Add the given function, with a 
     * {@link LegendProviders#createFunctionValuesLegendProvider(String)
     * legend provider} that provides the function values for the mouse
     * position.
     * 
     * @param function The function that should be painted
     * @param paint The paint (color)
     * @param name The name of the function to appear in the legend
     * @throws NullPointerException If the function or the paint is 
     * <code>null</code>
     */
    public final void addFunctionWithValueLegend(
        DoubleFunction<? extends Number> function, Paint paint, String name)
    {
        addFunction(function, paint, 
            LegendProviders.createFunctionValuesLegendProvider(name));
    }
    
    

    /**
     * Add the given function 
     * 
     * @param function The function that should be painted
     * @param paint The paint (color)
     * @param legendProvider The {@link LegendProvider}. This may be 
     * <code>null</code> if no legend should be painted for the given
     * function
     * @throws NullPointerException If the function or the paint is 
     * <code>null</code>
     */
    public final void addFunction(
        DoubleFunction<? extends Number> function, Paint paint,
        LegendProvider legendProvider)
    {
        if (function == null)
        {
            throw new NullPointerException("The function is null");
        }
        if (paint == null)
        {
            throw new NullPointerException("The paint is null");
        }
        functions.add(function);
        FunctionPainter functionPainter = 
            new FunctionPainter(function, paint);
        addPainter(functionPainter);
        functionPainters.add(functionPainter);
        legendProviders.add(legendProvider);
        updateLegend(0,0);
        repaint();
    }
    
    /**
     * Remove the given function 
     * 
     * @param function The function
     * @return Whether the function was contained in this panel
     */
    public final boolean removeFunction(
        DoubleFunction<? extends Number> function)
    {
        int index = functions.indexOf(function);
        if (index == -1)
        {
            return false;
        }
        functions.remove(index);
        FunctionPainter functionPainter = functionPainters.get(index);
        removePainter(functionPainter);
        functionPainters.remove(index);
        legendProviders.remove(index);
        updateLegend(0,0);
        repaint();
        return true;
    }
    
    /**
     * Removes all functions from this panel
     */
    public final void clearFunctions()
    {
        functions.clear();
        for (FunctionPainter functionPainter : functionPainters)
        {
            removePainter(functionPainter);
        }
        functionPainters.clear();
        legendProviders.clear();
        updateLegend(0,0);
        repaint();
    }
    
    
    
    /**
     * Update the legend for the given (mouse) coordinates 
     * 
     * @param sx The x-coordinate on the screen
     * @param sy The y-coordinate on the screen
     */
    private void updateLegend(int sx, int sy)
    {
        Point2D worldPoint = Points.inverseTransform(
            getWorldToScreen(), new Point(sx,sy), null);

        legendPainter.clearStrings();
        for (int i=0; i<functions.size(); i++)
        {
            DoubleFunction<? extends Number> function = functions.get(i);
            FunctionPainter functionPainter = functionPainters.get(i);
            LegendProvider legendProvider = legendProviders.get(i);
            if (legendProvider != null)
            {
                double wx = worldPoint.getX();
                double wy = worldPoint.getY();
                List<String> strings = 
                    legendProvider.getLegend(function, sx, sy, wx, wy);
                if (strings != null)
                {
                    Paint paint = functionPainter.getPaint();
                    for (String string : strings)
                    {
                        legendPainter.addString(string, paint);
                    }
                }
            }
        }
        repaint();
    }
    
    /**
     * Set the prototype legend string that determines the width of the legend.
     * If this is <code>null</code>, then only the actual strings will be used.
     * 
     * @param s The prototype legend string
     */
    public final void setPrototypeLegendString(String s)
    {
        legendPainter.setPrototypeString(s);
    }

    /**
     * Auto-fit to show the functions in the given interval. This is
     * equivalent to calling {@link #fit(double, double, double, double, 
     * boolean) fit(xMin,xMax,0.0,0.1,false)}
     *  
     * @param xMin The minimum x-value 
     * @param xMax The maximum x-value
     */
    public final void fit(double xMin, double xMax)
    {
        fit(xMin, xMax, 0.0, 0.1, false);
    }
    
    /**
     * Auto-fit to show the functions in the given interval. <br>
     * <br>
     * This will compute an <b>estimate</b> of the minimum and maximum 
     * y-values that all functions have in the given interval. Note
     * that this <b>estimate</b> may not be precise if the functions
     * contain singularities.<br>
     * <br> 
     * If functions return <code>null</code>, NaN or an infinite value, 
     * these will be ignored. If all values returned by all functions 
     * are <code>null</code>, NaN or infinite, then 0.0 will be used 
     * as the minimum value, or 1.0 for the maximum value, respectively. <br>
     * <br>
     * The given x-values, and the minimum- and maximum y-values will 
     * be used to {@link #setDisplayedWorldArea(Rectangle2D) set the 
     * displayed world area} with the specified margins, optionally
     * maintaining the current aspect ratio. 
     * 
     * @param xMin The minimum x-value 
     * @param xMax The maximum x-value
     * @param marginX The relative margin in x-direction
     * @param marginY The relative margin in y-direction
     * @param maintainAspectRatio Whether the current aspect ratio should
     * be maintained while fitting
     */
    public final void fit(double xMin, double xMax, 
        double marginX, double marginY, boolean maintainAspectRatio) 
    {
        final double epsilon = 1e-6; 
        Number yMin = null;
        Number yMax = null;
        for (DoubleFunction<? extends Number> function : functions)
        {
            Number min = FunctionUtils.estimateMinValue(function, xMin, xMax);
            Number max = FunctionUtils.estimateMaxValue(function, xMin, xMax);
            if (min != null && Double.isFinite(min.doubleValue()))
            {
                yMin = FunctionUtils.min(yMin, min);
            }
            if (max != null && Double.isFinite(max.doubleValue()))
            {
                yMax = FunctionUtils.max(yMax, max);
            }
        }
        if (yMin == null || !Double.isFinite(yMin.doubleValue()))
        {
            yMin = 0.0;
        }
        if (yMax == null || !Double.isFinite(yMax.doubleValue()))
        {
            yMax = 1.0;
        }
        double dx = xMax - xMin;
        double dy = yMax.doubleValue() - yMin.doubleValue();

        double x = xMin - marginX * dx;
        double y = yMin.doubleValue() - marginY * dy;
        double w = dx + marginX * dx * 2.0;
        double h = dy + marginY * dy * 2.0;
        h = Math.max(h, epsilon);
        Rectangle2D worldArea = new Rectangle2D.Double(x, -h-y, w, h);
        boolean oldMaintainAspectRatioState = isMaintainAspectRatio();
        setMaintainAspectRatio(maintainAspectRatio);
        setDisplayedWorldArea(worldArea);
        setMaintainAspectRatio(oldMaintainAspectRatioState);
        repaint();
    }
    
    
}