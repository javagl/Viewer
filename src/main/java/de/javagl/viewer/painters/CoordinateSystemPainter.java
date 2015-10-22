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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.function.IntSupplier;

import de.javagl.geom.AffineTransforms;
import de.javagl.geom.Lines;
import de.javagl.geom.Points;
import de.javagl.geom.Rectangles;
import de.javagl.viewer.Painter;

/**
 * Implementation of the {@link Painter} interface that paints a coordinate
 * system, consisting of a grid and labeled coordinate axes.
 */
public final class CoordinateSystemPainter implements Painter
{
    /**
     * The font that will be used for the labels
     */
    private final Font font = new Font("Dialog", Font.PLAIN, 9);
    
    /**
     * The stroke for the axes
     */
    private final Stroke stroke = new BasicStroke(1.0f);

    /**
     * The color of the x-axis. If this is <code>null</code>, then the
     * x-axis will not be painted.
     */
    private Color axisColorX = Color.GRAY;
    
    /**
     * The color of the y-axis. If this is <code>null</code>, then the
     * y-axis will not be painted.
     */
    private Color axisColorY = Color.GRAY;
    
    /**
     * The size of the tick marks on the screen
     */
    private final double tickSizeScreen = 5;
    
    /**
     * The size (height) of the tick marks of the x-axis, in world coordinates
     */
    private double tickSizeWorldX = 0.0;
    
    /**
     * The size (width) of the tick marks of the y-axis, in world coordinates
     */
    private double tickSizeWorldY = 0.0;
    
    /**
     * The minimum distance that two ticks should have on the screen
     */
    private final double minScreenTickDistanceX = 20;

    /**
     * The minimum distance that two ticks should have on the screen
     */
    private final double minScreenTickDistanceY = 20;
    
    /**
     * Whether the "minScreenTickDistanceX" should be adjusted
     * based on the strings that are printed for the labels
     */
    private final boolean adjustForStringLengths = true;
    
    /**
     * Whether labels are printed at the axis ticks
     */
    private final boolean printLabels = true;
    
    /**
     * The color for the grid. If this is <code>null</code>, then no
     * grid will be painted 
     */
    private Color gridColor = new Color(240,240,240);
    
    /**
     * A line object, used internally in various methods
     */
    private final Line2D.Double line = new Line2D.Double();
    
    /**
     * The tick positions of the x-axis, in world coordinates
     */
    private double worldTicksX[];

    /**
     * The label format for the x-axis. May be <code>null</code>
     * if no labels should be painted
     */
    private String labelFormatX;
    
    /**
     * The tick positions of the y-axis, in world coordinates
     */
    private double worldTicksY[];

    /**
     * The label format for the y-axis. May be <code>null</code>
     * if no labels should be painted
     */
    private String labelFormatY;
        
    /**
     * The bounds of the currently visible area, in world coordinates
     */
    private Rectangle2D worldBounds = new Rectangle2D.Double();
    
    /**
     * The supplier that provides the x-coordinate on the screen
     * where the x-axis should start
     */
    private IntSupplier supplierScreenMinX = null;

    /**
     * The supplier that provides the x-coordinate on the screen
     * where the x-axis should end
     */
    private IntSupplier supplierScreenMaxX = null;

    /**
     * The supplier that provides the y-coordinate on the screen
     * where the x-axis should be located
     */
    private IntSupplier supplierScreenY = null;
    
    /**
     * The supplier that provides the y-coordinate on the screen
     * where the y-axis should start
     */
    private IntSupplier supplierScreenMinY = null;

    /**
     * The supplier that provides the y-coordinate on the screen
     * where the y-axis should end
     */
    private IntSupplier supplierScreenMaxY = null;

    /**
     * The supplier that provides the x-coordinate on the screen
     * where the y-axis should be located
     */
    private IntSupplier supplierScreenX = null;

    /**
     * The {@link LabelPainter} for the labels on the x-axis
     */
    private final LabelPainter labelPainterX;
    
    /**
     * The {@link LabelPainter} for the labels on the y-axis
     */
    private final LabelPainter labelPainterY;
    
    /**
     * Creates a new default coordinate system painter
     */
    public CoordinateSystemPainter()
    {
        labelPainterX = new LabelPainter();
        labelPainterX.setLabelAnchor(0.5, 0.0);
        labelPainterX.setPaint(axisColorX);
        labelPainterX.setTransformingLabels(false);
        labelPainterX.setFont(font);
        
        labelPainterY = new LabelPainter();
        labelPainterY.setLabelAnchor(1.0, 0.5);
        labelPainterY.setPaint(axisColorY);
        labelPainterY.setTransformingLabels(false);
        labelPainterY.setFont(font);
        
    }
    
