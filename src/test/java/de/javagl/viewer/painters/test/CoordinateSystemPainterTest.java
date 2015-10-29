/*
 * www.javagl.de - Viewer 
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.painters.test;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.MouseControls;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.painters.CoordinateSystemPainter;

/**
 * Simple integration test for the {@link CoordinateSystemPainter}
 */
@SuppressWarnings("javadoc")
public class CoordinateSystemPainterTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
           
            @Override
            public void run()
            {
                createAndShowGUI();
            }
        });
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
        viewer.setMouseControl(
            MouseControls.createDefault(viewer, false, true));
        viewer.setFlippedVertically(true);
        
        CoordinateSystemPainter coordinateSystemPainter =
            new CoordinateSystemPainter();
        
        viewer.addPainter(coordinateSystemPainter);
        f.getContentPane().add(viewer, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(1,2));
        
        JCheckBox screenBasedCheckBox = new JCheckBox("Screen based");
        screenBasedCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (screenBasedCheckBox.isSelected())
                {
                    setScreenBasedLayout(coordinateSystemPainter, viewer);
                }
                else
                {
                    setWorldBasedLayout(coordinateSystemPainter);
                }
                viewer.repaint();
            }
        });
        controlPanel.add(screenBasedCheckBox);
        
        
        JCheckBox limitAxesCheckBox = new JCheckBox("Limit axes");
        limitAxesCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setAxisRanges(coordinateSystemPainter, 
                    limitAxesCheckBox.isSelected());
                viewer.repaint();
            }
        });
        controlPanel.add(limitAxesCheckBox);


        JButton resetDisplayedAreaButton = new JButton("Reset displayed area");
        resetDisplayedAreaButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                viewer.resetTransform();
                viewer.setDisplayedWorldArea(
                    new Rectangle2D.Double(0, 0, 
                        viewer.getWidth(), viewer.getHeight()));
            }
        });
        controlPanel.add(resetDisplayedAreaButton);
        
        f.getContentPane().add(controlPanel, BorderLayout.SOUTH);
        
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static void setScreenBasedLayout(
        CoordinateSystemPainter coordinateSystemPainter, JComponent viewer)
    {
        coordinateSystemPainter.setScreenAxisLayoutX(
            () -> 50, 
            () -> viewer.getWidth() - 50, 
            () -> viewer.getHeight() - 50);
        coordinateSystemPainter.setScreenAxisLayoutY(
            () -> viewer.getHeight() - 50, 
            () -> 50, 
            () -> 50);
        
    }
    
    private static void setWorldBasedLayout(
        CoordinateSystemPainter coordinateSystemPainter)
    {
        coordinateSystemPainter.setScreenAxisLayoutX(null, null, null);
        coordinateSystemPainter.setScreenAxisLayoutY(null, null, null);
    }
    
    private static void setAxisRanges(
        CoordinateSystemPainter coordinateSystemPainter, boolean limited)
    {
        if (limited)
        {
            coordinateSystemPainter.setAxisRangeX(-1.0, 1.0);
            coordinateSystemPainter.setAxisRangeY(-1.0, 1.0);
        }
        else
        {
            coordinateSystemPainter.setAxisRangeX(Double.NaN, Double.NaN);
            coordinateSystemPainter.setAxisRangeY(Double.NaN, Double.NaN);
        }
    }
    
    
}