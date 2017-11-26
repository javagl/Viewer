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

import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Interface for classes that may provide contents for the legend that
 * is displayed in a {@link FunctionPanel}
 */
public interface LegendProvider
{
    /**
     * Returns the strings that should be painted as the legend for
     * the given function. Each string of the resulting list will
     * be displayed in one line.
     * 
     * @param function The function
     * @param screenX The current x-coordinate of the mouse on the screen 
     * @param screenY The current y-coordinate of the mouse on the screen
     * @param worldX The current x-coordinate of the mouse in the world
     * @param worldY The current y-coordinate of the mouse in the world
     * @return The list of strings for the legend
     */
    List<String> getLegend(DoubleFunction<? extends Number> function, 
        int screenX, int screenY, double worldX, double worldY);
}