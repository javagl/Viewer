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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.function.Predicate;

import de.javagl.geom.AffineTransforms;
import de.javagl.viewer.ObjectPainter;


/**
 * Implementation of an {@link ObjectPainter} for strings that serve as
 * labels, to be painted with a certain font and layout
 */
public final class LabelPainter implements ObjectPainter<String>
{
    /**
     * Temporary affine transform, used internally
     */
    private static final AffineTransform TEMP_AFFINE_TRANSFORM =
        new AffineTransform();
    
    /**
     * A class describing the state of a label that is about to be
     * painted. An instance of this class will be passed to the
     * label painting condition predicate that may have been set
     * with {@link #setLabelPaintingCondition(Predicate)}, to 
     * determine whether the label should be painted.
     */
    public static class LabelPaintState
    {
        /**
         * The label
         */
        private String label;
        
        /**
         * The label bounds
         */
        private final Rectangle2D labelBounds;
        
        /**
         * The affine transform that will be applied to the graphics 
         * before the label is painted
         */
        private final AffineTransform labelTransform;
        
        /**
         * The affine transform that was passed to the painting method
         * of the {@link LabelPainter}
         */
        private final AffineTransform worldToScreenTransform;
        
        /**
         * Default constructor
         */
        LabelPaintState()
        {
            label = "";
            labelBounds = new Rectangle2D.Double();
            labelTransform = new AffineTransform();
            worldToScreenTransform = new AffineTransform();
        }
        
        /**
         * Returns the label that is about to be painted
         * 
         * @return The label
         */
        public String getLabel()
        {
            return label;
        }
        
        /**
         * Returns a reference to the bounds of the label that is about
         * to be painted. These are the untransformed bounds, and thus,
         * only depend on the font and contents of the label.
         * 
         * @return The label bounds
         */
        public Rectangle2D getLabelBounds()
        {
            return labelBounds;
        }
        
        /**
         * Returns a reference to the affine transform that will be applied
         * to the graphics context before the label is painted at the origin.
         * If the calling {@link LabelPainter} is 
         * {@link LabelPainter#isTransformingLabels() transforming the labels}, 
         * then this will include the world to screen transform.
         * 
         * @return The affine transform
         */
        public AffineTransform getLabelTransform()
        {
            return labelTransform;
        }

        /**
         * Returns a reference to an affine transform that is equal to
         * the transform that was passed to the painting method of
         * the {@link LabelPainter}
         * 
         * @return The affine transform
         */
        public AffineTransform getWorldToScreenTransform()
        {
            return worldToScreenTransform;
        }
        
        /**
         * Set the label
         * 
         * @param label The label
         */
        void setLabel(String label)
        {
            this.label = label;
        }
        
        /**
         * Set the label bounds
         * 
         * @param labelBounds The label bounds
         */
        void setLabelBounds(Rectangle2D labelBounds)
        {
            this.labelBounds.setFrame(labelBounds);
        }
        
        /**
         * Set the label transform. 
         * 
         * @param transform The label transform
         */
        void setLabelTransform(AffineTransform transform)
        {
            this.labelTransform.setTransform(transform);
        }
        
        /**
         * Set the world to screen transform
         * 
         * @param transform The transform
         */
        void setWorldToScreenTransform(AffineTransform transform)
        {
            this.worldToScreenTransform.setTransform(transform);
        }
        
    }
    
    
    /**
     * The anchor of the label
     */
    private final Point2D labelAnchor;
    
    /**
     * The location of the label
     */
    private final Point2D labelLocation;
    
    /**
     * The angle of the label
     */
    private double angleRad;
    
    /**
     * The paint of the label
     */
    private Paint paint;
    
    /**
     * The font of the label
     */
    private Font font;
    
    /**
     * Whether labels are transformed with the graphics transform
     */
    private boolean transformingLabels;

    /**
     * Describes the state of a label that is about to be painted.
     */
    private final LabelPaintState labelPaintState;
    
    /**
     * The condition that says whether a label should be painted.
     * If this is <code>null</code>, then the label will always
     * be painted.
     */
    private Predicate<LabelPaintState> labelPaintingCondition;
    
    /**
     * Creates a new label painter.<br>
     * <ul>
     *   <li>
     *     The {@link #setLabelAnchor(double, double) label anchor} will be
     *     at (0.5, 0.5), causing the label to be centered at the desired
     *     location
     *   </li>
     *   <li>
     *     The {@link #setLabelLocation(double, double) label location} will 
     *     be at (0.0, 0.0)
     *   </li>
     *   <li>
     *     The {@link #setAngle(double) angle} will be 0.0.  
     *   </li>
     *   <li>
     *     The painter will be {@link #setTransformingLabels(boolean)
     *     transforming} the labels
     *   </li>
     *   <li>
     *     The {@link #setFont(Font) font}, {@link #setPaint(Paint) paint}
     *     and {@link #setLabelPaintingCondition(Predicate) label painting
     *     condition} will be <code>null</code>, causing the label to always
     *     be painted, with the default font and paint of the graphics context  
     *   </li>
     *   
     * </ul>
     */
    public LabelPainter()
    {
        this.labelAnchor = new Point2D.Double(0.5, 0.5);
        this.labelLocation = new Point2D.Double(0.0, 0.0);
        this.angleRad = 0.0;
        this.font = null;
        this.paint = null;
        this.transformingLabels = true;
        this.labelPaintingCondition = null;
        
        this.labelPaintState = new LabelPaintState();
    }
    