    /**
     * Set the color for the grid that should be painted in the background.
     * If the given color is <code>null</code>, then the grid will not be
     * painted.
     * 
     * @param gridColor The grid color
     */
    public void setGridColor(Color gridColor)
    {
        this.gridColor = gridColor;
    }
    
    /**
     * Set the color for the x-axis. If this is <code>null</code>, then
     * the x-axis will not be painted.
     * 
     * @param axisColorX The color for the x-axis
     */
    public void setAxisColorX(Color axisColorX)
    {
        this.axisColorX = axisColorX;
    }

    /**
     * Set the color for the y-axis. If this is <code>null</code>, then
     * the y-axis will not be painted.
     * 
     * @param axisColorY The color for the y-axis
     */
    public void setAxisColorY(Color axisColorY)
    {
        this.axisColorY = axisColorY;
    }
    
    
    /**
     * Set the layout for the x-axis. The given suppliers will provide
     * the screen coordinates that determine how the x-axis should be 
     * displayed. If any of the given suppliers is <code>null</code>, 
     * then the axis will be painted in world coordinates.<br>
     * <br>
     * Note that this screen-relative layout is not sensibly applicable
     * when the view is rotated. 
     * 
     * @param supplierScreenMinX The supplier for the x-coordinate on the
     * screen where the x-axis should start
     * @param supplierScreenMaxX The supplier for the x-coordinate on the
     * screen where the x-axis should end
     * @param supplierScreenY The supplier for the y-coordinate on the
     * screen where the x-axis should be located
     */
    public void setScreenAxisLayoutX(
        IntSupplier supplierScreenMinX,
        IntSupplier supplierScreenMaxX,
        IntSupplier supplierScreenY)
    {
        this.supplierScreenMinX = supplierScreenMinX;
        this.supplierScreenMaxX = supplierScreenMaxX;
        this.supplierScreenY = supplierScreenY;
    }

    
    /**
     * Set the layout for the y-axis. The given suppliers will provide
     * the screen coordinates that determine how the y-axis should be 
     * displayed. If any of the given suppliers is <code>null</code>, 
     * then the axis will be painted in world coordinates.<br>
     * <br>
     * Note that this screen-relative layout is not sensibly applicable
     * when the view is rotated. 
     * 
     * @param supplierScreenMinY The supplier for the y-coordinate on the
     * screen where the y-axis should start
     * @param supplierScreenMaxY The supplier for the y-coordinate on the
     * screen where the y-axis should end
     * @param supplierScreenX The supplier for the x-coordinate on the
     * screen where the y-axis should be located
     */
    public void setScreenAxisLayoutY(
        IntSupplier supplierScreenMinY,
        IntSupplier supplierScreenMaxY,
        IntSupplier supplierScreenX)
    {
        this.supplierScreenMinY = supplierScreenMinY;
        this.supplierScreenMaxY = supplierScreenMaxY;
        this.supplierScreenX = supplierScreenX;
    }

    
    /**
     * Update the data that is used internally for painting the x-axis, 
     * namely the {@link #worldTicksX} and the {@link #labelFormatX}
     * 
     * @param g The graphics
     * @param worldToScreen The world-to-screen transform
     * @param worldMinX The minimum x-coordinate
     * @param worldMaxX The maximum x-coordinate
     */
    private void updateX(Graphics g, AffineTransform worldToScreen,
        double worldMinX, double worldMaxX)
    {
        
        double worldTickDistanceX = 
            Axes.computeWorldTickDistanceX(
                worldToScreen, minScreenTickDistanceX);
        if (printLabels && adjustForStringLengths)
        {
            worldTickDistanceX = 
                Axes.computeAdjustedWorldTickDistanceX(
                    g, font, worldToScreen, worldMinX, worldMaxX, 
                    worldTickDistanceX, minScreenTickDistanceX);
        }
        worldTicksX = Axes.computeWorldTicks(
            worldMinX, worldMaxX, worldTickDistanceX);
        labelFormatX =  null;
        if (printLabels)
        {
            labelFormatX = Axes.formatStringFor(worldTickDistanceX);
        }
    }

    /**
     * Update the data that is used internally for painting the y-axis, 
     * namely the {@link #worldTicksY} and the {@link #labelFormatY}
     * 
     * @param worldToScreen The world-to-screen transform
     * @param worldMinY The minimum y-coordinate
     * @param worldMaxY The maximum y-coordinate
     */
    private void updateY(AffineTransform worldToScreen,
        double worldMinY, double worldMaxY)
    {
        double worldTickDistanceY = 
            Axes.computeWorldTickDistanceY(
                worldToScreen, minScreenTickDistanceY);
        worldTicksY = Axes.computeWorldTicks(
            worldMinY, worldMaxY, worldTickDistanceY);
        labelFormatY =  null;
        if (printLabels)
        {
            labelFormatY = Axes.formatStringFor(worldTickDistanceY);
        }
    }
    

