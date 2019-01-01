package de.javagl.viewer.painters.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.javagl.viewer.Painter;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.painters.TooltipPainter;

/**
 * Integration test for the {@link TooltipPainter} class
 */
@SuppressWarnings("javadoc")
public class TooltipPainterTest
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
       
        Viewer viewer = new Viewer();
        
        TooltipPainter tooltipPainter = new TooltipPainter();
        
        tooltipPainter.set(0.0, 0.0, "At 0.000, 0.000");
        
        Painter originPainter = new Painter()
        {
            @Override
            public void paint(Graphics2D g, AffineTransform worldToScreen, 
                double w, double h)
            {
                int x = (int)worldToScreen.getTranslateX();
                int y = (int)worldToScreen.getTranslateY();
                g.setColor(Color.RED);
                g.drawLine(x - 5, y, x + 5, y);
                g.drawLine(x, y - 5, x, y + 5);
            }
        };
        viewer.addPainter(originPainter);
        viewer.addPainter(tooltipPainter);
        
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        JPanel controlPanel = 
            createControlPanel(viewer, tooltipPainter);
        f.getContentPane().add(
            controlPanel, BorderLayout.SOUTH);

        viewer.setPreferredSize(new Dimension(500,500));
        viewer.setDisplayedWorldArea(-5,-5,10,10);
        f.pack();
        viewer.setPreferredSize(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static JPanel createControlPanel(
        Viewer viewer, TooltipPainter tooltipPainter)
    {
        JPanel controlPanel = new JPanel(new GridLayout(0,1));
        
        PointEditingPanel positionEditingPanel = 
            new PointEditingPanel("position", p ->
        { 
            String string = String.format(Locale.ENGLISH, 
                "At %.3f, %.3f", p.getX(), p.getY());
            tooltipPainter.set(p.getX(), p.getY(), string); 
            viewer.repaint(); 
        }, -1.0, 2.0);
        controlPanel.add(positionEditingPanel);
        
        PointEditingPanel anchorEditingPanel = 
            new PointEditingPanel("anchor", p ->
        { 
            tooltipPainter.setAnchor(p.getX(), p.getY()); 
            viewer.repaint(); 
        }, -1.0, 2.0);
        controlPanel.add(anchorEditingPanel);
        
        return controlPanel;
    }
    
    static class PointEditingPanel extends JPanel
    {
        private static final long serialVersionUID = 7886478603516414700L;
        private final Point2D point;
        
        public PointEditingPanel(String name, 
            Consumer<Point2D> pointConsumer, double min, double max)
        {
            super(new GridLayout(2,2));
            
            this.point = new Point2D.Double();
            
            add(new JLabel(name+" X"));
            add(createSlider(x -> 
            { 
                point.setLocation(min + x * (max - min), point.getY());
                pointConsumer.accept(
                    new Point2D.Double(point.getX(), point.getY()));
            }));
            
            add(new JLabel(name+" Y"));
            add(createSlider(y -> 
            { 
                point.setLocation(point.getX(), min + y * (max - min));
                pointConsumer.accept(
                    new Point2D.Double(point.getX(), point.getY()));
            }));
        }
    }
    
    private static JSlider createSlider(DoubleConsumer doubleConsumer)
    {
        JSlider slider = new JSlider(0, 150, 50);
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int value = slider.getValue();
                double v = value / 150.0;
                doubleConsumer.accept(v);
            }
        });
        return slider;
    }
    
}
