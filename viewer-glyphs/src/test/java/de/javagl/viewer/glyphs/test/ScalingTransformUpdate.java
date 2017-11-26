package de.javagl.viewer.glyphs.test;

import java.awt.geom.AffineTransform;
import java.util.function.BiConsumer;

class ScalingTransformUpdate implements BiConsumer<Object, AffineTransform>
{
    /**
     * The minimum x-value that should be displayed.
     * 
     * @see #setRangeX(double, double)
     */
    private double minX;

    /**
     * The maximum x-value that should be displayed
     * 
     * @see #setRangeX(double, double)
     */
    private double maxX;

    /**
     * The minimum y-value that should be displayed.
     * 
     * @see #setRangeY(double, double)
     */
    private double minY;

    /**
     * The maximum y-value that should be displayed
     * 
     * @see #setRangeY(double, double)
     */
    private double maxY;

    /**
     * Set the range that should be displayed in x-direction
     * 
     * @param minX The minimum x-value that should be displayed
     * @param maxX The maximum x-value that should be displayed
     */
    public void setRangeX(double minX, double maxX)
    {
        this.minX = minX;
        this.maxX = maxX;
    }
    
    /**
     * Set the range that should be displayed in y-direction
     * 
     * @param minY The minimum y-value that should be displayed
     * @param maxY The maximum y-value that should be displayed
     */
    public void setRangeY(double minY, double maxY)
    {
        this.minY = minY;
        this.maxY = maxY;
    }
    
    @Override
    public void accept(Object t, AffineTransform affineTransform)
    {
        double scaleX = 1.0 / (maxX - minX);
        double scaleY = 1.0 / (maxY - minY);
        affineTransform.setToScale(scaleX, scaleY);
    }
    
}