/*
 * www.javagl.de - Cells 
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
package de.javagl.viewer.cells;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import de.javagl.geom.AffineTransforms;
import de.javagl.viewer.ObjectPainter;
import de.javagl.viewer.painters.LabelPainter;
import de.javagl.viewer.painters.StringBoundsUtils;

// TODO The scaling functions should probably be ToDoubleFunction


/**
 * Basic implementation of an {@link ObjectPainter} that can paint {@link Cell}
 * objects.<br>
 * <br>
 * It offers methods to set the... 
 * <ul>
 *   <li>fill paint (color) for cells and content</li>
 *   <li>draw paint (border color) for cells and content</li>
 *   <li>draw stroke (border style/thickness) for cells and content</li>
 *   <li>scaling factor for the cells and content</li>
 *   <li>label</li>
 *   <li>label font</li>
 *   <li>label paint (text color)</li>
 *   <li>label anchor and location</li>
 * </ul>
 * for all cells, or for each cell individually via a {@link Function}. <br>
 * <br>
 * Unless otherwise stated: If any of the given functions is <code>null</code>, 
 * or returns <code>null</code> for a particular cell, then the corresponding 
 * painting operation will not be performed.<br>
 * <br>
 * The existing methods already allow a fine-grained control over the
 * appearance of the cells painted by this cell painter. Classes that are 
 * extending this class for custom painting operations may override
 * any of the <code>protected</code> non-<code>final</code> methods:
 * <ul>
 *   <li>{@link #paintCell(
 *     Graphics2D, AffineTransform, double, double, Cell)}</li>
 *   <li>{@link #paintCellContent(
 *     Graphics2D, AffineTransform, double, double, Cell)}</li>
 *   <li>{@link #paintLabel(
 *     Graphics2D, AffineTransform, double, double, Cell)}</li>
 * </ul>   
 * For example, in order to paint custom cell contents, implementors will
 * usually only override the {@link #paintCellContent} method:
 * <pre><code>
 *     protected void paintCellContent(
 *         Graphics2D g, AffineTransform worldToScreen,
 *         double w, double h, Cell cell)
 *     {
 *     
 *         // Optionally, paint the content shape based on the
 *         // content fill paint, content draw paint and content draw stroke:
 *         paintCellContentShape(g, worldToScreen, w, h, cell);
 *         ...
 *         
 *         // Example: Draw a diagonal line through the content area:
 *         Line2D line = new Line2D.Double(0,0,1,1);
 *         g.draw(worldToScreen.createTransformedShape(line));
 *         
 *     }
 * </code></pre>
 */
public class BasicCellPainter implements ObjectPainter<Cell>
{
    /**
     * A unit rectangle
     */
    private static final Rectangle2D UNIT_RECTANGLE = 
        new Rectangle2D.Double(0,0,1,1);
    
    /**
     * A rectangle instance used for internal computations 
     */
    private static final Rectangle2D TEMP_RECTANGLE = new Rectangle2D.Double();

    /**
     * The function that provides the fill paint for the cells
     */
    private Function<? super Cell, ? extends Paint> 
        fillPaintFunction = null;

    /**
     * The function that provides the draw paint for the cells
     */
    private Function<? super Cell, ? extends Paint> 
        drawPaintFunction = null;

    /**
     * The function that provides the draw stroke for the cells
     */
    private Function<? super Cell, ? extends Stroke> 
        drawStrokeFunction = null;

    /**
     * The function that provides the fill paint for the cell content
     */
    private Function<? super Cell, ? extends Paint> 
        contentFillPaintFunction = null;

    /**
     * The function that provides the draw paint for the cell content
     */
    private Function<? super Cell, ? extends Paint> 
        contentDrawPaintFunction = null;

    /**
     * The function that provides the draw stroke for the cell content
     */
    private Function<? super Cell, ? extends Stroke> 
        contentDrawStrokeFunction = null;
    
    /**
     * The function that provides the labels for the cells
     */
    private Function<? super Cell, String> 
        labelFunction = null;

    /**
     * The function that provides the label paint for the cells
     */
    private Function<? super Cell, ? extends Paint> 
        labelPaintFunction = null;

    /**
     * The function that provides the label font for the cells
     */
    private Function<? super Cell, ? extends Font> 
        labelFontFunction = null;
    
    /**
     * The function that provides the label anchor for the cells
     */
    private Function<? super Cell, ? extends Point2D> 
        labelAnchorFunction = null;
    
    /**
     * The function that provides the label location for the cells
     */
    private Function<? super Cell, ? extends Point2D> 
        labelLocationFunction = null;
    