    @Override
    public final void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        Rectangle2D screenBounds = new Rectangle2D.Double(0, 0, w, h);
        AffineTransform screenToWorld = 
            AffineTransforms.invert(worldToScreen, null);
        Rectangles.computeBounds(
            screenToWorld, screenBounds, worldBounds);
        updateX(g, worldToScreen, worldBounds.getMinX(), worldBounds.getMaxX());
        updateY(worldToScreen, worldBounds.getMinY(), worldBounds.getMaxY());
        tickSizeWorldX = AffineTransforms.computeDistanceY(
            screenToWorld, tickSizeScreen);
        tickSizeWorldY = AffineTransforms.computeDistanceX(
            screenToWorld, tickSizeScreen);
        
        g.setStroke(stroke);
        if (gridColor != null)
        {
            g.setColor(gridColor);
            paintInternalGrid(g, worldToScreen);
        }
        if (axisColorX != null)
        {
            g.setColor(axisColorX);
            paintAxisX(g, worldToScreen);
        }
        if (axisColorY != null)
        {
            g.setColor(axisColorY);
            paintAxisY(g, worldToScreen);
        }
    }
    
    /**
     * Paint the x-axis.
     *  
     * @param g The graphics to paint to
     * @param worldToScreen The world-to-screen transform
     */
    private void paintAxisX(
        Graphics2D g, AffineTransform worldToScreen)
    {
        if (supplierScreenMinX == null || 
            supplierScreenMaxX == null || 
            supplierScreenY == null)
        {
            paintAxisX(g, worldToScreen, 
                worldBounds.getMinX(), worldBounds.getMaxX(), 0.0);
        }
        else
        {
            int screenMinX = supplierScreenMinX.getAsInt();
            int screenMaxX = supplierScreenMaxX.getAsInt();
            int screenY = supplierScreenY.getAsInt();
            Point2D pxMin = new Point2D.Double(screenMinX, screenY);
            Point2D pxMax = new Point2D.Double(screenMaxX, screenY);
            Points.inverseTransform(worldToScreen, pxMin, pxMin);
            Points.inverseTransform(worldToScreen, pxMax, pxMax);
            updateX(g, worldToScreen, pxMin.getX(), pxMax.getX());
            paintAxisX(g, worldToScreen, 
                pxMin.getX(), pxMax.getX(), pxMin.getY());
        }
    }
    
    /**
     * Paint the y-axis.
     *  
     * @param g The graphics to paint to
     * @param worldToScreen The world-to-screen transform
     */
    protected void paintAxisY(
        Graphics2D g, AffineTransform worldToScreen)
    {
        if (supplierScreenMinY == null || 
            supplierScreenMaxY == null || 
            supplierScreenX == null)
        {
            paintAxisY(g, worldToScreen, 
                worldBounds.getMinY(), worldBounds.getMaxY(), 0.0);
        }
        else
        {
            int screenMinY = supplierScreenMinY.getAsInt();
            int screenMaxY = supplierScreenMaxY.getAsInt();
            int screenX = supplierScreenX.getAsInt();
            Point2D pyMin = new Point2D.Double(screenX, screenMinY);
            Point2D pyMax = new Point2D.Double(screenX, screenMaxY);
            Points.inverseTransform(worldToScreen, pyMin, pyMin);
            Points.inverseTransform(worldToScreen, pyMax, pyMax);
            paintAxisY(g, worldToScreen, 
                pyMin.getY(), pyMax.getY(), pyMin.getX());
        }
    }
    
    
    
    /**
     * Paint the x-axis after it has been made sure that the
     * {@link #worldTicksX} and {@link #labelFormatX} are up to date
     *  
     * @param g The graphics to paint to
     * @param worldToScreen The world-to-screen transform
     * @param worldMinX The minimum world coordinate of the axis
     * @param worldMaxX The maximum world coordinate of the axis
     * @param worldY The world coordinate at which the axis should be painted
     */
    private void paintAxisX(
        Graphics2D g, AffineTransform worldToScreen, 
        double worldMinX, double worldMaxX, double worldY)
    {
        line.setLine(worldMinX,worldY,worldMaxX,worldY);
        Lines.transform(worldToScreen, line, line);
        g.draw(line);
        g.setFont(font);
        for (int i=0; i<worldTicksX.length; i++)
        {
            double worldTickX = worldTicksX[i];
            if (worldTickX >= worldMinX && worldTickX <= worldMaxX)
            {
                paintTickX(g, worldToScreen, worldTickX, worldY, labelFormatX);
            }
        }
    }

    
    /**
     * Paint the y-axis after it has been made sure that the
     * {@link #worldTicksY} and {@link #labelFormatY} are up to date
     *  
     * @param g The graphics to paint to
     * @param worldToScreen The world-to-screen transform
     * @param worldMinY The minimum world coordinate of the axis
     * @param worldMaxY The maximum world coordinate of the axis
     * @param worldX The world coordinate at which the axis should be painted
     */
    private void paintAxisY(
        Graphics2D g, AffineTransform worldToScreen, 
        double worldMinY, double worldMaxY, double worldX)
    {
        line.setLine(worldX,worldMinY,worldX,worldMaxY);
        Lines.transform(worldToScreen, line, line);
        g.draw(line);
        g.setFont(font);
        for (int i=0; i<worldTicksY.length; i++)
        {
            double worldTickY = worldTicksY[i];
            if (worldTickY >= worldMinY && worldTickY <= worldMaxY)
            {
                paintTickY(g, worldToScreen, worldX, worldTickY, labelFormatY);
            }
        }
    }

    /**
     * Paint the coordinate grid in the background, after it has been
     * made sure that the data for painting the grid and axes is up
     * to date 
     *  
     * @param g The graphics to paint to
     * @param worldToScreen The world-to-screen transform
     */
    private void paintInternalGrid(Graphics2D g, AffineTransform worldToScreen)
    {
        double worldMinX = worldBounds.getMinX();
        double worldMaxX = worldBounds.getMaxX();
        double worldMinY = worldBounds.getMinY();
        double worldMaxY = worldBounds.getMaxY();
        for (int i=0; i<worldTicksX.length; i++)
        {
            double worldTickX = worldTicksX[i];
            paintGridLineX(g, worldToScreen, worldTickX, 
                worldMinY, worldMaxY);
        }
        for (int i=0; i<worldTicksY.length; i++)
        {
            double worldTickY = worldTicksY[i];
            paintGridLineY(g, worldToScreen, worldTickY, 
                worldMinX, worldMaxX);
        }
    }
    
    
    
    /**
     * Paints a single grid line at the given x-coordinate 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldX The world coordinate of the grid line
     * @param worldMinY The minimum y-coordinate 
     * @param worldMaxY The maximum y-coordinate 
     */
    private void paintGridLineX(Graphics2D g, AffineTransform worldToScreen, 
        double worldX, double worldMinY, double worldMaxY)
    {
        line.setLine(worldX, worldMinY, worldX, worldMaxY);
        Lines.transform(worldToScreen, line, line);
        g.draw(line);
    }
    
    /**
     * Paints a single grid line at the given y-coordinate 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldY The world coordinate of the tick
     * @param worldMinX The minimum x-coordinate 
     * @param worldMaxX The maximum x-coordinate 
     */
    private void paintGridLineY(Graphics2D g, AffineTransform worldToScreen, 
        double worldY, double worldMinX, double worldMaxX)
    {
        line.setLine(worldMinX, worldY, worldMaxX, worldY);
        Lines.transform(worldToScreen, line, line);
        g.draw(line);
    }
    
    
    /**
     * Paints a single tick of the x-axis 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldX The x-world coordinate of the tick
     * @param worldY The y-world coordinate of the tick
     * @param labelFormat The format string for the labels. If this is
     * <code>null</code>, then no labels will be painted
     */
    private void paintTickX(Graphics2D g, AffineTransform worldToScreen, 
        double worldX, double worldY, String labelFormat)
    {
        line.setLine(worldX, worldY, worldX, worldY-1);
        Lines.transform(worldToScreen, line, line);
        Lines.scaleToLength(tickSizeScreen, line, line);
        g.draw(line);
        
        if (labelFormat != null)
        {
            String string = String.format(labelFormat, worldX);
            labelPainterX.setLabelLocation(worldX, worldY - tickSizeWorldX);
            labelPainterX.paint(g, worldToScreen, 0, 0, string);
        }
    }
    
    /**
     * Paints a single tick of the y-axis 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldX The x-world coordinate of the tick
     * @param worldY The y-world coordinate of the tick
     * @param labelFormat The format string for the labels. If this is
     * <code>null</code>, then no labels will be painted
     */
    private void paintTickY(Graphics2D g, AffineTransform worldToScreen, 
        double worldX, double worldY, String labelFormat)
    {
        line.setLine(worldX, worldY, worldX-1, worldY);
        Lines.transform(worldToScreen, line, line);
        Lines.scaleToLength(tickSizeScreen, line, line);
        g.draw(line);
        
        if (labelFormat != null)
        {
            String string = String.format(labelFormat, worldY);
            labelPainterY.setLabelLocation(worldX - tickSizeWorldY, worldY);
            labelPainterY.paint(g, worldToScreen, 0, 0, string);
        }
    }
}