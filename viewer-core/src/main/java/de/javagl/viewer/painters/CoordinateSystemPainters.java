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

import java.awt.Color;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.DoubleFunction;

import de.javagl.viewer.Viewer;

/**
 * Methods related to {@link CoordinateSystemPainter} instances
 */
public class CoordinateSystemPainters
{
    /**
     * Calls {@link #createDateLabelFormatter(OffsetDateTime, Duration, 
     * DateTimeFormatter)} with a predefined (but unspecified) pattern
     * for the formatter, containing only the time
     * 
     * @param zero The zero-date
     * @param unit The time unit
     * @return The formatter
     */
    public static DoubleFunction<String> createTimeLabelFormatter(
        OffsetDateTime zero, Duration unit)
    {
        return createDateLabelFormatter(zero, unit, "HH:mm:ss.SSS");
    }

    /**
     * Calls {@link #createDateLabelFormatter(OffsetDateTime, Duration, 
     * DateTimeFormatter)} with a predefined (but unspecified) pattern
     * for the formatter, containing only the date
     * 
     * @param zero The zero-date
     * @param unit The time unit
     * @return The formatter
     */
    public static DoubleFunction<String> createDateLabelFormatter(
        OffsetDateTime zero, Duration unit)
    {
        return createDateLabelFormatter(zero, unit, "yyyy-MM-dd");
    }
    
    /**
     * Calls {@link #createDateLabelFormatter(OffsetDateTime, Duration, 
     * DateTimeFormatter)} with a predefined (but unspecified) pattern
     * for the formatter, containing the date and the time
     * 
     * @param zero The zero-date
     * @param unit The time unit
     * @return The formatter
     */
    public static DoubleFunction<String> createDateTimeLabelFormatter(
        OffsetDateTime zero, Duration unit)
    {
        return createDateLabelFormatter(zero, unit, "yyyy-MM-dd HH:mm:ss.SSS");
    }
    
    /**
     * Calls {@link #createDateLabelFormatter(OffsetDateTime, Duration, 
     * DateTimeFormatter)} with a a formatter that was created from 
     * the given pattern
     * 
     * @param zero The zero-date
     * @param unit The time unit
     * @param pattern The pattern
     * @return The formatter
     * @throws IllegalArgumentException if the pattern is invalid
     */
    public static DoubleFunction<String> createDateLabelFormatter(
        OffsetDateTime zero, Duration unit, String pattern)
    {
        DateTimeFormatter formatter = 
            DateTimeFormatter.ofPattern(pattern);
        return createDateLabelFormatter(zero, unit, formatter);
    }
    
    /**
     * Create a function that converts a <code>double</code> value 
     * <code>x</code> into a date string, by computing 
     * <code>zero + x * unit</code> and formatting the resulting
     * date with the given formatter. The resulting date will
     * be rounded to microseconds.
     *  
     * @param zero The zero-date
     * @param unit The time unit
     * @param formatter The formatter
     * @return The formatter
     */
    public static DoubleFunction<String> createDateLabelFormatter(
        OffsetDateTime zero, Duration unit, DateTimeFormatter formatter)
    {
        long unitNanos = unit.toNanos();
        long unitMicros = (unitNanos / 1000);
        return x -> 
        {
            double xMicrosRounded = Math.round(x * unitMicros);
            long xNanos = (long) (xMicrosRounded * 1000);
            Duration xOffset = Duration.ofNanos(xNanos);
            OffsetDateTime xTime = zero.plus(xOffset);
            String string = xTime.format(formatter);
            return string;
        };
    }
    
    /**
     * Create a coordinate system painter for the given viewer, using a
     * fixed screen layout, with unspecified margins
     * 
     * @param viewer The viewer
     * @return The painter
     */
    static CoordinateSystemPainter createFixed(Viewer viewer)
    {
        return createFixed(viewer, 10, 10, 10, 10);
    }
    
    /**
     * Create a coordinate system painter for the given viewer, using a
     * fixed screen layout, with the given margins
     * 
     * @param viewer The viewer
     * @param left The left margin
     * @param right The right margin
     * @param top The top margin
     * @param bottom The bottom margin
     * @return The painter
     */
    static CoordinateSystemPainter createFixed(Viewer viewer,
        int left, int right, int top, int bottom)
    {
        CoordinateSystemPainter fixedCoordinateSystemPainter =
            new CoordinateSystemPainter();

        LabelPainter labelPainterX = 
            fixedCoordinateSystemPainter.getLabelPainterX();
        labelPainterX.setPaint(Color.LIGHT_GRAY);

        LabelPainter labelPainterY = 
            fixedCoordinateSystemPainter.getLabelPainterY();
        labelPainterY.setPaint(Color.LIGHT_GRAY);
        
        fixedCoordinateSystemPainter.setAxisColorX(Color.LIGHT_GRAY);
        fixedCoordinateSystemPainter.setAxisColorY(Color.LIGHT_GRAY);
        fixedCoordinateSystemPainter.setGridColorX(null);
        fixedCoordinateSystemPainter.setGridColorY(null);
        fixedCoordinateSystemPainter.setScreenAxisLayoutX(
            () -> left, 
            () -> viewer.getWidth() - right, 
            () -> top);
        fixedCoordinateSystemPainter.setScreenAxisLayoutY(
            () -> viewer.getHeight() - bottom, 
            () -> top, 
            () -> viewer.getWidth() - right);
        return fixedCoordinateSystemPainter;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private CoordinateSystemPainters()
    {
        // Private constructor to prevent instantiation
    }
}