    /**
     * The painter for the labels
     */
    private final LabelPainter labelPainter;
    
    /**
     * Whether this painter is hiding long labels
     */
    private boolean hidingLongLabels;

    /**
     * The function that provides the scaling for the cells
     */
    private Function<? super Cell, ? extends Number> 
        scalingFunction = null;

    /**
     * The function that provides the scaling for the cell contents
     */
    private Function<? super Cell, ? extends Number> 
        contentScalingFunction = null;

    /**
     * The affine transform that is the concatenation of the 
     * world-to-screen transform and the scaling for a 
     * particular cell
     */
    private final AffineTransform scaledWorldToScreenTransform;

    /**
     * The affine transform for the cell contents
     */
    private final AffineTransform contentTransform;

    /**
     * A backup for the content transform. This is only used in the
     * {@link #paint(Graphics2D, AffineTransform, double, double, Cell)}
     * function, to avoid rendering errors for the case that an 
     * overridden {@link #paintCellContent(Graphics2D, 
     * AffineTransform, double, double, Cell)} implementation modifies
     * the transform that it receives.
     */
    private final AffineTransform backupContentTransform;
    
    /**
     * Creates a cell painter. <br>
     * <br>
     * By default, this painter will not paint anything, but it will
     * be initialized with the following default values:<br>
     * <br>
     * <ul>
     *   <li> 
     *     It will have a default {@link #setLabelFont(Font) label font},
     *     which is a <code>"Dialog"</code> font with a size of 10.0f
     *   </li>
     *   <li> 
     *     It will have a default {@link #setLabelAnchor(Point2D) label anchor}
     *     and {@link #setLabelLocation(Point2D) label location}, which are
     *     both (0.5, 0.5), so that the label is centered
     *   </li>
     *   <li> 
     *     It will not {@link #setTransformingLabels(boolean) transform 
     *     the labels}
     *   </li>
     *   <li> 
     *     It will {@link #setHidingLongLabels(boolean) hide long labels}
     *   </li>
     *   <li> 
     *     It will have a basic {@link #setDrawStroke(Stroke) draw stroke}
     *     and {@link #setContentDrawStroke(Stroke) content draw stroke}
     *     with a width of 1.0f
     *   </li>
     * </ul>  
     */
    public BasicCellPainter()
    {
        this.scaledWorldToScreenTransform = new AffineTransform();
        this.contentTransform = new AffineTransform();
        this.backupContentTransform = new AffineTransform();
        
        this.labelPainter = new LabelPainter();

        setLabelFont(new Font("Dialog", Font.PLAIN, 1).deriveFont(10.0f));
        setLabelAnchor(new Point2D.Double(0.5, 0.5));
        setLabelLocation(new Point2D.Double(0.5, 0.5));

        setTransformingLabels(false);
        setHidingLongLabels(true);
        
        setDrawStroke(new BasicStroke(1.0f));        
        setContentDrawStroke(new BasicStroke(1.0f));
        
    }
    
    /**
     * Set the fill paint that will be used for all cells
     * 
     * @param paint The paint
     */
    public final void setFillPaint(Paint paint)
    {
        if (paint == null)
        {
            this.fillPaintFunction = null;
        }
        else
        {
            this.fillPaintFunction = (cell) -> (paint);
        }
    }
    
    /**
     * Set the function that provides the fill paint for the cells.
     * 
     * @param fillPaintFunction The function
     */
    public final void setFillPaintFunction(
        Function<? super Cell, ? extends Paint> fillPaintFunction)
    {
        this.fillPaintFunction = fillPaintFunction;
    }

    /**
     * Set the draw paint that will be used for all cells.<br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setDrawStroke(Stroke) draw stroke} is set.
     * 
     * @param paint The paint
     */
    public final void setDrawPaint(Paint paint)
    {
        if (paint == null)
        {
            this.drawPaintFunction = null;
        }
        else
        {
            this.drawPaintFunction = (cell) -> (paint);
        }
    }
    
    /**
     * Set the function that provides the draw paint for the cells.<br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setDrawStroke(Stroke) draw stroke} is set.
     * 
     * @param drawPaintFunction The function
     */
    public final void setDrawPaintFunction(
        Function<? super Cell, ? extends Paint> drawPaintFunction)
    {
        this.drawPaintFunction = drawPaintFunction;
    }

    
    /**
     * Set the draw stroke that will be used for all cells. <br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setDrawPaint(Paint) draw paint} has been set. 
     * 
     * @param stroke The stroke
     */
    public final void setDrawStroke(Stroke stroke)
    {
        if (stroke == null)
        {
            this.drawStrokeFunction = null;
        }
        else
        {
            this.drawStrokeFunction = (cell) -> (stroke);
        }
    }
    
