/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.test;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.LinkedMouseControls;
import de.javagl.viewer.Viewer;

/**
 * Simple integration test for the {@link LinkedMouseControls} class
 */
@SuppressWarnings("javadoc")
public class LinkedMouseControlsTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui()
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel container = new JPanel(new GridLayout(0, 1));

        int n = 3;
        List<Viewer> viewers = new ArrayList<Viewer>();
        for (int i = 0; i < n; i++)
        {
            Viewer viewer = new Viewer();
            viewer.addPainter(new ViewerTestPainterGraphics());
            viewers.add(viewer);

            JPanel panel = new JPanel(new GridLayout(1, 1));
            panel.setBorder(BorderFactory.createTitledBorder("Viewer " + i));
            panel.add(viewer);
            container.add(panel);
        }
        LinkedMouseControls.connect(viewers);

        f.getContentPane().add(container);
        f.setSize(1200, 800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

}
