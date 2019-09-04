/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.test;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Painter;
import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.BoxPlot;
import de.javagl.viewer.glyphs.BoxPlotPainter;
import de.javagl.viewer.glyphs.BoxPlots;
import de.javagl.viewer.glyphs.GridLayoutPainter;
 
/**
 * Simple integration test for multiple {@link BoxPlotPainter} instances
 * arranged in a grid layout
 */
public class MultiBoxPlotPainterTest
{
    /**
     * The entry point of this test
     *
     * @param args Not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    
   
    /**
     * Create and show the GUI, to be called on the EDT
     */
    private static void createAndShowGUI()
    {
        JFrame f = new JFrame("Viewer");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(
            new JLabel("<html>"
                + "Right mouse drags: Translate<br> "
                + "Left mouse drags: Rotate<br>"
                + "Mouse wheel: Zoom uniformly<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +shift: zoom along x<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: zoom along y<br>"
                + "</html>"),
            BorderLayout.NORTH);
       
        List<BoxPlot> boxPlots = Arrays.asList(
            BoxPlots.create(0.2, 0.25, 0.4, 0.65, 0.8, 0.5),
            BoxPlots.create(0.2, 0.30, 0.5, 0.65, 0.7, 0.4),
            BoxPlots.create(-0.3, 0.35, 0.4, 0.75, 0.9, 0.6),
            BoxPlots.create(0.1, 0.15, 0.4, 0.75, 0.8, 0.5)
        );
        
        GridLayoutPainter painter = new GridLayoutPainter(boxPlots.size(), 1);
        for (int x=0; x<boxPlots.size(); x++)
        {
            BoxPlot boxPlot = boxPlots.get(x);
            BoxPlotPainter boxPlotPainter = new BoxPlotPainter();
            painter.setDelegate(x, 0, Painters.create(boxPlotPainter, boxPlot));
        }
        double minY = BoxPlots.computeMin(boxPlots);
        double maxY = BoxPlots.computeMax(boxPlots);
        
        Viewer viewer = new Viewer();
        viewer.setFlippedVertically(true);
        
        Painter backgroundPainter = 
            BackgroundPainters.createVerticalGradient(
                new Color(210,210,210),
                new Color(250,250,250));
        viewer.addPainter(backgroundPainter);
        viewer.addPainter(Painters.createNormalized(
            painter, 0.0, minY, 1.0, maxY));
        viewer.setDisplayedWorldArea(-1, -1, 3, 3);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}