    /**
     * Set the function that provides the draw stroke for the cells. <br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setDrawPaint(Paint) draw paint} has been set.
     * 
     * @param drawStrokeFunction The function
     */
    public final void setDrawStrokeFunction(
        Function<? super Cell, ? extends Stroke> drawStrokeFunction)
    {
        this.drawStrokeFunction = drawStrokeFunction;
    }
    

    
    /**
     * Set the fill paint that will be used for all cell contents
     * 
     * @param paint The paint
     */
    public final void setContentFillPaint(Paint paint)
    {
        if (paint == null)
        {
            this.contentFillPaintFunction = null;
        }
        else
        {
            this.contentFillPaintFunction = (cell) -> (paint);
        }
    }
    
    /**
     * Set the function that provides the fill paint for the cell contents.
     * 
     * @param fillPaintFunction The function
     */
    public final void setContentFillPaintFunction(
        Function<? super Cell, ? extends Paint> fillPaintFunction)
    {
        this.contentFillPaintFunction = fillPaintFunction;
    }

    /**
     * Set the draw paint that will be used for all cell contents. <br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setContentDrawStroke(Stroke) content draw stroke} has been set.
     * 
     * @param paint The paint
     */
    public final void setContentDrawPaint(Paint paint)
    {
        if (paint == null)
        {
            this.contentDrawPaintFunction = null;
        }
        else
        {
            this.contentDrawPaintFunction = (cell) -> (paint);
        }
    }
    
    /**
     * Set the function that provides the draw paint for the cell contents.
     * <br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setContentDrawStroke(Stroke) content draw stroke} has been set.
     * 
     * @param drawPaintFunction The function
     */
    public final void setContentDrawPaintFunction(
        Function<? super Cell, ? extends Paint> drawPaintFunction)
    {
        this.contentDrawPaintFunction = drawPaintFunction;
    }

    
    /**
     * Set the draw stroke that will be used for all cell contents. <br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setContentDrawPaint(Paint) content draw paint} has been set.
     * 
     * @param stroke The stroke
     */
    public final void setContentDrawStroke(Stroke stroke)
    {
        if (stroke == null)
        {
            this.contentDrawStrokeFunction = null;
        }
        else
        {
            this.contentDrawStrokeFunction = (cell) -> (stroke);
        }
    }
    
    /**
     * Set the function that provides the draw stroke for the cells. <br>
     * <br>
     * This will not cause anything to be painted, unless a 
     * {@link #setContentDrawPaint(Paint) content draw paint} has been set.
     * 
     * @param drawStrokeFunction The function
     */
    public final void setContentDrawStrokeFunction(
        Function<? super Cell, ? extends Stroke> drawStrokeFunction)
    {
        this.contentDrawStrokeFunction = drawStrokeFunction;
    }
    
    
    /**
     * Set the function that provides the labels for the cells
     * 
     * @param labelFunction The function
     */
    public final void setLabelFunction(
        Function<? super Cell, String> labelFunction)
    {
        this.labelFunction = labelFunction;
    }

    
    /**
     * Set the label font that will be used for all cells. <br>
     * <br>
     * If this is <code>null</code>, then a default font will be used.
     * 
     * @param font The font
     */
    public final void setLabelFont(Font font)
    {
        if (font == null)
        {
            this.labelFontFunction = null;
        }
        else
        {
            this.labelFontFunction = (cell) -> (font);
        }
    }
    
    /**
     * Set the function that provides the label font for the cells.<br>
     * <br>
     * If this is function is <code>null</code> or returns <code>null</code> 
     * for a particular cell, then a default font will be used.
     * 
     * @param labelFontFunction The function
     */
    public final void setLabelFontFunction(
        Function<? super Cell, ? extends Font> labelFontFunction)
    {
        this.labelFontFunction = labelFontFunction;
    }
    
    
    /**
     * Set the label paint that will be used for all cells
     * 
     * @param paint The paint
     */
    public final void setLabelPaint(Paint paint)
    {
        if (paint == null)
        {
            this.labelPaintFunction = null;
        }
        else
        {
            this.labelPaintFunction = (cell) -> (paint);
        }
    }
    
