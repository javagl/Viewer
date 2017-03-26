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

import java.util.function.Predicate;

import de.javagl.viewer.painters.LabelPainter.LabelPaintState;

/**
 * Methods to create predicates that determine whether labels should be
 * painted in a {@link LabelPainter}, based on a {@link LabelPaintState}.
 */
public class LabelPainterPredicates
{
    /**
     * Returns a new {@link GeneralLabelPainterPredicate} that allows setting
     * the painting conditions for the labels based on the size of the label
     * in world- and screen coordinates.
     * 
     * @return The {@link GeneralLabelPainterPredicate}
     */
    public static GeneralLabelPainterPredicate create()
    {
        return new GeneralLabelPainterPredicate();
    }
    
    /**
     * Returns a predicate that causes labels to be painted when their 
     * (untransformed) width is not smaller than the given minimum
     * 
     * @param minimumLabelWidth The maximum label width
     * @return The predicate
     */
    public static Predicate<LabelPaintState> labelWidthAtLeast(
        double minimumLabelWidth)
    {
        GeneralLabelPainterPredicate predicate = 
            new GeneralLabelPainterPredicate();
        predicate.setMinimumWorldWidth(minimumLabelWidth);
        return predicate;
    }

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
        GeneralLabelPainterPredicate predicate = 
            new GeneralLabelPainterPredicate();
        predicate.setMaximumWorldWidth(maximumLabelWidth);
        return predicate;
    }
    
    /**
     * Returns a predicate that causes labels to be painted when their 
     * (transformed) width is at least the given minimum.
     * 
     * @param minimumTransformedLabelWidth The minimum label width
     * @return The predicate
     */
    public static Predicate<LabelPaintState> transformedLabelWidthAtLeast(
        double minimumTransformedLabelWidth)
    {
        GeneralLabelPainterPredicate predicate = 
            new GeneralLabelPainterPredicate();
        predicate.setMinimumScreenWidth(minimumTransformedLabelWidth);
        return predicate;
    }
    
    /**
     * Returns a predicate that causes labels to be painted when their 
     * (transformed) width is at most the given maximum.
     * 
     * @param maximumTransformedLabelWidth The minimum label width
     * @return The predicate
     */
    public static Predicate<LabelPaintState> transformedLabelWidthAtMost(
        double maximumTransformedLabelWidth)
    {
        GeneralLabelPainterPredicate predicate = 
            new GeneralLabelPainterPredicate();
        predicate.setMaximumScreenWidth(maximumTransformedLabelWidth);
        return predicate;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private LabelPainterPredicates()
    {
        // Private constructor to prevent instantiation
    }
}
