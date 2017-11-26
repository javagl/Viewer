package de.javagl.viewer.glyphs.test;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Painters;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.glyphs.BoxPlot;
import de.javagl.viewer.glyphs.BoxPlotPainter;
import de.javagl.viewer.glyphs.BoxPlots;
 
/**
 * Simple integration test for the {@link BoxPlotPainter}
 */
public class BoxPlotPainterTest
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
       
        
        BoxPlot boxPlot = BoxPlots.create(0.1, 0.25, 0.4, 0.65, 0.8, 0.5);
        BoxPlotPainter boxPlotPainter = new BoxPlotPainter();

        Viewer viewer = new Viewer();
        viewer.addPainter(Painters.create(boxPlotPainter, boxPlot));
        viewer.setDisplayedWorldArea(-1, -1, 3, 3);
        
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}