    /**
     * Set the function that provides the label paint for the cells
     * 
     * @param labelPaintFunction The function
     */
    public final void setLabelPaintFunction(
        Function<? super Cell, ? extends Paint> labelPaintFunction)
    {
        this.labelPaintFunction = labelPaintFunction;
    }
    
    
    /**
     * Set the label anchor that will be used for all cells. If this anchor
     * is <code>null</code>, then a default anchor of (0.5, 0.5) will be
     * used for all cells. A copy of the given point will be created, so
     * that changes in the given anchor point will not affect this painter.
     * 
     * @param anchor The anchor
     */
    public final void setLabelAnchor(Point2D anchor)
    {
        if (anchor == null)
        {
            this.labelAnchorFunction = null;
        }
        else
        {
            Point2D localAnchor = 
                new Point2D.Double(anchor.getX(), anchor.getY());
            this.labelAnchorFunction = (cell) -> (localAnchor);
        }
    }

    /**
     * Set the function that provides the label anchor for the cells.<br>
     * <br>
     * If the given function is <code>null</code> or returns <code>null</code>
     * for a particular cell, then a default anchor of (0.5, 0.5) will be 
     * used. 
     * 
     * @param labelAnchorFunction The function
     */
    public final void setLabelAnchorFunction(
        Function<? super Cell, ? extends Point2D> labelAnchorFunction)
    {
        this.labelAnchorFunction = labelAnchorFunction;
    }
    
    
    /**
     * Set the label location that will be used for all cells. If this location
     * is <code>null</code>, then a default location of (0.5, 0.5) will be
     * used for all cells. A copy of the given point will be created, so
     * that changes in the given location point will not affect this painter.
     * 
     * @param location The location
     */
    public final void setLabelLocation(Point2D location)
    {
        if (location == null)
        {
            this.labelLocationFunction = null;
        }
        else
        {
            Point2D localLocation = 
                new Point2D.Double(location.getX(), location.getY());
            this.labelLocationFunction = (cell) -> (localLocation);
        }
    }

    /**
     * Set the function that provides the label location for the cells.<br>
     * <br>
     * If the given function is <code>null</code> or returns <code>null</code>
     * for a particular cell, then a default location of (0.5, 0.5) will be 
     * used. 
     * 
     * @param labelLocationFunction The function
     */
    public final void setLabelLocationFunction(
        Function<? super Cell, ? extends Point2D> labelLocationFunction)
    {
        this.labelLocationFunction = labelLocationFunction;
    }
    
    
    /**
     * Set whether this painter is transforming the labels.<br>
     * <br>
     * If this is <code>true</code>, then the labels will be scaled and 
     * rotated depending on the world-to-screen transform. This usually only 
     * makes sense for labels with a {@link #getLabelFont(Cell) label font}
     * whose size is smaller than the size of the {@link Cell}s.<br>
     * <br>
     * If this is <code>false</code>, then the labels will only be 
     * <i>placed</i> based on the world-to-screen transform, but the
     * string that is actually painted will not be transformed. This will
     * cause the labels to always be oriented horizontally, regardless
     * of the rotation of the viewer. It will also cause the labels to
     * be painted with the {@link #getLabelFont(Cell) label font} that
     * is returned for the given cell, regardless of the scaling that
     * is applied in the viewer.
     *  
     * @param transformingLabels Whether this painter is transforming labels
     */
    public void setTransformingLabels(boolean transformingLabels)
    {
        labelPainter.setTransformingLabels(transformingLabels);
    }

    /**
     * Returns whether this painter is transforming the labels.<br>
     * <br>
     * See {@link #setTransformingLabels(boolean)} for details.
     * 
     * @return Whether this painter is transforming labels
     */
    public boolean isTransformingLabels()
    {
        return labelPainter.isTransformingLabels();
    }

    
    /**
     * Set whether this painter is hiding long labels.<br>
     * <br>
     * If this is <code>true</code>, then the labels will be hidden when
     * the bounds of the label text on the screen exceed the space that
     * is available for the respective cell content.
     *  
     * @param hidingLongLabels Whether this painter is hiding long labels
     */
    public void setHidingLongLabels(boolean hidingLongLabels)
    {
        this.hidingLongLabels = hidingLongLabels;
    }

    /**
     * Returns whether this painter is hiding long labels.<br>
     * <br>
     * See {@link #setHidingLongLabels(boolean)} for details.
     * 
     * @return Whether this painter is hiding long labels
     */
    public boolean isHidingLongLabels()
    {
        return hidingLongLabels;
    }
    
    
    /**
     * Set the scaling that will be used for all cells
     * 
     * @param scaling The scaling
     */
    public final void setScaling(double scaling)
    {
        if (Math.abs(scaling - 1.0) < 1e-6)
        {
            this.scalingFunction = null;
        }
        else
        {
            this.scalingFunction = (cell) -> (scaling);
        }
    }
    
