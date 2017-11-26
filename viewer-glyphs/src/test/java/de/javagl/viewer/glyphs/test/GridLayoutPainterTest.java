package de.javagl.viewer.glyphs.test;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.GridLayoutPainter;
 
/**
 * Simple integration test for the {@link GridLayoutPainter}
 */
public class GridLayoutPainterTest
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
       
        
        BackgroundPainter painter = 
            BackgroundPainters.createVerticalGradient(
                Color.LIGHT_GRAY, Color.DARK_GRAY);
        
        int cellsX = 4;
        int cellsY = 4;
        GridLayoutPainter gridLayoutPainter = 
            new GridLayoutPainter(cellsX, cellsY, 0.125, 0.125);
        for (int y = 0; y < cellsY; y++)
        {
            for (int x = 0; x < cellsX; x++)
            {
                gridLayoutPainter.setDelegate(x, y, painter);
            }
        }
        
        Viewer viewer = new Viewer();
        viewer.addPainter(gridLayoutPainter);
        viewer.setDisplayedWorldArea(-1, -1, 3, 3);
        
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}