/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */

package de.javagl.viewer.test;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Viewer;

/**
 * Simple integration test and demonstration of the {@link Viewer} class
 */
public class ViewerTest
{
    /**
     * The entry point of this test
     *
     * @param args Not used
     */
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
       
        Viewer viewer = new Viewer();
        viewer.addPainter(new ViewerTestPainterGraphics());
        viewer.addPainter(new ViewerTestPainterShapes());
        f.getContentPane().add(viewer, BorderLayout.CENTER);

        JPanel configPanel = createConfigPanel(viewer);
        f.getContentPane().add(configPanel, BorderLayout.EAST);

        JLabel infoLabel = new JLabel(" ");
        f.getContentPane().add(infoLabel, BorderLayout.SOUTH);
        viewer.addMouseMotionListener(
            new ViewerTestInfoHandler(infoLabel, viewer));
       
        viewer.setPreferredSize(new Dimension(500,500));
        f.pack();
        viewer.setPreferredSize(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    /**
     * Create the config panel for the viewer
     * 
     * @param viewer The viewer
     * @return The config panel
     */
    static JPanel createConfigPanel(Viewer viewer)
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new GridLayout(0,1));
        
        JCheckBox flippedVerticallyCheckBox = 
            new JCheckBox("Flipped vertically", false);
        flippedVerticallyCheckBox.addActionListener(
           e -> viewer.setFlippedVertically(
               flippedVerticallyCheckBox.isSelected()));
        controlPanel.add(flippedVerticallyCheckBox);
        
        JCheckBox maintainAspectRatioCheckBox = 
            new JCheckBox("Maintain aspect ratio", true);
        maintainAspectRatioCheckBox.addActionListener(
           e -> viewer.setMaintainAspectRatio(
               maintainAspectRatioCheckBox.isSelected()));
        controlPanel.add(maintainAspectRatioCheckBox);
        
        JCheckBox resizingContentsCheckBox = 
            new JCheckBox("Resizing contents", false);
        resizingContentsCheckBox.addActionListener(
           e -> viewer.setResizingContents(
               resizingContentsCheckBox.isSelected()));
        controlPanel.add(resizingContentsCheckBox);
        
        JButton setDisplayedWorldAreaButton = 
            new JButton("Set displayed world area to (0,0)-(100,100)");
        setDisplayedWorldAreaButton.addActionListener(
            e -> viewer.setDisplayedWorldArea(
                new Rectangle2D.Double(0,0,100,100)));
        controlPanel.add(setDisplayedWorldAreaButton);
        
        JButton resetTransformButton = 
            new JButton("Reset transform");
        resetTransformButton.addActionListener(
            e -> viewer.resetTransform());
        controlPanel.add(resetTransformButton);
        
        configPanel.add(controlPanel, BorderLayout.NORTH);
        
        return configPanel;
    }
    
}