    /**
     * Set the function that provides the scaling for the cells.<br>
     * <br>
     * If the function is <code>null</code> or returns <code>null</code> for 
     * a particular cell, then a scaling of <code>1.0</code> will be used.
     * 
     * @param scalingFunction The function
     */
    public final void setScalingFunction(
        Function<? super Cell, ? extends Number> scalingFunction)
    {
        this.scalingFunction = scalingFunction;
    }

    /**
     * Set the function that provides the scaling for the cells.<br>
     * <br>
     * This is just an overload of 
     * {@link #setScalingFunction(Function)} that accepts a 
     * <code>ToDoubleFunction</code>.
     * 
     * @param scalingFunction The function
     */
    public final void setScalingDoubleFunction(
        final ToDoubleFunction<? super Cell> scalingFunction)
    {
        if (scalingFunction == null)
        {
            this.scalingFunction = null;
        }
        else
        {
            this.scalingFunction = new Function<Cell, Number>()
            {
                @Override
                public Number apply(Cell cell)
                {
                    return scalingFunction.applyAsDouble(cell);
                }
            };
        }
    }
    
    
    /**
     * Set the scaling that will be used for all cell contents
     * 
     * @param scaling The scaling
     */
    public final void setContentScaling(double scaling)
    {
        if (Math.abs(scaling - 1.0) < 1e-6)
        {
            this.contentScalingFunction = null;
        }
        else
        {
            this.contentScalingFunction = (cell) -> (scaling);
        }
    }
    
    /**
     * Set the function that provides the scaling for the cell contents.<br>
     * <br>
     * If the function is <code>null</code> or returns <code>null</code> for 
     * a particular cell, then a scaling of <code>1.0</code> will be used.
     * 
     * @param scalingFunction The function
     */
    public final void setContentScalingFunction(
        Function<? super Cell, ? extends Number> scalingFunction)
    {
        this.contentScalingFunction = scalingFunction;
    }
    
    /**
     * Set the function that provides the scaling for the cell contents.<br>
     * <br>
     * This is just an overload of 
     * {@link #setContentScalingFunction(Function)} that accepts a 
     * <code>ToDoubleFunction</code>.
     * 
     * @param contentScalingFunction The function
     */
    public final void setContentScalingDoubleFunction(
        final ToDoubleFunction<? super Cell> contentScalingFunction)
    {
        if (contentScalingFunction == null)
        {
            this.contentScalingFunction = null;
        }
        else
        {
            this.contentScalingFunction = new Function<Cell, Number>()
            {
                @Override
                public Number apply(Cell cell)
                {
                    return contentScalingFunction.applyAsDouble(cell);
                }
            };
        }
    }
    
    
    /**
     * Returns the fill paint for the given cell.<br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The paint
     */
    protected final Paint getFillPaint(Cell cell)
    {
        if (fillPaintFunction == null)
        {
            return null;
        }
        return fillPaintFunction.apply(cell);
    }
    
    /**
     * Returns the draw paint for the given cell.<br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The paint
     */
    protected final Paint getDrawPaint(Cell cell)
    {
        if (drawPaintFunction == null)
        {
            return null;
        }
        return drawPaintFunction.apply(cell);
    }

    /**
     * Returns the draw stroke for the given cell.<br>
     * <br> 
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The stroke
     */
    protected final Stroke getDrawStroke(Cell cell)
    {
        if (drawStrokeFunction == null)
        {
            return null;
        }
        return drawStrokeFunction.apply(cell);
    }
    
    
    /**
     * Returns the fill paint for the given cell content.<br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The paint
     */
    protected final Paint getContentFillPaint(Cell cell)
    {
        if (contentFillPaintFunction == null)
        {
            return null;
        }
        return contentFillPaintFunction.apply(cell);
    }
    
    /**
     * Returns the draw paint for the given cell content.<br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The paint
     */
    protected final Paint getContentDrawPaint(Cell cell)
    {
        if (contentDrawPaintFunction == null)
        {
            return null;
        }
        return contentDrawPaintFunction.apply(cell);
    }

    /**
     * Returns the draw stroke for the given cell content.<br>
     * <br> 
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The stroke
     */
    protected final Stroke getContentDrawStroke(Cell cell)
    {
        if (contentDrawStrokeFunction == null)
        {
            return null;
        }
        return contentDrawStrokeFunction.apply(cell);
    }
    