    /**
     * Set the font that should be used for the label. If the given font
     * is <code>null</code>, then the font of the graphics context will 
     * be used.
     * 
     * @param font The font
     */
    public void setFont(Font font)
    {
        this.font = font;
    }
    
    /**
     * Returns the font that is used for the label. This may be 
     * <code>null</code> if no font was set explicitly.
     * 
     * @return The font
     */
    public Font getFont()
    {
        return font;
    }
    
    /**
     * Set the paint that should be used for the label. If the given paint
     * is <code>null</code>, then the current paint of the graphics context
     * will be used.
     * 
     * @param paint The paint
     */
    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }
    
    /**
     * Returns the paint that is used for the label. This may be 
     * <code>null</code> if no paint was set explicitly.
     * 
     * @return The paint
     */
    public Paint getPaint()
    {
        return paint;
    }
    
    /**
     * Set the anchor position of the label. The anchor position describes
     * a point <i>relative</i> to the bounding box of the label (so it is
     * usually - but not necessarily - a point in [(0,0)...(1,1)]). <br>
     * <br>
     * This is the point that will be placed at the 
     * {@link #setLabelLocation(double, double) label location}. For example,
     * in order to place the <i>center</i> of the label at a certain location,
     * an anchor point of (0.5, 0.5) may be set.<br> 
     * <br>
     * This is also the point that the label is rotated about, when a non-zero
     * {@link #setAngle(double) rotation angle} is set.<br>
     * <br>
     * 
     * @param x The x-coordinate of the anchor position
     * @param y The y-coordinate of the anchor position
     */
    public void setLabelAnchor(double x, double y)
    {
        labelAnchor.setLocation(x, y);
    }
    
    /**
     * Returns a new point that describes the current anchor position of
     * the label. See {@link #setLabelAnchor(double, double)}.
     * 
     * @return The new point storing the anchor position
     */
    public Point2D getLabelAnchor()
    {
        return new Point2D.Double(labelAnchor.getX(), labelAnchor.getY());
    }

    
    /**
     * Set the absolute location of the label. This is the location of the
     * label, relative to the origin of the world coordinate system that
     * this painter is painting to.
     * 
     * @param x The x coordinate of the location 
     * @param y The y-coordinate of the location
     */
    public void setLabelLocation(double x, double y)
    {
        labelLocation.setLocation(x, y);
    }
    
    /**
     * Returns a new point that describes the location of the label.
     * See {@link #setLabelLocation(double, double)}.
     * 
     * @return The new point storing the location
     */
    public Point2D getLabelLocation()
    {
        return new Point2D.Double(labelLocation.getX(), labelLocation.getY());
    }
    
    
    /**
     * Set the angle (in radians) about that the label will be rotated 
     * around its {@link #setLabelAnchor(double, double) anchor point}.
     * 
     * @param angleRad The rotation angle, in radians
     */
    public void setAngle(double angleRad)
    {
        this.angleRad = angleRad;
    }
    
    /**
     * Returns the angle (in radians) about that the label will be rotated
     * around its {@link #setLabelAnchor(double, double) anchor point}.
     * 
     * @return The rotation angle, in radians
     */
    public double getAngle()
    {
        return angleRad;
    }
    
    /**
     * Set whether this painter is transforming the labels using the 
     * <code>worldToScreen</code> transform that is passed to its
     * {@link #paint(Graphics2D, AffineTransform, double, double, String) paint}
     * method.<br>
     * <br>
     * If this is set to <code>true</code>, then the label strings will
     * be transformed with the <code>worldToScreen</code> transform, like
     * all other painted elements: When the view is scaled, then the
     * text will be scaled. When the view is rotated, then the labels
     * will be rotated.<br>
     * <br>
     * If this is set to <code>false</code>, then the labels will be
     * fixed. This means that they will always be painted with the
     * size that is defined via the {@link #setFont(Font) font}, and
     * they will always be oriented horizontally (unless they are 
     * rotated by this painter itself, via the {@link #setAngle(double) angle}).
     * 
     * @param transformingLabels Whether this painter is transforming the labels
     */
    public void setTransformingLabels(boolean transformingLabels)
    {
        this.transformingLabels = transformingLabels;
    }
    
    /**
     * Returns whether this painter is transforming the labels. See
     * {@link #setTransformingLabels(boolean)}.
     * 
     * @return Whether this painter is transforming the labels
     */
    public boolean isTransformingLabels()
    {
        return transformingLabels;
    }
    
    
    /**
     * Set the condition indicating whether a certain label should be
     * painted. <br>
     * <br>
     * The given predicate will receive the {@link LabelPaintState}
     * of each label that is about to be painted. It has to be assumed
     * that the same {@link LabelPaintState} instance will be used
     * for all these calls. So the predicate may not store and / or
     * modify the {@link LabelPaintState}.<br>
     * <br>
     * If the given condition is <code>null</code>, then the
     * labels will always be painted.
     * 
     * @param labelPaintingCondition The label painting condition
     */
    public void setLabelPaintingCondition(
        Predicate<LabelPaintState> labelPaintingCondition)
    {
        this.labelPaintingCondition = labelPaintingCondition;
    }
    
    
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h, String label)
    {
        if (label == null)
        {
            return;
        }
        g.setFont(font);
        g.setPaint(paint);
        if (transformingLabels)
        {
            drawStringTransformed(g, worldToScreen, label);
        }
        else
        {
            drawStringFixed(g, worldToScreen, label);
        }
    }
    
    /**
     * Draw the given string into the given graphics, transformed with
     * the given world-to-screen transform.
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param string The string to paint
     */
    private void drawStringTransformed(Graphics2D g, 
        AffineTransform worldToScreen, String string)
    {
        Rectangle2D labelBounds = 
            StringBoundsUtils.computeStringBounds(string, g.getFont());

        double absoluteLabelAnchorX = 
            computeAbsoluteX(labelBounds, labelAnchor);
        double absoluteLabelAnchorY = 
            computeAbsoluteY(labelBounds, labelAnchor);
        
        TEMP_AFFINE_TRANSFORM.setTransform(worldToScreen);
        TEMP_AFFINE_TRANSFORM.translate(
            labelLocation.getX(), labelLocation.getY());
        TEMP_AFFINE_TRANSFORM.rotate(angleRad);
        TEMP_AFFINE_TRANSFORM.translate(
            -absoluteLabelAnchorX, -absoluteLabelAnchorY);
        
        if (labelPaintingCondition != null)
        {
            labelPaintState.setLabel(string);
            labelPaintState.setLabelBounds(labelBounds);
            labelPaintState.setLabelTransform(TEMP_AFFINE_TRANSFORM);
            labelPaintState.setWorldToScreenTransform(worldToScreen);
            boolean shouldPaint = labelPaintingCondition.test(labelPaintState);
            if (!shouldPaint)
            {
                return;
            }
        }
        
        AffineTransform oldAt = g.getTransform();
        g.transform(TEMP_AFFINE_TRANSFORM);
        g.drawString(string, 0, 0);
        g.setTransform(oldAt);
    }
    
    
    /**
     * Draw the given string into the given graphics. This will draw
     * the string without transforming it with the world-to-screen
     * transform.
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param string The string to paint
     */
    private void drawStringFixed(Graphics2D g, 
        AffineTransform worldToScreen, String string)
    {
        Rectangle2D labelBounds = 
            StringBoundsUtils.computeStringBounds(string, g.getFont());

        double labelLocationScreenX = 
            AffineTransforms.computeX(worldToScreen, labelLocation);
        double labelLocationScreenY = 
            AffineTransforms.computeY(worldToScreen, labelLocation);
        double absoluteLabelAnchorX = 
            computeAbsoluteX(labelBounds, labelAnchor);
        double absoluteLabelAnchorY = 
            computeAbsoluteY(labelBounds, labelAnchor);

        TEMP_AFFINE_TRANSFORM.setToTranslation(
            labelLocationScreenX, labelLocationScreenY);
        TEMP_AFFINE_TRANSFORM.rotate(angleRad);
        TEMP_AFFINE_TRANSFORM.translate(
            -absoluteLabelAnchorX, -absoluteLabelAnchorY);
        
        if (labelPaintingCondition != null)
        {
            labelPaintState.setLabel(string);
            labelPaintState.setLabelBounds(labelBounds);
            labelPaintState.setLabelTransform(TEMP_AFFINE_TRANSFORM);
            labelPaintState.setWorldToScreenTransform(worldToScreen);
            boolean shouldPaint = labelPaintingCondition.test(labelPaintState);
            if (!shouldPaint)
            {
                return;
            }
        }

        AffineTransform oldAT = g.getTransform();
        g.transform(TEMP_AFFINE_TRANSFORM);
        g.drawString(string, 0, 0);
        g.setTransform(oldAT);
    }
 
    /**
     * Compute the absolute x-coordinate of the point that defines relative
     * coordinates in the given rectangle
     * 
     * @param rectangle The rectangle
     * @param relativePoint The relative point
     * @return The x-coordinate
     */
    private static double computeAbsoluteX(
        Rectangle2D rectangle, Point2D relativePoint)
    {
        return rectangle.getX() + rectangle.getWidth() * relativePoint.getX();
    }

    /**
     * Compute the absolute y-coordinate of the point that defines relative
     * coordinates in the given rectangle
     * 
     * @param rectangle The rectangle
     * @param relativePoint The relative point
     * @return The y-coordinate
     */
    private static double computeAbsoluteY(
        Rectangle2D rectangle, Point2D relativePoint)
    {
        return rectangle.getY() + rectangle.getHeight() * relativePoint.getY();
    }
    
    
}