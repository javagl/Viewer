/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2020 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.selection.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import de.javagl.viewer.Painter;
import de.javagl.viewer.selection.PointBasedSelector;
import de.javagl.viewer.selection.Selectors;
import de.javagl.viewer.selection.ShapeBasedSelector;

/**
 * Implementation of the {@link Painter} interface for the selection tests
 */
@SuppressWarnings("javadoc")
class ViewerSelectionTestPainter implements Painter, 
    ShapeBasedSelector<Point2D>, PointBasedSelector<Point2D>
{
    private final List<Point2D> points;
    private final double pointRadius = 5.0;
    private final Predicate<Point2D> selectionPredicate;
    
    public ViewerSelectionTestPainter(Predicate<Point2D> selectionPredicate)
    {
        this.selectionPredicate = selectionPredicate;
        this.points = new ArrayList<Point2D>();
        
        Random random = new Random(0);
        int n = 50;
        for (int i=0; i<n; i++)
        {
            double x = random.nextDouble();
            double y = random.nextDouble();
            Point2D point = new Point2D.Double(x, y);
            points.add(point);
        }
    }
    
    @Override
    public void paint(Graphics2D g, AffineTransform worldToScreen, 
        double w, double h)
    {
        drawGrid(g, worldToScreen);
        drawPoints(g, worldToScreen);
    }

    private void drawGrid(Graphics2D g, AffineTransform worldToScreen)
    {
        g.setStroke(new BasicStroke(1.0f));
        Path2D grid = new Path2D.Double();
        double delta = 0.1;
        for (int i = 0; i < 10; i++)
        {
            grid.moveTo(0.0, i * delta);
            grid.lineTo(1.0, i * delta);
            grid.moveTo(i * delta, 0.0);
            grid.lineTo(i * delta, 1.0);
        }
        Rectangle2D border = new Rectangle2D.Double(0, 0, 1, 1);
       
        g.setColor(Color.LIGHT_GRAY);
        g.draw(worldToScreen.createTransformedShape(grid));
        g.setColor(Color.BLACK);
        g.draw(worldToScreen.createTransformedShape(border));
    }
    
    private void drawPoints(Graphics2D g, AffineTransform worldToScreen)
    {
        g.setColor(Color.RED);
        Shape pointShape = new Ellipse2D.Double(
            -pointRadius, -pointRadius, 
            pointRadius + pointRadius,
            pointRadius + pointRadius);
        
        Stroke defaultStroke = new BasicStroke(1.0f);
        Stroke selectedStroke = new BasicStroke(3.0f);
        
        Point2D p = new Point2D.Double();
        for (int i = 0; i < points.size(); i++)
        {
            AffineTransform oldAt = g.getTransform();
            Point2D point = points.get(i);
            worldToScreen.transform(point, p);
            double x = p.getX();
            double y = p.getY();
            g.translate(x, y);
            if (selectionPredicate.test(point))
            {
                g.setStroke(selectedStroke);
            }
            else
            {
                g.setStroke(defaultStroke);
            }
            g.draw(pointShape);
            g.setTransform(oldAt);
        }
    }

    @Override
    public Collection<Point2D> computeElementsForPoint(
        Point2D point, AffineTransform worldToScreen)
    {
        return Selectors.computePointsForPoint(
            point, worldToScreen, points, pointRadius);
    }

    @Override
    public Collection<Point2D> computeElementsForShape(
        Shape shape, AffineTransform worldToScreen)
    {
        return Selectors.computePointsForShape(shape, worldToScreen, points);
    }

}