    /**
     * Returns the scaling for the given cell.<br>
     * <br>
     * If the {@link #setScalingFunction(Function) scaling function} is
     * <code>null</code> or returns <code>null</code> for the given cell,
     * then a default scaling factor of 1.0 will be returned.
     * 
     * @param cell The cell
     * @return The scaling
     */
    protected final double getScaling(Cell cell)
    {
         if (scalingFunction == null)
         {
             return 1.0;
         }
         Number scaling = scalingFunction.apply(cell);
         if (scaling == null)
         {
             return 1.0;
         }
         return scaling.doubleValue();
    }

    /**
     * Returns the scaling for the given cell content.<br>
     * <br>
     * If the {@link #setContentScalingFunction(Function) content scaling 
     * function} is <code>null</code> or returns <code>null</code> for the 
     * given cell, then a default scaling factor of 1.0 will be returned.
     * 
     * @param cell The cell
     * @return The content scaling
     */
    protected final double getContentScaling(Cell cell)
    {
         if (contentScalingFunction == null)
         {
             return 1.0;
         }
         Number contentScaling = contentScalingFunction.apply(cell);
         if (contentScaling == null)
         {
             return 1.0;
         }
         return contentScaling.doubleValue();
    }

    /**
     * Returns the label font for the given cell.<br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The font
     */
    protected final Font getLabelFont(Cell cell)
    {
        if (labelFontFunction == null)
        {
            return null;
        }
        return labelFontFunction.apply(cell);
    }
    
    /**
     * Returns the label paint for the given cell.<br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The paint
     */
    protected final Paint getLabelPaint(Cell cell)
    {
        if (labelPaintFunction == null)
        {
            return null;
        }
        return labelPaintFunction.apply(cell);
    }

    /**
     * Returns the label for the given cell.<br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The paint
     */
    protected final String getLabel(Cell cell)
    {
        if (labelFunction == null)
        {
            return null;
        }
        return labelFunction.apply(cell);
    }
    
    /**
     * Returns the anchor for the label of the given cell.<br>
     * <br>
     * <b><u>WARNING</u>: The returned point may <u>NOT</u> be 
     * modified by the caller!</b><br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The label anchor
     */
    protected final Point2D getLabelAnchor(Cell cell)
    {
        if (labelAnchorFunction == null)
        {
            return null;
        }
        return labelAnchorFunction.apply(cell);
    }
    
    /**
     * Returns the location for the label of the given cell.<br>
     * <br>
     * <b><u>WARNING</u>: The returned point may <u>NOT</u> be 
     * modified by the caller!</b><br>
     * <br>
     * Returns <code>null</code> if the corresponding painting operation
     * should be skipped.
     * 
     * @param cell The cell
     * @return The label location
     */
    protected final Point2D getLabelLocation(Cell cell)
    {
        if (labelLocationFunction == null)
        {
            return null;
        }
        return labelLocationFunction.apply(cell);
    }
    
    
    /**
     * Compute the scaled transform for the given cell, by concatenating
     * the given base transform with a transform that scales the given 
     * cell about its center by the scaling factor that is given via
     * the given scaling function
     * 
     * @param baseTransform The base transform 
     * @param scalingFunction  The scaling function. If this is 
     * <code>null</code> or returns <code>null</code> for the given
     * cell, then no scaling will be applied, and the result will 
     * be the base transform
     * @param cell The cell
     * @param result The transform that will store the result
     */
    private static void computeScaledTransform(
        AffineTransform baseTransform, 
        Function<? super Cell, ? extends Number> scalingFunction,
        Cell cell, AffineTransform result)
    {
        result.setTransform(baseTransform);
        if (scalingFunction != null)
        {
            Number scaling = scalingFunction.apply(cell);
            if (scaling != null)
            {
                double s = scaling.doubleValue();
                double centerX = cell.getCenterX();
                double centerY = cell.getCenterY();
                result.translate(centerX, centerY);
                result.scale(s, s);
                result.translate(-centerX, -centerY);
            }
        }
    }
    

    @Override
    public void paint(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, Cell cell)
    {
        computeScaledTransform(worldToScreen, scalingFunction, cell, 
            scaledWorldToScreenTransform);
        paintCell(g, scaledWorldToScreenTransform, w, h, cell);

        computeScaledTransform(worldToScreen, contentScalingFunction, cell, 
            contentTransform);
        cell.concatenateWithContentTransform(
            contentTransform, contentTransform);
        Shape contentArea = AffineTransforms.createTransformedShape(
            contentTransform, UNIT_RECTANGLE);
        if (g.getClip().intersects(contentArea.getBounds2D()))
        {
            backupContentTransform.setTransform(contentTransform);
            paintCellContent(g, contentTransform, w, h, cell);
            paintLabel(g, backupContentTransform, w, h, cell);
        }
    }
    

