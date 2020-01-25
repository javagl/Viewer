/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.test;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Painter;
import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.ScatterChartMatrices;
import de.javagl.viewer.glyphs.ScatterChartMatrix;
import de.javagl.viewer.glyphs.ScatterChartMatrixPainter;

/**
 * Simple integration test of the {@link ScatterChartMatrixPainter}
 */
public class ScatterChartMatrixPainterTest
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

        ScatterChartMatrix scatterChartMatrix = 
            ScatterChartMatrices.createTest(null);

        ScatterChartMatrixPainter scatterChartMatrixPainter = 
            new ScatterChartMatrixPainter();
        Viewer viewer = new Viewer();
        Painter backgroundPainter = 
            BackgroundPainters.createVerticalGradient(
                new Color(250,250,250),
                new Color(210,210,210));
        viewer.addPainter(backgroundPainter);
        viewer.addPainter(
            Painters.create(scatterChartMatrixPainter, scatterChartMatrix));
        viewer.setDisplayedWorldArea(-1, -1, 3, 3);

        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    
}
