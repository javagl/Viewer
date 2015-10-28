/*
 * www.javagl.de - Viewer
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
package de.javagl.viewer.painters;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.Predicate;

import de.javagl.geom.AffineTransforms;
import de.javagl.viewer.painters.LabelPainter.LabelPaintState;

/**
 * Methods to create predicates that determine whether labels should be
 * painted in a {@link LabelPainter}, based on a {@link LabelPaintState}.
 */
public class LabelPainterPredicates
{
    /**
     * Returns a predicate that causes labels to be painted when their 
     * (untransformed) width is not greater than the given maximum
     * 
     * @param maximumLabelWidth The maximum label width
     * @return The predicate
     */
    public static Predicate<LabelPaintState> labelWidthAtMost(
        double maximumLabelWidth)
    {
        return new Predicate<LabelPaintState>()
        {
            @Override
            public boolean test(LabelPaintState labelPaintState)
            {
                Rectangle2D labelBounds = labelPaintState.getLabelBounds();
                return labelBounds.getWidth() <= maximumLabelWidth;
            }
        };
    }
    
    /**
     * Returns a predicate that causes labels to be painted when their 
     * (transformed) width is at least the given minimum.
     * 
     * @param minimumTransformedLabelWidth The maximum label width
     * @return The predicate
     */
    public static Predicate<LabelPaintState> transformedLabelWidthAtLeast(
        double minimumTransformedLabelWidth)
    {
        return new Predicate<LabelPaintState>()
        {
            @Override
            public boolean test(LabelPaintState labelPaintState)
            {
                Rectangle2D labelBounds = labelPaintState.getLabelBounds();
                AffineTransform affineTransform = 
                    labelPaintState.getAffineTransform();
                double transformedLabelWidth = 
                    AffineTransforms.computeDistanceX(
                        affineTransform, labelBounds.getWidth());
                return transformedLabelWidth >= minimumTransformedLabelWidth;
            }
        };
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private LabelPainterPredicates()
    {
        // Private constructor to prevent instantiation
    }
}