    /**
     * Perform the painting operations for the cell (the "background")
     * on the given Graphics. The default implementation will only perform 
     * the default cell shape painting, by calling {@link #paintCellShape}.<br>
     * <br>
     * The given world-to-screen transform is the transform that transforms
     * the {@link Cell#getShape() shape} of the cell from world coordinates
     * to its tiled position in screen coordinates, possibly concatenated 
     * with a {@link #setScaling(double) scaling}.
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param cell The object to paint
     */
    protected void paintCell(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, Cell cell)
    {
        paintCellShape(g, worldToScreen, w, h, cell);
    }
    
    
    /**
     * Perform the painting operations for the cell shape (the "background")
     * on the given Graphics.<br>
     * <br>
     * When the {@link #getFillPaint(Cell) fill paint} for the given cell 
     * is not <code>null</code>, then this method will fill the 
     * {@link Cell#getShape() cell shape} with this paint, by calling
     * {@link #fillShapeWithTransformedGraphics}.
     * <br>
     * When the {@link #getDrawPaint(Cell) draw paint} and the
     * {@link #getDrawStroke(Cell) draw stroke} of the given cell 
     * are not <code>null</code>, then this method will draw the 
     * {@link Cell#getShape() cell shape} by calling 
     * {@link #drawTransformedShape}.
     * <br>
     * The given world-to-screen transform is the transform that transforms
     * the {@link Cell#getShape() shape} of the cell from world coordinates
     * to its tiled position in screen coordinates, concatenated 
     * with a {@link #getScaling(Cell) scaling} for the cell.
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param cell The object to paint
     */
    protected final void paintCellShape(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, Cell cell)
    {
        Paint fillPaint = getFillPaint(cell);
        if (fillPaint != null)
        {
            fillShapeWithTransformedGraphics(
                g, worldToScreen, w, h, cell.getShape(), fillPaint);
        }
        Paint drawPaint = getDrawPaint(cell);
        Stroke drawStroke = getDrawStroke(cell);
        if (drawPaint != null && drawStroke != null)
        {
            drawTransformedShape(
                g, worldToScreen, w, h, cell.getShape(), drawPaint, drawStroke);
        }
    }
    
    /**
     * Perform the painting operations for the cell content (the "foreground")
     * on the given Graphics. This method will only be called when the content
     * area of the cell intersects the clipping area of the graphics. The
     * default implementation will only perform the default cell content
     * shape painting, by calling {@link #paintCellContentShape}.<br>
     * <br>
     * The given world-to-screen transform is the transform that transforms
     * the unit rectangle to the screen rectangle of the content area of
     * the given cell, concatenated with the 
     * {@link #getContentScaling(Cell) content scaling for the cell}.
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param cell The object to paint
     */
    protected void paintCellContent(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, Cell cell)
    {
        paintCellContentShape(g, worldToScreen, w, h, cell);
    }
    
    /**
     * Perform the painting operations for the cell content (the "foreground")
     * on the given Graphics. This method will only be called when the content
     * area of the cell intersects the clipping area of the graphics.<br>
     * <br>
     * When the {@link #getFillPaint(Cell) fill paint} for the given cell 
     * is not <code>null</code>, then this method will fill the 
     * unit rectangle with this paint, by calling
     * {@link #fillShapeWithTransformedGraphics}.
     * <br>
     * When the {@link #getContentDrawPaint(Cell) content draw paint} and the
     * {@link #getContentDrawStroke(Cell) content draw stroke} of the given 
     * cell are not <code>null</code>, then this method will draw the unit 
     * rectangle by calling {@link #drawTransformedShape}.
     * <br>
     * The given world-to-screen transform is the transform that transforms
     * the unit rectangle to the screen rectangle of the content area of
     * the given cell, possibly concatenated with a 
     * {@link #setContentScaling(double) content scaling}. 
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param cell The object to paint
     */
    protected final void paintCellContentShape(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, Cell cell)
    {
        Paint contentFillPaint = getContentFillPaint(cell);
        if (contentFillPaint != null)
        {
            fillShapeWithTransformedGraphics(
                g, worldToScreen, w, h, UNIT_RECTANGLE, contentFillPaint);
        }
        Paint contentDrawPaint = getContentDrawPaint(cell);
        Stroke contentDrawStroke = getContentDrawStroke(cell);
        if (contentDrawPaint != null && contentDrawStroke != null)
        {
            drawTransformedShape(g, worldToScreen, w, h, UNIT_RECTANGLE, 
                contentDrawPaint, contentDrawStroke);
        }
    }
    
