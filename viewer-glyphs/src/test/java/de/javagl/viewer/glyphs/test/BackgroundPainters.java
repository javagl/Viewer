package de.javagl.viewer.glyphs.test;

import java.awt.Color;
import java.awt.Paint;

/**
 * Methods to create {@link BackgroundPainter} instances
 */
class BackgroundPainters
{
    /**
     * Creates a new {@link BackgroundPainter} with the given background
     * 
     * @param background The background
     * @return The {@link BackgroundPainter}
     */
    static BackgroundPainter create(Paint background)
    {
        BackgroundPainter backgroundPainter = new BackgroundPainter();
        backgroundPainter.setBackground(background);
        return backgroundPainter;
    }
    
    /**
     * Creates a new {@link BackgroundPainter} with the specified gradient
     * 
     * @param topColor The color at the top
     * @param bottomColor The color at the bottom 
     * @return The {@link BackgroundPainter}
     */
    static BackgroundPainter createVerticalGradient(
        Color topColor, Color bottomColor)
    {
        BackgroundPainter backgroundPainter = new BackgroundPainter();
        backgroundPainter.setVerticalGradient(topColor, bottomColor);
        return backgroundPainter;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private BackgroundPainters()
    {
        // Private constructor to prevent instantiation 
    }
    
}
