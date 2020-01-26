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

import de.javagl.viewer.glyphs.BoxPlot;
import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.selection.SelectionHandler;

/**
 * Extension of a {@link SelectionHandler} that handles the selection in
 * a glyph, for example, in a {@link ScatterChart} or {@link BoxPlot}.
 * 
 * @param <G> The glyph type
 * @param <T> The type of the selected elements
 */
public interface GlyphSelectionHandler<G, T> extends SelectionHandler<T>
{
    /**
     * Set the glyph for which the selection should be handled
     * 
     * @param glyph The glyph
     */
    void setGlyph(G glyph);
}