    /**
     * Fill the given shape in the given Graphics, by transforming the
     * given graphics with the given transform, setting the given paint,
     * filling the given shape, and restoring the original transform. 
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param shape The shape to paint
     * @param fillPaint The paint that is used for filling
     */
    protected final void fillShapeWithTransformedGraphics(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, Shape shape, Paint fillPaint)
    {
        AffineTransform oldAt = g.getTransform();
        g.transform(worldToScreen);
        g.setPaint(fillPaint);
        g.fill(shape);
        g.setTransform(oldAt);
    }
    
    
    /**
     * Draw the given shape to the given Graphics, by setting the given paint
     * and stroke, and drawing a shape that was created by transforming
     * the given shape with the given transform. 
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param shape The shape to paint
     * @param drawPaint The paint that is used for drawing
     * @param drawStroke The stroke that is used for drawing
     */
    protected final void drawTransformedShape(
        Graphics2D g, AffineTransform worldToScreen,
        double w, double h, Shape shape, Paint drawPaint, Stroke drawStroke)
    {
        Shape paintedShape = AffineTransforms.createTransformedShape(
            worldToScreen, shape);
        g.setPaint(drawPaint);
        g.setStroke(drawStroke);
        g.draw(paintedShape);
    }
    
    
    
    /**
     * Perform the painting operations for the cell label on the given 
     * Graphics. This method will only be called when the content
     * area of the cell intersects the clipping area of the graphics. The
     * default implementation will only perform the default label
     * painting, by calling {@link #paintLabelString}.<br>
     * <br>
     * The given world-to-screen transform is the transform that transforms
     * the unit rectangle to the screen rectangle of the content area of
     * the given cell, concatenated with the 
     * {@link #getContentScaling(Cell) content scaling for the cell}.
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param cell The cell whose label should be painted
     */
    protected void paintLabel(Graphics2D g,
        AffineTransform worldToScreen, double w, double h, Cell cell)
    {
        paintLabelString(g, worldToScreen, w, h, cell, 20.0);
    }
    
    /**
     * Paints the label for the given cell, if it is likely that there is 
     * enough space for it.<br>
     * <br>
     * 
     * @param g The Graphics used for painting 
     * @param worldToScreen The world-to-screen transform
     * @param w The width of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param h The height of the area, in screen coordinates, for 
     * which this painter is responsible 
     * @param cell The object to paint
     * @param screenSpaceThresholdX The threshold for the available
     * screen space that is necessary in order to perform a precise
     * test of whether the actual string fits into the actual screen
     * space. This may, for example, be set to 20.0, in order to avoid
     * complex computations of string bounds for cells that have a size
     * of less than 20 pixels.
     */
    protected final void paintLabelString(Graphics2D g, 
        AffineTransform worldToScreen, double w, double h, Cell cell,
        double screenSpaceThresholdX)
    {
        String label = getLabel(cell);
        if (label == null)
        {
            return;
        }
        Paint labelPaint = getLabelPaint(cell);
        if (labelPaint == null)
        {
            return;
        }
        Font font = getLabelFont(cell);
        if (font == null)
        {
            return;
        }
        Point2D labelAnchor = getLabelAnchor(cell);
        if (labelAnchor == null)
        {
            return;
        }
        Point2D labelLocation = getLabelLocation(cell);
        if (labelLocation == null)
        {
            return;
        }
        
        double screenSpaceX = 
            AffineTransforms.computeDistanceX(worldToScreen, 1.0);
        if (screenSpaceX < screenSpaceThresholdX)
        {
            return;
        }
        
        if (hidingLongLabels)
        {
            Rectangle2D labelBounds = 
                StringBoundsUtils.computeStringBounds(
                    label, font, TEMP_RECTANGLE);
            if (isTransformingLabels())
            {
                if (labelBounds.getWidth() > 1.0)
                {
                    return;
                }
            }
            if (labelBounds.getWidth() > screenSpaceX)
            {
                return;
            }
        }
        
        g.setPaint(labelPaint);
        g.setFont(font);
        
        labelPainter.setLabelAnchor(
            labelAnchor.getX(), labelAnchor.getY());
        labelPainter.setLabelLocation(
            labelLocation.getX(), labelLocation.getY());
        labelPainter.paint(g, worldToScreen, w, h, label);
    }
}



