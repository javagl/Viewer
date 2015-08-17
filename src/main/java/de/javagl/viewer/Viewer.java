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
package de.javagl.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;

/**
 * A panel that allows translating, rotating and zooming. <br>
 * <br>
 * Instances of classes implementing the {@link Painter} interface may be 
 * added to this viewer and perform painting operations. Their 
 * {@link Painter#paint(Graphics2D, AffineTransform, double, double) paint}
 * method will receive the world-to-screen transform, as determined by the
 * current translation, rotation and zoom of the viewer. <br>
 * <br>
 * The viewer supports multiple layers. The layer may be specified when a 
 * {@link Painter} is added using the {@link #addPainter(Painter, int)}
 * method. The {@link Painter} instances will be called starting at the 
 * lowest layer. Inside one layer, the {@link Painter}s will be called in 
 * the order in which they have been added.<br>
 * <br>
 * A viewer has several methods that affect the translation, rotation and
 * zoom factor:<br>
 * <br>
 * <ul>
 *     <li>{@link #translate(double, double)}</li>
 *     <li>{@link #rotate(double, double, double)}</li>
 *     <li>{@link #zoom(double, double, double, double)}</li>
 * </ul>
 * These methods will usually not be called by clients or users of the
 * viewer. Instead, they are called by a class that implements the
 * {@link MouseControl} interface, and has been set using 
 * {@link #setMouseControl(MouseControl)}. These classes are responsible for
 * making sure that these methods are called with valid parameters. 
 * Particularly, they should not be called with <code>NaN</code> or 
 * infinite values, and the zooming factors should not be 0.0. The viewer
 * class itself does not perform any sanity checks on these arguments. 
 */
public class Viewer extends JPanel 
{
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3252732941609348700L;
    
    /**
     * The map from each layer to the set of {@link Painter}s that will 
     * perform painting operations in the {@link #paintComponent(Graphics)} 
     * method.
     */
    private final Map<Integer, Set<Painter>> painters;
    
    /**
     * The transformation of this viewer
     */
    private final AffineTransform transform;
    
    /**
     * The inverse transform, computed on demand, and set to <code>null</code>
     * when it is invalidated.
     */
    private AffineTransform inverseTransform = null;
    
    /**
     * The area in world coordinates covered by this panel
     */
    private final Rectangle2D worldArea;
    
    /**
     * A pending size for the world area. When 
     * {@link #setDisplayedWorldArea(Rectangle2D)} is called before
     * this component is visible, the intended area has to be stored
     * until the component becomes visible and the area update can
     * actually be applied.
     */
    private Rectangle2D pendingWorldArea = null;
    
    /**
     * The state of the {@link #maintainAspectRatio} flag when the
     * {@link #pendingWorldArea} was set
     */
    private boolean pendingWorldAreaMaintainAspectRatioState = false;
    
    /**
     * The last stored screen size
     */
    private Dimension previousSize = null;
    
    /**
     * Whether the y-axis should be flipped to point from the
     * bottom of the screen to the top
     */
    private boolean flippedVertically = false;

    /**
     * A transform that will be concatenated with the world-to-screen
     * transform, for example, to flip the y-axis vertically
     */
    private AffineTransform basicWorldToScreenTransform = null;

    
    /**
     * Whether the contents should be resized when the screen
     * is resized
     */
    private boolean resizingContents = false;
    
    /**
     * This flag determines whether the aspect ratio should be maintained 
     * during resize operations (when resizingContents is <code>true</code>)
     * or when {@link #setDisplayedWorldArea(Rectangle2D)} is called.
     */
    private boolean maintainAspectRatio = true;
    
    /**
     * Whether the antialiasing rendering hint should be enabled
     * by default
     */
    private boolean antialiasing = true;
    
    /**
     * The {@link MouseControl} that is attached to this viewer
     */
    private MouseControl mouseControl;
    
