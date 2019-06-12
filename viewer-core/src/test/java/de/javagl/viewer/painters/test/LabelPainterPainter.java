package de.javagl.viewer.painters.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.javagl.viewer.Painter;
import de.javagl.viewer.SimpleObjectPainter;
import de.javagl.viewer.painters.LabelPainter;
import de.javagl.viewer.painters.StringBoundsUtils;

/**
 * A helper class for the {@link LabelPainterTest}
 */
@SuppressWarnings("javadoc")
class LabelPainterPainter implements Painter
{
    private static final Font DEFAULT_FONT = 
        new Font("Sans Serif", Font.PLAIN, 9);
    
    private final LabelPainter labelPainter;
    private final SimpleObjectPainter<String> labelObjectPainter;
    
    LabelPainterPainter(
        LabelPainter labelPainter,
        SimpleObjectPainter<String> labelObjectPainter)
    {
        this.labelPainter = labelPainter;
        this.labelObjectPainter = labelObjectPainter;
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        // Draw the unit square and the location marker
        g.setColor(Color.BLUE);
        g.draw(worldToScreen.createTransformedShape(
            new Rectangle2D.Double(0,0,1,1)));
        Point2D labelLocation = labelPainter.getLabelLocation();
        Point2D screenLabelLocation =
            worldToScreen.transform(labelLocation, null);
        drawMarker(g, screenLabelLocation, 3, "Location");
        
        Font font = labelPainter.getFont();
        
        if (labelPainter.isTransformingLabels())
        {
            // Compute the bounds of the label with the font of the
            // LabelPainter and the world to screen transform
            g.setFont(font);
            Rectangle2D labelBounds = 
                StringBoundsUtils.computeStringBounds(
                    labelObjectPainter.getObject(), font);

            // Compute the absolute location of the anchor in the screen
            Point2D labelAnchor = labelPainter.getLabelAnchor();
            double absoluteLabelAnchorX = 
                computeAbsoluteX(labelBounds, labelAnchor);
            double absoluteLabelAnchorY = 
                computeAbsoluteY(labelBounds, labelAnchor);
            Point2D absoluteLabelAnchor = 
                new Point2D.Double(absoluteLabelAnchorX, absoluteLabelAnchorY);
            Point2D absoluteScreenLabelAnchor = 
                worldToScreen.transform(absoluteLabelAnchor, null);
            
            // Draw the label, but only with the current worldToScreen
            // transform, and without any transforms from the LabelPainter. 
            // This will show the label with the right size, but without
            // any rotation or translation that are set in the LabelPainter
            // via its anchor or location 
            AffineTransform oldAt = g.getTransform();
            g.transform(worldToScreen);
            g.setColor(new Color(255,0,0,65));
            g.drawString(labelObjectPainter.getObject(), 0, 0);
            g.setTransform(oldAt);
            
            // Paint the bounds of the (untransformed) label and
            // the and anchor location
            g.draw(worldToScreen.createTransformedShape(labelBounds));
            drawMarker(g, absoluteScreenLabelAnchor, 4, "Anchor");
            
            // Draw the bounds of the label when it is transformed
            // with the rotation and anchor- and location translations 
            // of the LabelPainter
            g.setColor(Color.BLACK);
            AffineTransform at = new AffineTransform(worldToScreen);
            at.translate(labelLocation.getX(), labelLocation.getY());
            at.rotate(labelPainter.getAngle());
            at.translate(-absoluteLabelAnchorX, -absoluteLabelAnchorY);
            g.draw(at.createTransformedShape(labelBounds));
        }
        else
        {
            // Compute the bounds of the label with the font of the
            // LabelPainter 
            g.setFont(font);
            Rectangle2D labelBounds = 
                StringBoundsUtils.computeStringBounds(
                    labelObjectPainter.getObject(), font);

            // Compute the absolute location of the anchor in the screen
            Point2D labelAnchor = labelPainter.getLabelAnchor();
            double absoluteLabelAnchorX = 
                computeAbsoluteX(labelBounds, labelAnchor);
            double absoluteLabelAnchorY = 
                computeAbsoluteY(labelBounds, labelAnchor);
            Point2D absoluteLabelAnchor = 
                new Point2D.Double(absoluteLabelAnchorX, absoluteLabelAnchorY);
            
            // Draw the label, without any transforms from the LabelPainter. 
            // This will show the label with the right size, but without
            // any rotation or translation that are set in the LabelPainter
            // via its anchor or location
            AffineTransform oldAt = g.getTransform();
            Point2D originScreen =
                worldToScreen.transform(new Point2D.Double(),  null);
            g.translate(originScreen.getX(), originScreen.getY());
            g.setColor(new Color(255,0,0,165));
            g.drawString(labelObjectPainter.getObject(), 0, 0);
            
            // Paint the bounds of the (untransformed) label and
            // the and anchor location
            g.draw(labelBounds);
            drawMarker(g, absoluteLabelAnchor, 4, "Anchor");
            g.setTransform(oldAt);
            
            // Draw the bounds of the label when it is transformed
            // with the rotation and anchor- and location translations 
            // of the LabelPainter
            g.setColor(Color.BLACK);
            AffineTransform at = new AffineTransform();
            at.translate(screenLabelLocation.getX(), screenLabelLocation.getY());
            at.translate(labelLocation.getX(), labelLocation.getY());
            at.rotate(labelPainter.getAngle());
            at.translate(-absoluteLabelAnchorX, -absoluteLabelAnchorY);
            g.draw(at.createTransformedShape(labelBounds));
        }
        
        // For comparison, draw the bounds of the label that are computed
        // by the painter
        Shape computedBounds = labelPainter.computeLabelBounds(
            worldToScreen,  labelObjectPainter.getObject());
        g.setStroke(new BasicStroke(3.0f));
        g.setPaint(new Color(0,255,0,64));
        g.draw(computedBounds);
    }
    
    private static void drawMarker(
        Graphics2D g, Point2D p, double r, String text)
    {
        Font oldFont = g.getFont();
        g.setFont(DEFAULT_FONT);
        g.draw(dot(p, r));
        g.drawString(text, (int)p.getX(), (int)p.getY());
        g.setFont(oldFont);
    }
    
    private static Shape dot(Point2D p, double r)
    {
        return new Ellipse2D.Double(p.getX()-r, p.getY()-r, r+r, r+r);
    }
        
    private static double computeAbsoluteX(Rectangle2D bounds, Point2D anchor)
    {
        return bounds.getX() + bounds.getWidth() * anchor.getX();
    }
    private static double computeAbsoluteY(Rectangle2D bounds, Point2D anchor)
    {
        return bounds.getY() + bounds.getHeight() * anchor.getY();
    }
    
}