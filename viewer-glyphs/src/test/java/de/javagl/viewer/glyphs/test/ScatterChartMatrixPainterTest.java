/*
 * www.javagl.de - Viewer - Glyphs
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.glyphs.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Painter;
import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.ScatterChart;
import de.javagl.viewer.glyphs.ScatterChartMatrix;
import de.javagl.viewer.glyphs.ScatterChartMatrixPainter;
import de.javagl.viewer.glyphs.TickShapes;

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

        Object data[][] = createData();
        Object headers[] = createHeaders();
        ScatterChartMatrix scatterChartMatrix = new ScatterChartMatrix()
        {
            @Override
            public int getNumCharts()
            {
                return 4;
            }
            
            @Override
            public String getLabel(int row, int col)
            {
                return String.valueOf(headers[row]) + "/"
                    + String.valueOf(headers[col]);
            }
            
            @Override
            public ScatterChart getChart(int row, int col)
            {
                if (row <= col) return null;
                
                Shape shape = TickShapes.square(2);
                return new ScatterChart()
                {
                    @Override
                    public Shape getShape(int index)
                    {
                        return shape;
                    }
                    
                    @Override
                    public double getPointX(int index)
                    {
                        return (Double)data[index][col];
                    }
                    
                    @Override
                    public double getPointY(int index)
                    {
                        return (Double)data[index][row];
                    }
                    
                    @Override
                    public Paint getFillPaint(int index)
                    {
                        String species = String.valueOf(data[index][4]);
                        if (species.equals("Iris setosa"))
                        {
                            return Color.RED;
                        }
                        if (species.equals("Iris versicolor"))
                        {
                            return Color.GREEN;
                        }
                        if (species.equals("Iris virginica"))
                        {
                            return Color.BLUE;
                        }
                        return Color.MAGENTA;
                    }
                    
                    @Override
                    public Paint getDrawPaint(int index)
                    {
                        return null;
                    }
                    
                    @Override
                    public Stroke getDrawStroke(int index)
                    {
                        return null;
                    }
                    
                    @Override
                    public int getNumPoints()
                    {
                        return data.length;
                    }
                };
            }
        };

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
    
    /**
     * Create unspecified data for this test
     * 
     * @return The data
     */
    private static Object[][] createData()
    {
        Object data[][] = 
        {
            { 5.1, 3.5, 1.4, 0.2, "Iris setosa" },
            { 4.9, 3.0, 1.4, 0.2, "Iris setosa" },
            { 4.7, 3.2, 1.3, 0.2, "Iris setosa" },
            { 4.6, 3.1, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.6, 1.4, 0.2, "Iris setosa" },
            { 5.4, 3.9, 1.7, 0.4, "Iris setosa" },
            { 4.6, 3.4, 1.4, 0.3, "Iris setosa" },
            { 5.0, 3.4, 1.5, 0.2, "Iris setosa" },
            { 4.4, 2.9, 1.4, 0.2, "Iris setosa" },
            { 4.9, 3.1, 1.5, 0.1, "Iris setosa" },
            { 5.4, 3.7, 1.5, 0.2, "Iris setosa" },
            { 4.8, 3.4, 1.6, 0.2, "Iris setosa" },
            { 4.8, 3.0, 1.4, 0.1, "Iris setosa" },
            { 4.3, 3.0, 1.1, 0.1, "Iris setosa" },
            { 5.8, 4.0, 1.2, 0.2, "Iris setosa" },
            { 5.7, 4.4, 1.5, 0.4, "Iris setosa" },
            { 5.4, 3.9, 1.3, 0.4, "Iris setosa" },
            { 5.1, 3.5, 1.4, 0.3, "Iris setosa" },
            { 5.7, 3.8, 1.7, 0.3, "Iris setosa" },
            { 5.1, 3.8, 1.5, 0.3, "Iris setosa" },
            { 5.4, 3.4, 1.7, 0.2, "Iris setosa" },
            { 5.1, 3.7, 1.5, 0.4, "Iris setosa" },
            { 4.6, 3.6, 1.0, 0.2, "Iris setosa" },
            { 5.1, 3.3, 1.7, 0.5, "Iris setosa" },
            { 4.8, 3.4, 1.9, 0.2, "Iris setosa" },
            { 5.0, 3.0, 1.6, 0.2, "Iris setosa" },
            { 5.0, 3.4, 1.6, 0.4, "Iris setosa" },
            { 5.2, 3.5, 1.5, 0.2, "Iris setosa" },
            { 5.2, 3.4, 1.4, 0.2, "Iris setosa" },
            { 4.7, 3.2, 1.6, 0.2, "Iris setosa" },
            { 4.8, 3.1, 1.6, 0.2, "Iris setosa" },
            { 5.4, 3.4, 1.5, 0.4, "Iris setosa" },
            { 5.2, 4.1, 1.5, 0.1, "Iris setosa" },
            { 5.5, 4.2, 1.4, 0.2, "Iris setosa" },
            { 4.9, 3.1, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.2, 1.2, 0.2, "Iris setosa" },
            { 5.5, 3.5, 1.3, 0.2, "Iris setosa" },
            { 4.9, 3.6, 1.4, 0.1, "Iris setosa" },
            { 4.4, 3.0, 1.3, 0.2, "Iris setosa" },
            { 5.1, 3.4, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.5, 1.3, 0.3, "Iris setosa" },
            { 4.5, 2.3, 1.3, 0.3, "Iris setosa" },
            { 4.4, 3.2, 1.3, 0.2, "Iris setosa" },
            { 5.0, 3.5, 1.6, 0.6, "Iris setosa" },
            { 5.1, 3.8, 1.9, 0.4, "Iris setosa" },
            { 4.8, 3.0, 1.4, 0.3, "Iris setosa" },
            { 5.1, 3.8, 1.6, 0.2, "Iris setosa" },
            { 4.6, 3.2, 1.4, 0.2, "Iris setosa" },
            { 5.3, 3.7, 1.5, 0.2, "Iris setosa" },
            { 5.0, 3.3, 1.4, 0.2, "Iris setosa" },
            { 7.0, 3.2, 4.7, 1.4, "Iris versicolor" },
            { 6.4, 3.2, 4.5, 1.5, "Iris versicolor" },
            { 6.9, 3.1, 4.9, 1.5, "Iris versicolor" },
            { 5.5, 2.3, 4.0, 1.3, "Iris versicolor" },
            { 6.5, 2.8, 4.6, 1.5, "Iris versicolor" },
            { 5.7, 2.8, 4.5, 1.3, "Iris versicolor" },
            { 6.3, 3.3, 4.7, 1.6, "Iris versicolor" },
            { 4.9, 2.4, 3.3, 1.0, "Iris versicolor" },
            { 6.6, 2.9, 4.6, 1.3, "Iris versicolor" },
            { 5.2, 2.7, 3.9, 1.4, "Iris versicolor" },
            { 5.0, 2.0, 3.5, 1.0, "Iris versicolor" },
            { 5.9, 3.0, 4.2, 1.5, "Iris versicolor" },
            { 6.0, 2.2, 4.0, 1.0, "Iris versicolor" },
            { 6.1, 2.9, 4.7, 1.4, "Iris versicolor" },
            { 5.6, 2.9, 3.6, 1.3, "Iris versicolor" },
            { 6.7, 3.1, 4.4, 1.4, "Iris versicolor" },
            { 5.6, 3.0, 4.5, 1.5, "Iris versicolor" },
            { 5.8, 2.7, 4.1, 1.0, "Iris versicolor" },
            { 6.2, 2.2, 4.5, 1.5, "Iris versicolor" },
            { 5.6, 2.5, 3.9, 1.1, "Iris versicolor" },
            { 5.9, 3.2, 4.8, 1.8, "Iris versicolor" },
            { 6.1, 2.8, 4.0, 1.3, "Iris versicolor" },
            { 6.3, 2.5, 4.9, 1.5, "Iris versicolor" },
            { 6.1, 2.8, 4.7, 1.2, "Iris versicolor" },
            { 6.4, 2.9, 4.3, 1.3, "Iris versicolor" },
            { 6.6, 3.0, 4.4, 1.4, "Iris versicolor" },
            { 6.8, 2.8, 4.8, 1.4, "Iris versicolor" },
            { 6.7, 3.0, 5.0, 1.7, "Iris versicolor" },
            { 6.0, 2.9, 4.5, 1.5, "Iris versicolor" },
            { 5.7, 2.6, 3.5, 1.0, "Iris versicolor" },
            { 5.5, 2.4, 3.8, 1.1, "Iris versicolor" },
            { 5.5, 2.4, 3.7, 1.0, "Iris versicolor" },
            { 5.8, 2.7, 3.9, 1.2, "Iris versicolor" },
            { 6.0, 2.7, 5.1, 1.6, "Iris versicolor" },
            { 5.4, 3.0, 4.5, 1.5, "Iris versicolor" },
            { 6.0, 3.4, 4.5, 1.6, "Iris versicolor" },
            { 6.7, 3.1, 4.7, 1.5, "Iris versicolor" },
            { 6.3, 2.3, 4.4, 1.3, "Iris versicolor" },
            { 5.6, 3.0, 4.1, 1.3, "Iris versicolor" },
            { 5.5, 2.5, 4.0, 1.3, "Iris versicolor" },
            { 5.5, 2.6, 4.4, 1.2, "Iris versicolor" },
            { 6.1, 3.0, 4.6, 1.4, "Iris versicolor" },
            { 5.8, 2.6, 4.0, 1.2, "Iris versicolor" },
            { 5.0, 2.3, 3.3, 1.0, "Iris versicolor" },
            { 5.6, 2.7, 4.2, 1.3, "Iris versicolor" },
            { 5.7, 3.0, 4.2, 1.2, "Iris versicolor" },
            { 5.7, 2.9, 4.2, 1.3, "Iris versicolor" },
            { 6.2, 2.9, 4.3, 1.3, "Iris versicolor" },
            { 5.1, 2.5, 3.0, 1.1, "Iris versicolor" },
            { 5.7, 2.8, 4.1, 1.3, "Iris versicolor" },
            { 6.3, 3.3, 6.0, 2.5, "Iris virginica" },
            { 5.8, 2.7, 5.1, 1.9, "Iris virginica" },
            { 7.1, 3.0, 5.9, 2.1, "Iris virginica" },
            { 6.3, 2.9, 5.6, 1.8, "Iris virginica" },
            { 6.5, 3.0, 5.8, 2.2, "Iris virginica" },
            { 7.6, 3.0, 6.6, 2.1, "Iris virginica" },
            { 4.9, 2.5, 4.5, 1.7, "Iris virginica" },
            { 7.3, 2.9, 6.3, 1.8, "Iris virginica" },
            { 6.7, 2.5, 5.8, 1.8, "Iris virginica" },
            { 7.2, 3.6, 6.1, 2.5, "Iris virginica" },
            { 6.5, 3.2, 5.1, 2.0, "Iris virginica" },
            { 6.4, 2.7, 5.3, 1.9, "Iris virginica" },
            { 6.8, 3.0, 5.5, 2.1, "Iris virginica" },
            { 5.7, 2.5, 5.0, 2.0, "Iris virginica" },
            { 5.8, 2.8, 5.1, 2.4, "Iris virginica" },
            { 6.4, 3.2, 5.3, 2.3, "Iris virginica" },
            { 6.5, 3.0, 5.5, 1.8, "Iris virginica" },
            { 7.7, 3.8, 6.7, 2.2, "Iris virginica" },
            { 7.7, 2.6, 6.9, 2.3, "Iris virginica" },
            { 6.0, 2.2, 5.0, 1.5, "Iris virginica" },
            { 6.9, 3.2, 5.7, 2.3, "Iris virginica" },
            { 5.6, 2.8, 4.9, 2.0, "Iris virginica" },
            { 7.7, 2.8, 6.7, 2.0, "Iris virginica" },
            { 6.3, 2.7, 4.9, 1.8, "Iris virginica" },
            { 6.7, 3.3, 5.7, 2.1, "Iris virginica" },
            { 7.2, 3.2, 6.0, 1.8, "Iris virginica" },
            { 6.2, 2.8, 4.8, 1.8, "Iris virginica" },
            { 6.1, 3.0, 4.9, 1.8, "Iris virginica" },
            { 6.4, 2.8, 5.6, 2.1, "Iris virginica" },
            { 7.2, 3.0, 5.8, 1.6, "Iris virginica" },
            { 7.4, 2.8, 6.1, 1.9, "Iris virginica" },
            { 7.9, 3.8, 6.4, 2.0, "Iris virginica" },
            { 6.4, 2.8, 5.6, 2.2, "Iris virginica" },
            { 6.3, 2.8, 5.1, 1.5, "Iris virginica" },
            { 6.1, 2.6, 5.6, 1.4, "Iris virginica" },
            { 7.7, 3.0, 6.1, 2.3, "Iris virginica" },
            { 6.3, 3.4, 5.6, 2.4, "Iris virginica" },
            { 6.4, 3.1, 5.5, 1.8, "Iris virginica" },
            { 6.0, 3.0, 4.8, 1.8, "Iris virginica" },
            { 6.9, 3.1, 5.4, 2.1, "Iris virginica" },
            { 6.7, 3.1, 5.6, 2.4, "Iris virginica" },
            { 6.9, 3.1, 5.1, 2.3, "Iris virginica" },
            { 5.8, 2.7, 5.1, 1.9, "Iris virginica" },
            { 6.8, 3.2, 5.9, 2.3, "Iris virginica" },
            { 6.7, 3.3, 5.7, 2.5, "Iris virginica" },
            { 6.7, 3.0, 5.2, 2.3, "Iris virginica" },
            { 6.3, 2.5, 5.0, 1.9, "Iris virginica" },
            { 6.5, 3.0, 5.2, 2.0, "Iris virginica" },
            { 6.2, 3.4, 5.4, 2.3, "Iris virginica" },
            { 5.9, 3.0, 5.1, 1.8, "Iris virginica" },
        };
        return data;
    }
    
    /**
     * Create unspecified headers for this test
     * 
     * @return The headers
     */
    private static Object[] createHeaders()
    {
        Object headers[] = 
        {
            "Sepal length",
            "Sepal width",
            "Petal length",
            "Petal width",
            "Species"        
        };
        return headers;
    }
    
}
