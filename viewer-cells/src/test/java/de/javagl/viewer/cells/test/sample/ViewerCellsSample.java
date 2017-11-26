package de.javagl.viewer.cells.test.sample;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.javagl.viewer.Viewer;
import de.javagl.viewer.cells.CellMapPanel;
import de.javagl.viewer.cells.test.ViewerCellsTestInfoHandler;

/**
 * Integration test and demonstration of the cell rendering
 * using the {@link Viewer} class
 */
public class ViewerCellsSample
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
        JFrame f = new JFrame("ViewerCells");
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

        ViewerCellsSamplePanel viewerCellsSamplePanel =
            new ViewerCellsSamplePanel();
        f.getContentPane().add(viewerCellsSamplePanel, BorderLayout.CENTER);

        JLabel infoLabel = new JLabel(" ");
        f.getContentPane().add(infoLabel, BorderLayout.SOUTH);

        CellMapPanel cellMapPanel = viewerCellsSamplePanel.getCellMapPanel();
        cellMapPanel.addMouseMotionListener(
            new ViewerCellsTestInfoHandler(
                infoLabel, cellMapPanel));
        
        cellMapPanel.setPreferredSize(new Dimension(500,500));
        f.pack();
        cellMapPanel.setPreferredSize(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

