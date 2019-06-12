/*
 * www.javagl.de - Viewer 
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.painters.test;
import java.awt.BorderLayout;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import de.javagl.viewer.MouseControls;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.painters.CoordinateSystemPainter;
import de.javagl.viewer.painters.CoordinateSystemPainters;
import de.javagl.viewer.painters.LabelPainter;

/**
 * Simple integration test for the {@link CoordinateSystemPainter}
 */
@SuppressWarnings("javadoc")
public class CoordinateSystemLabelPainterTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    
    private static void createAndShowGUI()
    {
        JFrame f = new JFrame("Viewer");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        f.getContentPane().setLayout(new BorderLayout());
       
        f.getContentPane().add(
            new JLabel("<html>"
                + "Right mouse drags: Translate<br> "
                + "Mouse wheel: Zoom uniformly<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +shift: zoom along x<br>"
                + "&nbsp;&nbsp;&nbsp;&nbsp; +ctrl: zoom along y<br>"
                + "</html>"),
            BorderLayout.NORTH);
       
        
        Viewer viewer = new Viewer();
        viewer.setFlippedVertically(true);
        viewer.setMouseControl(
            MouseControls.createDefault(viewer, false, true));
        
        CoordinateSystemPainter coordinateSystemPainter =
            new CoordinateSystemPainter();
        
        LabelPainter labelPainterX = coordinateSystemPainter.getLabelPainterX();
        labelPainterX.setAngle(Math.toRadians(-45.0));
        labelPainterX.setLabelAnchor(1.0, 0.5);
        
        Duration unit = Duration.ofMinutes(1);
        OffsetDateTime zero = 
            OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        String pattern = "HH:mm:ss";
        coordinateSystemPainter.setLabelFormatterX(
            CoordinateSystemPainters.createDateLabelFormatter(
                zero, unit, pattern));
        
        viewer.addPainter(coordinateSystemPainter);
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        
        JSlider angleSliderX = new JSlider(0,  90);
        angleSliderX.addChangeListener(e -> 
        {
            int angleDeg = angleSliderX.getValue();
            labelPainterX.setAngle(Math.toRadians(-angleDeg));
            viewer.repaint();
        });
        f.getContentPane().add(angleSliderX, BorderLayout.SOUTH);

        LabelPainter labelPainterY = coordinateSystemPainter.getLabelPainterY();
        labelPainterY.setAngle(Math.toRadians(45.0));
        labelPainterY.setLabelAnchor(1.0, 0.0);
        
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
}
