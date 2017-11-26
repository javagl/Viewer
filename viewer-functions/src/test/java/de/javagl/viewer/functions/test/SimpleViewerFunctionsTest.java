/*
 * www.javagl.de - Viewer - Functions
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.functions.test;
import java.awt.Color;
import java.util.function.DoubleFunction;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.javagl.viewer.MouseControls;
import de.javagl.viewer.functions.FunctionPanel;

/**
 * Simple test and demonstration of the Viewer class
 */
public class SimpleViewerFunctionsTest
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

        FunctionPanel functionPanel = new FunctionPanel();
        functionPanel.setMouseControl(MouseControls.createDefault(functionPanel));
        DoubleFunction<Double> function0 = Math::sin;
        functionPanel.addFunctionWithValueLegend(
            function0, Color.RED, "Sine");
        functionPanel.setDisplayedWorldArea(
            -1.0, -2.0, 12.0, 4.0);

        f.getContentPane().add(functionPanel);

        f.setSize(300,300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}