    /**
     * Creates a new Viewer.<br>
     * <br>
     * The default settings are as follows:
     * <ul>
     *   <li>{@link #isResizingContents()} is <code>false</code></li>
     *   <li>{@link #isMaintainAspectRatio()} is <code>true</code></li>
     *   <li>{@link #isAntialiasing()} is <code>true</code></li>
     * </ul>
     * A default {@link MouseControls#createDefault(Viewer, boolean, boolean) 
     * MouseControl}, where rotation and non-uniform scaling are allowed,
     * will be installed by calling {@link #setMouseControl(MouseControl)} 
     */
    public Viewer()
    {
        this.painters = new TreeMap<Integer, Set<Painter>>();
        this.transform = new AffineTransform();
        this.inverseTransform = new AffineTransform();
        this.worldArea = new Rectangle2D.Double(0,0,1,1);
        setMouseControl(
            MouseControls.createDefault(this, true, true));
        setBackground(Color.WHITE);
        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                handleResize();
                repaint();
            }
        });
    }
    
    /**
     * Set the given {@link MouseControl} as a <code>MouseListener</code>, 
     * <code>MouseMotionListener</code> and <code>MouseWheelListener</code>
     * for this viewer. Any previously registered {@link MouseControl} will
     * be removed.
     * 
     * @param newMouseControl The {@link MouseControl} to set. This may be
     * <code>null</code> to remove any {@link MouseControl}
     */
    public final void setMouseControl(MouseControl newMouseControl)
    {
        if (mouseControl != null)
        {
            removeMouseListener(mouseControl);
            removeMouseMotionListener(mouseControl);
            removeMouseWheelListener(mouseControl);
        }
        this.mouseControl = newMouseControl;
        if (mouseControl != null)
        {
            addMouseListener(mouseControl);
            addMouseMotionListener(mouseControl);
            addMouseWheelListener(mouseControl);
        }
    }
    
    
    /**
     * Set whether the y-axis should be flipped to point from the
     * bottom of the screen to the top
     * 
     * @param flippedVertically Whether the y-axis should be flipped
     */
    public final void setFlippedVertically(boolean flippedVertically)
    {
        if (flippedVertically)
        {
            basicWorldToScreenTransform = getFlipVerticallyTransform();
        }
        else
        {
            basicWorldToScreenTransform = null;
        }
        this.flippedVertically = flippedVertically;
        repaint();
    }
    
    /**
     * Set whether the y-axis is flipped to point from the bottom of the 
     * screen to the top
     * 
     * @return Whether the y-axis is be flipped
     */
    public final boolean isFlippedVertically()
    {
        return flippedVertically;
    }
    
    /**
     * Set whether the contents should be resized when the screen
     * is resized. When this is set to <code>true</code>, then resizing
     * this panel will adjust the scale factors accordingly, so that
     * the world area that is displayed remains the same while resizing.<br>
     * <br>
     * Note that this usually only makes sense when the aspect ratio is not
     * maintained - that is, when {@link #setMaintainAspectRatio(boolean)}
     * was set to <code>false</code>.
     * 
     * @param resizingContents The resizing behavior
     */
    public final void setResizingContents(boolean resizingContents)
    {
        this.resizingContents = resizingContents;
    }
    
    /**
     * Returns whether this viewer is configured so that the contents
     * should be resized when the screen is resized. See 
     * {@link #setResizingContents(boolean)}.
     * 
     * @return The resizing behavior
     */
    public final boolean isResizingContents()
    {
        return resizingContents;
    }
    
    /**
     * Set whether the aspect ratio of the displayed world area is 
     * maintained during resize operations of this panel, or when 
     * {@link #setDisplayedWorldArea(Rectangle2D)} is called. <br>
     * <br>
     * Note that this should usually be set to <code>false</code> when
     * {@link #setResizingContents(boolean)} is set to <code>true</code>.
     * 
     * @param maintainAspectRatio Whether the aspect ratio is maintained
     */
    public final void setMaintainAspectRatio(boolean maintainAspectRatio)
    {
        this.maintainAspectRatio = maintainAspectRatio;
    }
    
    /**
     * Returns whether the aspect ratio is maintained during resize operations
     * or when {@link #setDisplayedWorldArea(Rectangle2D)} is called
     * 
     * @return Whether the aspect ratio is maintained during resize operations
     */
    public final boolean isMaintainAspectRatio()
    {
        return maintainAspectRatio;
    }
    
    /**
     * Set whether the antialiasing rendering hint should be enabled
     * by default
     * 
     * @param antialiasing Whether antialiasing should be enabled
     */
    public final void setAntialiasing(boolean antialiasing)
    {
        this.antialiasing = antialiasing;
    }
    
    /**
     * Return whether the antialiasing rendering hint is enabled
     * by default
     * 
     * @return Whether antialiasing is enabled
     */
    public final boolean isAntialiasing()
    {
        return antialiasing;
    }
    
    
    
    /**
     * Add the given {@link Painter}, which will perform painting
     * operations in the {@link #paintComponent(Graphics)} method.
     * The painter will be added at the default layer (0). If the
     * given painter is <code>null</code>, then this call will have
     * no effect and <code>false</code> will be returned.
     * 
     * @param painter The {@link Painter} to add
     * @return Whether the painter was not yet contained in this viewer
     */
    public final boolean addPainter(Painter painter)
    {
        return addPainter(painter, 0);
    }
    
    /**
     * Add the given {@link Painter}, which will perform painting
     * operations in the {@link #paintComponent(Graphics)} method,
     * on the specified layer. If the given painter is <code>null</code>, 
     * then this call will have no effect and <code>false</code> will be 
     * returned.
     * 
     * @param painter The {@link Painter} to add
     * @param layer The layer for the {@link Painter}
     * @return Whether the painter was not yet contained in this viewer
     */
    public final boolean addPainter(Painter painter, int layer)
    {
        if (painter == null)
        {
            return false;
        }
        Set<Painter> set = painters.get(layer);
        if (set == null)
        {
            set = new LinkedHashSet<Painter>();
            painters.put(layer, set);
        }
        boolean changed = set.add(painter);
        if (changed)
        {
            repaint();
        }
        return changed;
    }
    
    
    /**
     * Remove the given {@link Painter} from all layers that it is contained
     * in
     * 
     * @param painter The {@link Painter} to remove
     * @return Whether the painter was contained in this viewer
     */
    public final boolean removePainter(Painter painter)
    {
        boolean changed = false;
        Set<Integer> toRemove = new LinkedHashSet<Integer>();
        for (Entry<Integer, Set<Painter>> entry : painters.entrySet())
        {
            Set<Painter> set = entry.getValue();
            boolean wasContained = set.remove(painter);
            if (wasContained)
            {
                changed = true;
                if (set.isEmpty())
                {
                    toRemove.add(entry.getKey());
                }
            }
        }
        if (!toRemove.isEmpty())
        {
            painters.keySet().removeAll(toRemove);
        }
        if (changed)
        {
            repaint();
        }
        return changed;
    }
    
    /**
     * Remove the given {@link Painter} from the specified layer
     * 
     * @param painter The {@link Painter} to remove
     * @param layer The layer from which the painter should be removed
     * @return Whether the painter was contained in this layer
     */
    public final boolean removePainter(Painter painter, int layer)
    {
        Set<Painter> set = painters.get(layer);
        boolean wasContained = set.remove(painter);
        if (wasContained)
        {
            if (set.isEmpty())
            {
                painters.remove(layer);
            }
            repaint();
            return true;
        }
        return false;
    }
    
    
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        
        if (pendingWorldArea != null)
        {
            boolean b = isMaintainAspectRatio();
            setMaintainAspectRatio(
                pendingWorldAreaMaintainAspectRatioState);
            setDisplayedWorldArea(pendingWorldArea);
            pendingWorldArea = null;
            setMaintainAspectRatio(b);
        }
        
        if (antialiasing)
        {
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else
        {
            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        for (Entry<Integer, Set<Painter>> entry : painters.entrySet())
        {
            Set<Painter> set = entry.getValue();
            for (Painter painter : set)
            {
                AffineTransform w = getWorldToScreen();
                painter.paint(g, w, getWidth(), getHeight());
            }
        }
    }
    

    /**
     * Returns a copy of the current world-to-screen transform
     * 
     * @return The world-to-screen transform
     */
    public final AffineTransform getWorldToScreen()
    {
        AffineTransform at = new AffineTransform(transform);
        if (basicWorldToScreenTransform != null)
        {
            at.concatenate(basicWorldToScreenTransform);
        }
        return at;
    }
    
    /**
     * Returns an affine transform that flips the contents vertically
     * 
     * @return The affine transform
     */
    private static AffineTransform getFlipVerticallyTransform()
    {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getTranslateInstance(0, 1));
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        return at;
    }
    
    
    /**
     * Returns a copy of the screen-to-world transform
     * 
     * @return The screen-to-world transform
     */
    public final AffineTransform getScreenToWorld()
    {
        return new AffineTransform(getInverseTransform());
    }
    
    /**
     * Returns the inverse transform, creating it if necessary
     * 
     * @return The inverse transform
     */
    private final AffineTransform getInverseTransform()
    {
        if (inverseTransform == null)
        {
            try
            {
                inverseTransform = getWorldToScreen().createInverse();
            }
            catch (NoninvertibleTransformException e)
            {
                throw new IllegalArgumentException(
                    "Non-invertible transform", e);
            }
        }
        return inverseTransform;
    }
    

    /**
     * Transform the contents of this viewer with the given transform
     * 
     * @param t The transform
     * @throws NullPointerException if the given transform is <code>null</code>
     * @throws IllegalArgumentException if the determinant of the given 
     * transform is 0.0, or NaN, or infinite
     */
    public final void transform(AffineTransform t)
    {
        GeomUtils.validate(t);
        transform.concatenate(t);
        inverseTransform = null;
        repaint();
    }
    
    /**
     * Reset this viewer to the identity transform
     */
    public final void resetTransform()
    {
        transform.setToIdentity();
        inverseTransform = null;
        repaint();
    }
    
    /**
     * Set the world-to-screen transform of this viewer to the given transform.
     * 
     * @param t The transform to set
     * @throws NullPointerException if the given transform is <code>null</code>
     * @throws IllegalArgumentException if the determinant of the given 
     * transform is 0.0, or NaN, or infinite
     */
    public final void setTransform(AffineTransform t)
    {
        GeomUtils.validate(t);
        transform.setTransform(t);
        inverseTransform = null;
        repaint();
    }
    
    
    /**
     * Zoom about the specified point (in screen coordinates) by the given 
     * factor.  
     * 
     * @param screenCenterX The x-coordinate of the zooming center, 
     * in screen coordinates
     * @param screenCenterY The y-coordinate of the zooming center, 
     * in screen coordinates
     * @param factorX The zooming factor for the x-axis
     * @param factorY The zooming factor for the y-axis
     */
    public final void zoom(double screenCenterX, double screenCenterY, 
        double factorX, double factorY)
    {
        Point2D worldCenter = GeomUtils.inverseTransform(
            transform, new Point2D.Double(screenCenterX, screenCenterY), null);
        AffineTransform t = new AffineTransform();
        t.translate(worldCenter.getX(), worldCenter.getY());
        t.scale(factorX, factorY);
        t.translate(-worldCenter.getX(), -worldCenter.getY());
        transform(t);
        repaint();
    }
    
    /**
     * Translate this viewer by the given delta, in screen
     * coordinates
     * 
     * @param screenDx The movement delta in x-direction, in screen coordinates
     * @param screenDy The movement delta in y-direction, in screen coordinates
     */
    public final void translate(double screenDx, double screenDy)
    {
        Point2D worldOld = GeomUtils.inverseTransform(
            transform, new Point2D.Double(0, 0), null);
        Point2D worldNew = GeomUtils.inverseTransform(
            transform, new Point2D.Double(screenDx, screenDy), null);
        double tdx = worldNew.getX() - worldOld.getX();
        double tdy = worldNew.getY() - worldOld.getY();
        AffineTransform t = new AffineTransform();
        t.translate(tdx, tdy);
        transform(t);
        repaint();
    }
    
    /**
     * Rotate about the specified point (in screen coordinates)
     * by the given angle (in radians)
     * 
     * @param screenCenterX The x-coordinate of the zooming center, 
     * in screen coordinates
     * @param screenCenterY The y-coordinate of the zooming center, 
     * in screen coordinates
     * @param angleRad The angle, in radians
     */
    public final void rotate(
        double screenCenterX, double screenCenterY, double angleRad)
    {
        transform.preConcatenate(
            AffineTransform.getRotateInstance(
                angleRad, screenCenterX, screenCenterY));
        inverseTransform = null;
        repaint();
    }
    
    
    
    /**
     * Set the area (in world coordinates) that should be shown.<br> 
     * <br>
     * This will adjust the scaling and translation so that at least the
     * specified rectangle is visible (even when the view is rotated).<br>
     * <br>
     * If this viewer is {@link #isMaintainAspectRatio() maintaining
     * the aspect ratio}, then the smallest area with the current 
     * aspect ratio will be visible that entirely contains the specified
     * rectangle.  
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate  
     * @param w The width 
     * @param h The height
     */
    public final void setDisplayedWorldArea(
        double x, double y, double w, double h)
    {
        setDisplayedWorldArea(new Rectangle2D.Double(x, y, w, h));
    }
    
    /**
     * Set the area (in world coordinates) that should be shown. <br> 
     * <br>
     * This will adjust the scaling and translation so that at least the
     * given rectangle is visible (even when the view is rotated).<br>
     * <br>
     * If this viewer is {@link #isMaintainAspectRatio() maintaining
     * the aspect ratio}, then the smallest area with the current 
     * aspect ratio will be visible that entirely contains the given
     * rectangle.
     * 
     * @param newWorldArea The world area
     */
    public final void setDisplayedWorldArea(Rectangle2D newWorldArea)
    {
        if (getWidth() <= 0 || getHeight() <= 0)
        {
            pendingWorldArea = new Rectangle2D.Double();
            pendingWorldArea.setRect(newWorldArea);
            pendingWorldAreaMaintainAspectRatioState = 
                maintainAspectRatio;
            return;
        }
        pendingWorldArea = null;
        worldArea.setRect(newWorldArea);
        
        Rectangle2D worldAreaInScreen = GeomUtils.computeBounds(
            getWorldToScreen(), worldArea, null);
        double scaleX = getWidth() / worldAreaInScreen.getWidth();
        double scaleY = getHeight() / worldAreaInScreen.getHeight();
        double dx = -worldAreaInScreen.getX();
        double dy = -worldAreaInScreen.getY();

        if (maintainAspectRatio)
        {
            scaleX = Math.min(scaleX, scaleY);
            scaleY = scaleX;
        }
        transform.preConcatenate(
            AffineTransform.getTranslateInstance(dx, dy));
        transform.preConcatenate(
            AffineTransform.getScaleInstance(scaleX, scaleY));
        transform.preConcatenate(
            AffineTransform.getTranslateInstance(-dx * scaleX, -dy * scaleY));
        inverseTransform = null;
        
        Rectangle2D newWorldAreaInScreen = GeomUtils.computeBounds(
            getWorldToScreen(), worldArea, null);
        double newDx = -newWorldAreaInScreen.getX();
        double newDy = -newWorldAreaInScreen.getY();
        translate(newDx, newDy);
        
        repaint();
     }
    
    
    /**
     * Update the scaling factor when this component was resized
     */
    private void handleResize()
    {
        if (getWidth() <= 0 || getHeight() <= 0)
        {
            return;
        }
        if (pendingWorldArea != null)
        {
            boolean b = isMaintainAspectRatio();
            setMaintainAspectRatio(
                pendingWorldAreaMaintainAspectRatioState);
            setDisplayedWorldArea(pendingWorldArea);
            pendingWorldArea = null;
            setMaintainAspectRatio(b);
            pendingWorldArea = null;
        }
        if (resizingContents)
        {
            if (previousSize == null)
            {
                previousSize = getSize();
            }
            double scaleX = (double)getWidth() / previousSize.width;
            double scaleY = (double)getHeight() / previousSize.height;
            if (maintainAspectRatio)
            {
                scaleX = Math.min(scaleX, scaleY);
                scaleY = scaleX;
            }
            transform.preConcatenate(
                AffineTransform.getScaleInstance(scaleX, scaleY));
            inverseTransform = null;
            
        }
        previousSize = getSize();
    }

}
