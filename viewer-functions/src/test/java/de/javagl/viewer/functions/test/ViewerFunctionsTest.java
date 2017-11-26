/*
 * www.javagl.de - Viewer - Functions
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.functions.test;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.function.DoubleFunction;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.functions.FunctionPanel;

/**
 * Simple test and demonstration of the Viewer class
 */
public class ViewerFunctionsTest
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
        JFrame f = new JFrame("Viewer Functions");
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
       
        // Create the FunctionPanel
        FunctionPanel functionPanel = new FunctionPanel();
        functionPanel.setDisplayedWorldArea(
            new Rectangle2D.Double(-1.0, -1.0, 2.0, 2.0));

        // Create some functions
        DoubleFunction<Double> function0 = x -> Math.sin(x);
        DoubleFunction<Double> function1 = x -> x * x * x;
        DoubleFunction<Double> function2 = x ->
        {
            if (x < 0.0)
            {
                return Math.tan(x);
            }
            if (x <= 0.25)
            {
                return null;
            }
            return x * Math.sin(x);
        };

        // Add the functions to the FunctionPanel
        functionPanel.addFunction(
            function0, Color.RED, "Sine");
        functionPanel.addFunctionWithValueLegend(
            function1, new Color(0, 128, 0), "Cubic: x^3");
        functionPanel.addFunctionWithValueLegend(
            function2, Color.BLUE, "Custom");
        
        f.getContentPane().add(functionPanel, BorderLayout.CENTER);

        
        // Create the control panel
        JPanel controlPanel = new JPanel(new GridLayout(1,2));
        
        final JLabel infoLabel = new JLabel(" ");
        controlPanel.add(infoLabel);

        JButton resetDisplayedAreaButton = new JButton("Reset displayed area");
        resetDisplayedAreaButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                functionPanel.resetTransform();
                functionPanel.setDisplayedWorldArea(
                    new Rectangle2D.Double(-1.0, -1.0, 2.0, 2.0));
            }
        });
        controlPanel.add(resetDisplayedAreaButton);
        
        f.getContentPane().add(controlPanel, BorderLayout.SOUTH);
        
        functionPanel.addMouseMotionListener(
            new ViewerFunctionsTestInfoHandler(functionPanel, infoLabel));
       
        
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}