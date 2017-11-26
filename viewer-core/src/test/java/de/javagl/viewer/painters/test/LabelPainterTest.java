package de.javagl.viewer.painters.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Predicate;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.javagl.viewer.SimpleObjectPainter;
import de.javagl.viewer.Viewer;
import de.javagl.viewer.painters.LabelPainter;
import de.javagl.viewer.painters.LabelPainter.LabelPaintState;
import de.javagl.viewer.painters.LabelPainterPredicates;

/**
 * Integration test for the {@link LabelPainter} class
 */
@SuppressWarnings("javadoc")
public class LabelPainterTest
{
    private static final Font DEFAULT_FONT = 
        new Font("Sans Serif", Font.PLAIN, 9);
    
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
       
        Viewer viewer = new Viewer();
        LabelPainter labelPainter = new LabelPainter();
        
        labelPainter.setFont(DEFAULT_FONT.deriveFont(20.3f));
        labelPainter.setTransformingLabels(false);
        labelPainter.setLabelAnchor(0.0, 0.0);
        labelPainter.setLabelLocation(0.0, 0.0);
        
        Predicate<LabelPaintState> p0 = 
            LabelPainterPredicates.transformedLabelWidthAtLeast(20);
        Predicate<LabelPaintState> p1 = 
            LabelPainterPredicates.labelWidthAtMost(60);
        labelPainter.setLabelPaintingCondition(p0.and(p1));
        
        SimpleObjectPainter<String> labelObjectPainter =
            new SimpleObjectPainter<String>(labelPainter);
        labelObjectPainter.setObject("Test");
        
        viewer.addPainter(labelObjectPainter);
        viewer.addPainter(new LabelPainterPainter(labelPainter, labelObjectPainter));
        
        f.getContentPane().add(viewer, BorderLayout.CENTER);
        
        f.getContentPane().add(
            createControlPanel(viewer, labelPainter, labelObjectPainter), BorderLayout.SOUTH);

        viewer.setPreferredSize(new Dimension(500,500));
        viewer.setDisplayedWorldArea(-5,-5,10,10);
        f.pack();
        viewer.setPreferredSize(null);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    private static JPanel createControlPanel(
        Viewer viewer, LabelPainter labelPainter,  
        SimpleObjectPainter<String> labelObjectPainter)
    {
        JPanel controlPanel = new JPanel(new GridLayout(0,1));
        
        PointEditingPanel anchorEditingPanel = 
            new PointEditingPanel("anchor", p ->
        { 
            labelPainter.setLabelAnchor(p.getX(), p.getY()); 
            viewer.repaint(); 
        });
        controlPanel.add(anchorEditingPanel);
        
        PointEditingPanel locationEditingPanel = 
            new PointEditingPanel("location", p ->
        { 
            labelPainter.setLabelLocation(p.getX(), p.getY()); 
            viewer.repaint(); 
        });
        controlPanel.add(locationEditingPanel);
        
        JPanel anglePanel = new JPanel(new GridLayout(1,2));
        anglePanel.add(new JLabel("angle"));
        anglePanel.add(createSlider(x -> 
        { 
            labelPainter.setAngle(x * Math.PI * 2); 
            viewer.repaint(); 
        }));
        controlPanel.add(anglePanel);
        
        JPanel fontSizePanel = createFonSizePanel(viewer, labelPainter);
        controlPanel.add(fontSizePanel);
        
        JPanel checkboxPanel = new JPanel(new GridLayout(0,1));
        JCheckBox transformingLabelsCheckBox = 
            new JCheckBox("transforming labels", 
                labelPainter.isTransformingLabels());
        transformingLabelsCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                labelPainter.setTransformingLabels(
                    transformingLabelsCheckBox.isSelected()); 
                viewer.repaint(); 
            }
        });
        checkboxPanel.add(transformingLabelsCheckBox);
        
        controlPanel.add(checkboxPanel);
        
        
        JTextField labelTextField = new JTextField("Test");
        labelTextField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                labelObjectPainter.setObject(labelTextField.getText());                
                viewer.repaint(); 
            }
        });
        controlPanel.add(labelTextField);
        
        
        return controlPanel;
    }

    private static JPanel createFonSizePanel(Viewer viewer,
        LabelPainter labelPainter)
    {
        JPanel fontSizePanel = new JPanel(new GridLayout(0,1));
        fontSizePanel.add(new JLabel("Font size"));
        JSpinner fontSizeSpinner = new JSpinner(new SpinnerNumberModel(
            labelPainter.getFont().getSize2D(), 0.1, 100.0, 0.1));
        fontSizeSpinner.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                Object object = fontSizeSpinner.getValue();
                Number number = (Number)object;
                float fontSize = number.floatValue();
                labelPainter.setFont(DEFAULT_FONT.deriveFont(fontSize));
                viewer.repaint();
            }
        });
        fontSizePanel.add(fontSizeSpinner);
        return fontSizePanel;
    }
    
    static class PointEditingPanel extends JPanel
    {
        private static final long serialVersionUID = 7886478603516414700L;
        private final Point2D point;
        
        public PointEditingPanel(String name, Consumer<Point2D> pointConsumer)
        {
            super(new GridLayout(2,2));
            
            this.point = new Point2D.Double();
            
            add(new JLabel(name+" X"));
            add(createSlider(x -> 
            { 
                point.setLocation(x, point.getY());
                pointConsumer.accept(
                    new Point2D.Double(point.getX(), point.getY()));
            }));
            
            add(new JLabel(name+" Y"));
            add(createSlider(y -> 
            { 
                point.setLocation(point.getX(), y);
                pointConsumer.accept(
                    new Point2D.Double(point.getX(), point.getY()));
            }));
        }
    }
    
    private static JSlider createSlider(DoubleConsumer doubleConsumer)
    {
        JSlider slider = new JSlider(0, 100, 0);
        slider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int value = slider.getValue();
                double v = value / 100.0;
                doubleConsumer.accept(v);
            }
        });
        return slider;
    }
    
}
