/*
 * www.javagl.de - Viewer - Glyphs
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
package de.javagl.viewer.glyphs;


/**
 * Interface for a simple box plot.
 */
public interface BoxPlot
{
    /**
     * Returns the minimum value that appears in the data set. This will 
     * determine the lower end of the box plot
     * 
     * @return The minimum
     */
    double getMinimum();

    /**
     * Returns the lower quantile value. This will determine the lower whisker
     * position of the box plot
     * 
     * @return The lower quantile
     */
    double getLowerQuantile();
    
    /**
     * Returns the median. 
     * 
     * @return The media
     */
    double getMedian();

    /**
     * Returns the upper quantile value. This will determine the upper whisker
     * position of the box plot
     * 
     * @return The upper quantile
     */
    double getUpperQuantile();

    /**
     * Returns the maximum value that appears in the data set. This will 
     * determine the upper end of the box plot
     * 
     * @return The maximum
     */
    double getMaximum();
    
    /**
     * Returns the mean.
     * 
     * @return The mean
     */
    double getMean();
}
