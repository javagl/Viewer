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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.function.DoubleFunction;
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
     * A line object, used internally in various methods
     */
    private static final Line2D.Double TEMP_LINE = new Line2D.Double();

    /**
     * A point object, used internally in various methods
     */
    private static final Point2D.Double TEMP_POINT = new Point2D.Double();
    
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
     * The color for the x-axis tick grid lines. If this is <code>null</code>, 
     * then no grid lines will be painted 
     */
    private Color gridColorX = new Color(240,240,240);
    
    /**
     * The color for the y-axis tick grid lines. If this is <code>null</code>, 
     * then no grid lines will be painted 
     */
    private Color gridColorY = new Color(240,240,240);
    
    /**
     * The fixed distance between ticks on the x-axis, in world coordinates
     */
    private double fixedWorldTickDistanceX = Double.NaN;
    
    /**
     * The fixed distance between ticks on the y-axis, in world coordinates
     */
    private double fixedWorldTickDistanceY = Double.NaN;
    
    /**
     * The tick positions of the x-axis, in world coordinates
     */
    private double worldTicksX[];

    /**
     * The label format for the x-axis. May be <code>null</code>
     * if the {@link #labelFormatterX} is used.
     */
    private String labelFormatX;
    
    /**
     * The formatter that will receive values for ticks at the x-axis, and
     * return the corresponding label string. May be <code>null</code> if
     * the default {@link #labelFormatX} should be used.
     */
    private DoubleFunction<String> labelFormatterX;
    
    /**
     * The tick positions of the y-axis, in world coordinates
     */
    private double worldTicksY[];

    /**
     * The label format for the y-axis. May be <code>null</code>
     * if the {@link #labelFormatterY} is used.
     */
    private String labelFormatY;

    /**
     * The formatter that will receive values for ticks at the y-axis, and
     * return the corresponding label string. May be <code>null</code> if
     * the default {@link #labelFormatY} should be used.
     */
    private DoubleFunction<String> labelFormatterY;
    
    /**
     * The bounds of the currently visible area, in world coordinates
     */
    private final Rectangle2D worldBounds = new Rectangle2D.Double();
    
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
     * The minimum value for the x-axis, in world coordinates
     * 
     * @see #setAxisRangeX(double, double)
     */
    private double worldMinAxisX = Double.NaN;
    
    /**
     * The maximum value for the x-axis, in world coordinates
     * 
     * @see #setAxisRangeX(double, double)
     */
    private double worldMaxAxisX = Double.NaN;

    /**
     * The minimum value for the Y-axis, in world coordinates
     * 
     * @see #setAxisRangeY(double, double)
     */
    private double worldMinAxisY = Double.NaN;
    
    /**
     * The maximum value for the y-axis, in world coordinates
     * 
     * @see #setAxisRangeY(double, double)
     */
    private double worldMaxAxisY = Double.NaN;
    
    /**
     * The x-coordinate where the y-axis should be, in world coordinates
     */
    private double worldXofY = 0.0;

    /**
     * The y-coordinate where the x-axis should be, in world coordinates
     */
    private double worldYofX = 0.0;
    
    /**
     * Whether ticks on the x-axis should be oriented along the positive y-axis
     */
    private boolean tickOrientationPositiveX = false;
    
    /**
     * Whether the y-component of the label painter for the x-axis labels
     * should be adjusted automatically (to either be 0.0 or 1.0), 
     * depending on the {@link #tickOrientationPositiveX} flag and
     * the current y-scaling of the world-to-screen transform 
     */
    private boolean adjustLabelAnchorX = false;
    
    /**
     * Whether ticks on the y-axis should be oriented along the positive x-axis
     */
    private boolean tickOrientationPositiveY = false;

    /**
     * Whether the x-component of the label painter for the y-axis labels
     * should be adjusted automatically (to either be 0.0 or 1.0), 
     * depending on the {@link #tickOrientationPositiveY} flag and
     * the current x-scaling of the world-to-screen transform 
     */
    private boolean adjustLabelAnchorY = false;
    
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
        labelPainterX.setPaint(axisColorX);
        labelPainterX.setTransformingLabels(false);
        labelPainterX.setFont(font);
        labelPainterX.setLabelAnchor(0.5, 0.0);
        
        labelPainterY = new LabelPainter();
        labelPainterY.setPaint(axisColorY);
        labelPainterY.setTransformingLabels(false);
        labelPainterY.setFont(font);
        labelPainterY.setLabelAnchor(1.0, 0.5);
    }
    
    /**
     * Returns the {@link LabelPainter} that is used for painting the labels
     * along the x-axis
     * 
     * @return The {@link LabelPainter}
     */
    public LabelPainter getLabelPainterX()
    {
        return labelPainterX;
    }
    
    /**
     * Set the formatter that will receive x-values for ticks, and return
     * the string that should be painted at this coordinate. This formatter
     * may be null, in which case a string representation of the x-value
     * will be painted.
     * 
     * @param labelFormatterX The formatter
     */
    public void setLabelFormatterX(DoubleFunction<String> labelFormatterX)
    {
        this.labelFormatterX = labelFormatterX;
    }
    
    /**
     * Returns the {@link LabelPainter} that is used for painting the labels
     * along the y-axis
     * 
     * @return The {@link LabelPainter}
     */
    public LabelPainter getLabelPainterY()
    {
        return labelPainterY;
    }
    
    /**
     * Set the formatter that will receive y-values for ticks, and return
     * the string that should be painted at this coordinate. This formatter
     * may be null, in which case a string representation of the y-value
     * will be painted.
     * 
     * @param labelFormatterY The formatter
     */
    public void setLabelFormatterY(DoubleFunction<String> labelFormatterY)
    {
        this.labelFormatterY = labelFormatterY;
    }
    
    /**
     * Set the fixed distance between ticks on the x-axis, in world coordinates.
     * If the given value is NaN, then the distance will be computed 
     * automatically.
     * 
     * @param fixedWorldTickDistanceX The tick distance
     * @throws IllegalArgumentException If the given distance is not positive
     */
    public void setFixedWorldTickDistanceX(double fixedWorldTickDistanceX)
    {
        if (fixedWorldTickDistanceX <= 0)
        {
            throw new IllegalArgumentException(
                "Tick distance must be positive, "
                + "but is "+fixedWorldTickDistanceX);
        }
        this.fixedWorldTickDistanceX = fixedWorldTickDistanceX;
    }

    /**
     * Set the fixed distance between ticks on the y-axis, in world coordinates.
     * If the given value is NaN, then the distance will be computed 
     * automatically.
     * 
     * @param fixedWorldTickDistanceY The tick distance
     * @throws IllegalArgumentException If the given distance is not positive
     */
    public void setFixedWorldTickDistanceY(double fixedWorldTickDistanceY)
    {
        if (fixedWorldTickDistanceY <= 0)
        {
            throw new IllegalArgumentException(
                "Tick distance must be positive, "
                + "but is "+fixedWorldTickDistanceY);
        }
        this.fixedWorldTickDistanceY = fixedWorldTickDistanceY;
    }
    
    /**
     * Set the color for the grid lines that should be painted at the x-axis
     * ticks in the background. If the given color is <code>null</code>, then 
     * the grid lines will not be painted.
     * 
     * @param gridColorX The grid color
     */
    public void setGridColorX(Color gridColorX)
    {
        this.gridColorX = gridColorX;
    }

    /**
     * Set the color for the grid lines that should be painted at the y-axis
     * ticks in the background. If the given color is <code>null</code>, then 
     * the grid lines will not be painted.
     * 
     * @param gridColorY The grid color
     */
    public void setGridColorY(Color gridColorY)
    {
        this.gridColorY = gridColorY;
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
     * Set whether ticks on the x-axis should be oriented along the positive 
     * y-axis
     * 
     * @param tickOrientationPositiveX Whether the ticks should be oriented
     * along the positive axis
     */
    void setTickOrientationPositiveX(boolean tickOrientationPositiveX)
    {
        this.tickOrientationPositiveX = tickOrientationPositiveX;
    }

    /**
     * Set the range of the x-axis that should be displayed. If either
     * of the given values is <code>Double.NaN</code>, then the minimum
     * or maximum value of the currently visible world area will be
     * used, respectively
     * 
     * @param worldMinAxisX The minimum value, in world coordinates
     * @param worldMaxAxisX The maximum value, in world coordinates
     */
    public void setAxisRangeX(double worldMinAxisX, double worldMaxAxisX)
    {
        this.worldMinAxisX = worldMinAxisX;
        this.worldMaxAxisX = worldMaxAxisX;
    }
    
    /**
     * Set the location where the x-axis should be painted
     *  
     * @param worldYofX The y-coordinate where the x-axis should be painted
     */
    public void setAxisLocationX(double worldYofX)
    {
        this.worldYofX = worldYofX;
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
     * Set the range of the y-axis that should be displayed. If either
     * of the given values is <code>Double.NaN</code>, then the minimum
     * or maximum value of the currently visible world area will be
     * used, respectively
     * 
     * @param worldMinAxisY The minimum value, in world coordinates
     * @param worldMaxAxisY The maximum value, in world coordinates
     */
    public void setAxisRangeY(double worldMinAxisY, double worldMaxAxisY)
    {
        this.worldMinAxisY = worldMinAxisY;
        this.worldMaxAxisY = worldMaxAxisY;
    }
    
    /**
     * Set the location where the y-axis should be painted
     *  
     * @param worldXofY The x-coordinate where the y-axis should be painted
     */
    public void setAxisLocationY(double worldXofY)
    {
        this.worldXofY = worldXofY;
    }

    /**
     * Set whether ticks on the y-axis should be oriented along the positive 
     * x-axis
     * 
     * @param tickOrientationPositiveY Whether the ticks should be oriented
     * along the positive axis
     */
    void setTickOrientationPositiveY(boolean tickOrientationPositiveY)
    {
        this.tickOrientationPositiveY = tickOrientationPositiveY;
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
     * @param worldToScreen The world-to-screen transform
     * @param worldMinX The minimum x-coordinate
     * @param worldMaxX The maximum x-coordinate
     */
    private void updateX(AffineTransform worldToScreen,
        double worldMinX, double worldMaxX)
    {
        Paint labelPaintX = labelPainterX.getPaint();
        double worldTickDistanceX = fixedWorldTickDistanceX;
        if (!Double.isFinite(worldTickDistanceX))
        {   
            worldTickDistanceX = Axes.computeWorldTickDistanceX(
                worldToScreen, minScreenTickDistanceX);
            if (labelPaintX != null && adjustForStringLengths)
            {
                worldTickDistanceX = 
                    Axes.computeAdjustedWorldTickDistanceX(
                        font, worldToScreen, worldMinX, worldMaxX, 
                        worldTickDistanceX, minScreenTickDistanceX);
            }
        }
        
        worldTicksX = Axes.computeWorldTicks(
            worldMinX, worldMaxX, worldTickDistanceX);
        if (labelPaintX != null)
        {
            labelFormatX = Axes.formatStringFor(worldTickDistanceX);
        }
        
        if (adjustLabelAnchorX)
        {
            Point2D anchor = labelPainterX.getLabelAnchor();
            if (tickOrientationPositiveX)
            {
                if (worldToScreen.getScaleY() > 0)
                {
                    labelPainterX.setLabelAnchor(anchor.getX(), 0.0);
                }
                else
                {
                    labelPainterX.setLabelAnchor(anchor.getX(), 1.0);
                }
            }
            else
            {
                if (worldToScreen.getScaleY() > 0)
                {
                    labelPainterX.setLabelAnchor(anchor.getX(), 1.0);
                }
                else
                {
                    labelPainterX.setLabelAnchor(anchor.getX(), 0.0);
                }
            }
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
        Paint labelPaintY = labelPainterY.getPaint();
        double worldTickDistanceY = fixedWorldTickDistanceY;
        if (!Double.isFinite(worldTickDistanceY))
        {
            worldTickDistanceY = 
                Axes.computeWorldTickDistanceY(
                    worldToScreen, minScreenTickDistanceY);
        }
        worldTicksY = Axes.computeWorldTicks(
            worldMinY, worldMaxY, worldTickDistanceY);
        if (labelPaintY != null)
        {
            labelFormatY = Axes.formatStringFor(worldTickDistanceY);
        }
        
        if (adjustLabelAnchorY)
        {
            Point2D anchor = labelPainterY.getLabelAnchor();
            if (tickOrientationPositiveY)
            {
                if (worldToScreen.getScaleX() > 0)
                {
                    labelPainterY.setLabelAnchor(0.0, anchor.getY());
                }
                else
                {
                    labelPainterY.setLabelAnchor(1.0, anchor.getY());
                }
            }
            else
            {
                if (worldToScreen.getScaleX() > 0)
                {
                    labelPainterY.setLabelAnchor(1.0, anchor.getY());
                }
                else
                {
                    labelPainterY.setLabelAnchor(0.0, anchor.getY());
                }
            }
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
        updateX(worldToScreen, worldBounds.getMinX(), worldBounds.getMaxX());
        updateY(worldToScreen, worldBounds.getMinY(), worldBounds.getMaxY());
        
        g.setStroke(stroke);
        if (gridColorX != null)
        {
            g.setColor(gridColorX);
            paintInternalGridX(g, worldToScreen);
        }
        if (gridColorY != null)
        {
            g.setColor(gridColorY);
            paintInternalGridY(g, worldToScreen);
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
                getValue(worldMinAxisX, worldBounds.getMinX()), 
                getValue(worldMaxAxisX, worldBounds.getMaxX()), 
                worldYofX);
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
            updateX(worldToScreen, pxMin.getX(), pxMax.getX());
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
                getValue(worldMinAxisY, worldBounds.getMinY()), 
                getValue(worldMaxAxisY, worldBounds.getMaxY()), 
                worldXofY);
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
        TEMP_LINE.setLine(worldMinX,worldY,worldMaxX,worldY);
        Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
        g.draw(TEMP_LINE);
        for (int i=0; i<worldTicksX.length; i++)
        {
            double worldTickX = worldTicksX[i];
            if (worldTickX >= worldMinX && worldTickX <= worldMaxX)
            {
                paintTickX(g, worldToScreen, worldTickX, worldY);
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
        TEMP_LINE.setLine(worldX,worldMinY,worldX,worldMaxY);
        Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
        g.draw(TEMP_LINE);
        for (int i=0; i<worldTicksY.length; i++)
        {
            double worldTickY = worldTicksY[i];
            if (worldTickY >= worldMinY && worldTickY <= worldMaxY)
            {
                paintTickY(g, worldToScreen, worldX, worldTickY);
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
    private void paintInternalGridX(Graphics2D g, AffineTransform worldToScreen)
    {
        double worldMinX = getValue(worldMinAxisX, worldBounds.getMinX());
        double worldMaxX = getValue(worldMaxAxisX, worldBounds.getMaxX());
        double worldMinY = getValue(worldMinAxisY, worldBounds.getMinY());
        double worldMaxY = getValue(worldMaxAxisY, worldBounds.getMaxY());
        for (int i=0; i<worldTicksX.length; i++)
        {
            double worldTickX = worldTicksX[i];
            if (worldTickX >= worldMinX && worldTickX <= worldMaxX)
            {
                paintGridLineX(g, worldToScreen, worldTickX, 
                    worldMinY, worldMaxY);
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
    private void paintInternalGridY(Graphics2D g, AffineTransform worldToScreen)
    {
        double worldMinX = getValue(worldMinAxisX, worldBounds.getMinX());
        double worldMaxX = getValue(worldMaxAxisX, worldBounds.getMaxX());
        double worldMinY = getValue(worldMinAxisY, worldBounds.getMinY());
        double worldMaxY = getValue(worldMaxAxisY, worldBounds.getMaxY());
        for (int i=0; i<worldTicksY.length; i++)
        {
            double worldTickY = worldTicksY[i];
            if (worldTickY >= worldMinY && worldTickY <= worldMaxY)
            {
                paintGridLineY(g, worldToScreen, worldTickY, 
                    worldMinX, worldMaxX);
            }
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
        TEMP_LINE.setLine(worldX, worldMinY, worldX, worldMaxY);
        Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
        g.draw(TEMP_LINE);
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
        TEMP_LINE.setLine(worldMinX, worldY, worldMaxX, worldY);
        Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
        g.draw(TEMP_LINE);
    }
    
    
    /**
     * Paints a single tick of the x-axis 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldX The x-world coordinate of the tick
     * @param worldY The y-world coordinate of the tick
     */
    private void paintTickX(Graphics2D g, AffineTransform worldToScreen, 
        double worldX, double worldY)
    {
        TEMP_LINE.setLine(worldX, worldY, worldX, worldY+1);
        Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
        double length = -tickSizeScreen;
        if (tickOrientationPositiveX)
        {
            length = -length;
        }
        Lines.scaleToLength(length, TEMP_LINE, TEMP_LINE);
        g.draw(TEMP_LINE);
        
        Paint labelPaintX = labelPainterX.getPaint();
        if (labelPaintX != null )
        {
            TEMP_POINT.setLocation(TEMP_LINE.getX2(), TEMP_LINE.getY2());
            Points.inverseTransform(worldToScreen, TEMP_POINT, TEMP_POINT);
            paintLabelX(g, worldToScreen, TEMP_POINT.getX(), TEMP_POINT.getY());
        }
    }
    
    /**
     * Paints a single label of the x-axis 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldX The x-world coordinate of the label
     * @param worldY The y-world coordinate of the label
     */
    private void paintLabelX(Graphics2D g, AffineTransform worldToScreen, 
        double worldX, double worldY)
    {
        String string = null;
        if (labelFormatterX == null)
        {
            string = String.format(labelFormatX, worldX);
        }
        else
        {
            string = labelFormatterX.apply(worldX);
        }
        labelPainterX.setLabelLocation(worldX, worldY);
        labelPainterX.paint(g, worldToScreen, 0, 0, string);
    }
    
    /**
     * Paints a single tick of the y-axis 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldX The x-world coordinate of the tick
     * @param worldY The y-world coordinate of the tick
     */
    private void paintTickY(Graphics2D g, AffineTransform worldToScreen, 
        double worldX, double worldY)
    {
        TEMP_LINE.setLine(worldX, worldY, worldX+1.0, worldY);
        Lines.transform(worldToScreen, TEMP_LINE, TEMP_LINE);
        double length = -tickSizeScreen;
        if (tickOrientationPositiveY)
        {
            length = -length;
        }
        Lines.scaleToLength(length, TEMP_LINE, TEMP_LINE);
        g.draw(TEMP_LINE);
        
        Paint labelPaintY = labelPainterY.getPaint();
        if (labelPaintY != null )
        {
            TEMP_POINT.setLocation(TEMP_LINE.getX2(), TEMP_LINE.getY2());
            Points.inverseTransform(worldToScreen, TEMP_POINT, TEMP_POINT);
            paintLabelY(g, worldToScreen, TEMP_POINT.getX(), TEMP_POINT.getY());
        }
    }
    
    /**
     * Paints a single label of the y-axis 
     * 
     * @param g The graphics context
     * @param worldToScreen The world-to-screen transform
     * @param worldX The x-world coordinate of the label
     * @param worldY The y-world coordinate of the label
     */
    private void paintLabelY(Graphics2D g, AffineTransform worldToScreen, 
        double worldX, double worldY)
    {
        String string = null;
        if (labelFormatterY == null)
        {
            string = String.format(labelFormatY, worldY);
        }
        else
        {
            string = labelFormatterY.apply(worldY);
        }
        labelPainterY.setLabelLocation(worldX, worldY);
        labelPainterY.paint(g, worldToScreen, 0, 0, string);
    }
    
    
    /**
     * Returns the given optional value if it is not <code>Double.NaN</code>,
     * and the given value otherwise
     * 
     * @param optionalValue The optional value
     * @param value The value
     * @return The respective value
     */
    private static double getValue(double optionalValue, double value)
    {
        if (!Double.isNaN(optionalValue))
        {
            return optionalValue;
        }
        return value;
    }
    
}