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
 * General implementation of a predicate that may be assigned to a 
 * {@link LabelPainter#setLabelPaintingCondition(Predicate) LabelPainter} 
 * in order to determine whether a label should be painted. It allows 
 * setting the minimum and maximum valid size for the label, in world and 
 * in screen coordinates, respectively.
 */
public final class GeneralLabelPainterPredicate 
    implements Predicate<LabelPaintState>
{
    /**
     * The minimum width that the label may have in world coordinates
     */
    private double minimumWorldWidth = Double.NEGATIVE_INFINITY;

    /**
     * The maximum width that the label may have in world coordinates
     */
    private double maximumWorldWidth = Double.POSITIVE_INFINITY;

    /**
     * The minimum height that the label may have in world coordinates
     */
    private double minimumWorldHeight = Double.NEGATIVE_INFINITY;

    /**
     * The maximum height that the label may have in world coordinates
     */
    private double maximumWorldHeight = Double.POSITIVE_INFINITY;

    /**
     * The minimum width that the label may have in screen coordinates
     */
    private double minimumScreenWidth = Double.NEGATIVE_INFINITY;

    /**
     * The maximum width that the label may have in screen coordinates
     */
    private double maximumScreenWidth = Double.POSITIVE_INFINITY;

    /**
     * The minimum height that the label may have in screen coordinates
     */
    private double minimumScreenHeight = Double.NEGATIVE_INFINITY;

    /**
     * The maximum height that the label may have in screen coordinates
     */
    private double maximumScreenHeight = Double.POSITIVE_INFINITY;

    /**
     * Default constructor.<br>
     * <br>
     * By default all limits are set to <code>Double.NEGATIVE_INFINITY</code>
     * and <code>Double.POSITIVE_INFINITY</code>, respectively, causing all
     * labels to be painted.
     */
    public GeneralLabelPainterPredicate()
    {
        // Default constructor
    }
    
    /**
     * Return the minimum width that the label may have, in world coordinates
     *
     * @return The width
     */
    public double getMinimumWorldWidth()
    {
        return minimumWorldWidth;
    }

    /**
     * Set the minimum width that the label may have, in world coordinates
     *
     * @param minimumWorldWidth The width
     */
    public void setMinimumWorldWidth(double minimumWorldWidth)
    {
        this.minimumWorldWidth = minimumWorldWidth;
    }

    /**
     * Return the maximum width that the label may have, in world coordinates
     *
     * @return The width
     */
    public double getMaximumWorldWidth()
    {
        return maximumWorldWidth;
    }

    /**
     * Set the maximum width that the label may have, in world coordinates
     *
     * @param maximumWorldWidth The width
     */
    public void setMaximumWorldWidth(double maximumWorldWidth)
    {
        this.maximumWorldWidth = maximumWorldWidth;
    }

    /**
     * Return the minimum height that the label may have, in world coordinates
     *
     * @return The width
     */
    public double getMinimumWorldHeight()
    {
        return minimumWorldHeight;
    }

    /**
     * Set the minimum height that the label may have, in world coordinates
     *
     * @param minimumWorldHeight The width
     */
    public void setMinimumWorldHeight(double minimumWorldHeight)
    {
        this.minimumWorldHeight = minimumWorldHeight;
    }

    /**
     * Return the maximum height that the label may have, in world coordinates
     *
     * @return The width
     */
    public double getMaximumWorldHeight()
    {
        return maximumWorldHeight;
    }

    /**
     * Set the maximum height that the label may have, in world coordinates
     *
     * @param maximumWorldHeight The width
     */
    public void setMaximumWorldHeight(double maximumWorldHeight)
    {
        this.maximumWorldHeight = maximumWorldHeight;
    }

    /**
     * Return the minimum width that the label may have, in screen coordinates
     *
     * @return The width
     */
    public double getMinimumScreenWidth()
    {
        return minimumScreenWidth;
    }

    /**
     * Set the minimum width that the label may have, in screen coordinates
     *
     * @param minimumScreenWidth The width
     */
    public void setMinimumScreenWidth(double minimumScreenWidth)
    {
        this.minimumScreenWidth = minimumScreenWidth;
    }

    /**
     * Return the maximum width that the label may have, in screen coordinates
     *
     * @return The width
     */
    public double getMaximumScreenWidth()
    {
        return maximumScreenWidth;
    }

    /**
     * Set the maximum width that the label may have, in screen coordinates
     *
     * @param maximumScreenWidth The width
     */
    public void setMaximumScreenWidth(double maximumScreenWidth)
    {
        this.maximumScreenWidth = maximumScreenWidth;
    }

    /**
     * Return the minimum height that the label may have, in screen coordinates
     *
     * @return The width
     */
    public double getMinimumScreenHeight()
    {
        return minimumScreenHeight;
    }

    /**
     * Set the minimum height that the label may have, in screen coordinates
     *
     * @param minimumScreenHeight The width
     */
    public void setMinimumScreenHeight(double minimumScreenHeight)
    {
        this.minimumScreenHeight = minimumScreenHeight;
    }

    /**
     * Return the maximum height that the label may have, in screen coordinates
     *
     * @return The width
     */
    public double getMaximumScreenHeight()
    {
        return maximumScreenHeight;
    }

    /**
     * Set the maximum height that the label may have, in screen coordinates
     *
     * @param maximumScreenHeight The width
     */
    public void setMaximumScreenHeight(double maximumScreenHeight)
    {
        this.maximumScreenHeight = maximumScreenHeight;
    }

    @Override
    public boolean test(LabelPaintState labelPaintState)
    {
        Rectangle2D bounds = labelPaintState.getLabelBounds();
        
        /*
        {
            double worldWidth = bounds.getWidth();
            double worldHeight = bounds.getHeight();
            AffineTransform labelTransform = 
                labelPaintState.getLabelTransform();
            double screenWidth =
                AffineTransforms.computeDistanceX(labelTransform, worldWidth);
            double screenHeight =
                AffineTransforms.computeDistanceY(labelTransform, worldHeight);
            System.out.println("worldWidth "+worldWidth);
            System.out.println("worldHeight "+worldHeight);
            System.out.println("screenWidth "+screenWidth);
            System.out.println("screenHeight "+screenHeight);
        }
        */
        
        
        double worldWidth = bounds.getWidth();
        if (worldWidth < minimumWorldWidth)
        {
            return false; 
        }
        if (worldWidth > maximumWorldWidth)
        {
            return false; 
        }

        double worldHeight = bounds.getHeight();
        if (worldHeight < minimumWorldHeight)
        {
            return false; 
        }
        if (worldHeight > maximumWorldHeight)
        {
            return false; 
        }
        

        AffineTransform labelTransform = labelPaintState.getLabelTransform();
        double screenWidth =
            AffineTransforms.computeDistanceX(labelTransform, worldWidth);
        if (screenWidth < minimumScreenWidth)
        {
            return false;
        }
        if (screenWidth > maximumScreenWidth)
        {
            return false;
        }
        
        double screenHeight =
            AffineTransforms.computeDistanceY(labelTransform, worldHeight);
        if (screenHeight < minimumScreenHeight)
        {
            return false;
        }
        if (screenHeight > maximumScreenHeight)
        {
            return false;
        }

        return true;
    }

}
