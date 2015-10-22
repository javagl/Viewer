/*
 * www.javagl.de - Viewer - Functions
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 */
package de.javagl.viewer.painters;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.javagl.viewer.Painter;
import de.javagl.viewer.Viewer;

/**
 * A simple visual comparison test for the {@link StringBoundsUtils} class
 */
@SuppressWarnings("javadoc")
public class StringBoundsUtilsTest
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

    private static final Font DEFAULT_FONT = 
        new Font("Dialog", Font.PLAIN, 10);
    private static Font font = DEFAULT_FONT.deriveFont(10f);
    
    private static void createAndShowGUI()
    {
        JFrame f = new JFrame("Viewer");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
       
        Viewer viewer = new Viewer();

        String string = "AbcXyz";
        viewer.addPainter(new Painter()
        {
            @Override
            public void paint(Graphics2D g, AffineTransform worldToScreen, 
                double w, double h)
            {
                AffineTransform at = g.getTransform();
                g.setColor(Color.BLACK);
                g.setRenderingHint(
                    RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                Rectangle2D boundsA = 
                    computeStringBoundsDefault(string, font);
                Rectangle2D boundsB = 
                    StringBoundsUtils.computeStringBounds(string, font);
                
                g.setFont(new Font("Monospaced", Font.BOLD, 12));
                g.setColor(Color.GREEN);
                g.drawString(createString(boundsA), 10, 20);
                g.setColor(Color.RED);
                g.drawString(createString(boundsB), 10, 40);
                
                g.setFont(font);
                g.transform(worldToScreen);
                g.drawString(string, 0, 0);
                g.setTransform(at);
                
                g.setColor(Color.GREEN);
                g.draw(worldToScreen.createTransformedShape(boundsA));
                g.setColor(Color.RED);
                g.draw(worldToScreen.createTransformedShape(boundsB));
            }
        });
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        
        f.getContentPane().add(
            new JLabel("Mouse wheel: Zoom, "
                + "Right mouse drags: Move, "
                + "Left mouse drags: Rotate"), 
            BorderLayout.NORTH);
        
        JSpinner fontSizeSpinner = 
            new JSpinner(new SpinnerNumberModel(10.0, 0.1, 100.0, 0.1));
        fontSizeSpinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                Object object = fontSizeSpinner.getValue();
                Number number = (Number)object;
                float fontSize = number.floatValue();
                font = DEFAULT_FONT.deriveFont(fontSize);
                viewer.repaint();
            }
        });
        JPanel p = new JPanel();
        p.add(new JLabel("Font size"), BorderLayout.WEST);
        p.add(fontSizeSpinner, BorderLayout.CENTER);
        f.getContentPane().add(p, BorderLayout.SOUTH);
        

        viewer.setPreferredSize(new Dimension(1000,500));
        viewer.setDisplayedWorldArea(-15,-15,30,30);
        f.pack();
        viewer.setPreferredSize(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static String createString(Rectangle2D r)
    {
        return String.format(Locale.ENGLISH,
            "x=%12.4f y=%12.4f w=%12.4f h=%12.4f",
            r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
    
    private static final FontRenderContext DEFAULT_FONT_RENDER_CONTEXT =
        new FontRenderContext(null, true, true);
    public static Rectangle2D computeStringBoundsDefault(
        String string, Font font)
    {
        return font.getStringBounds(string, DEFAULT_FONT_RENDER_CONTEXT);        
    }
    
    
}
