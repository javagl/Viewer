package de.javagl.viewer.cells.test.sample;

import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A simple panel for editing a Point2D
 */
@SuppressWarnings({"javadoc", "serial"})
class PointEditorPanel extends JPanel
{
    private final List<Consumer<Point2D>> pointListeners;
    
    PointEditorPanel(Consumer<Point2D> pointConsumer)
    {
        super(new GridLayout(2,2));
        
        this.pointListeners = new CopyOnWriteArrayList<Consumer<Point2D>>();
        
        add(new JLabel("X:"));
        JSpinner spinnerX = 
            new JSpinner(new SpinnerNumberModel(0.0,  0.0, 1.0, 0.1));
        add(spinnerX);

        add(new JLabel("Y:"));
        JSpinner spinnerY = 
            new JSpinner(new SpinnerNumberModel(0.0,  0.0, 1.0, 0.1));
        add(spinnerY);
        
        ChangeListener changeListener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                Object valueX = spinnerX.getValue();
                Number numberX = (Number)valueX;
                double x = numberX.doubleValue();

                Object valueY = spinnerY.getValue();
                Number numberY = (Number)valueY;
                double y = numberY.doubleValue();
                
                notifyPointListeners(new Point2D.Double(x,y));
            }
        };
        spinnerX.addChangeListener(changeListener);
    }
    
    private void notifyPointListeners(Point2D point)
    {
        for (Consumer<Point2D> pointListener : pointListeners)
        {
            pointListener.accept(point);
        }
    }
    
    void addPointListener(Consumer<Point2D> pointListener)
    {
        pointListeners.add(pointListener);
    }

    void removePointListener(Consumer<Point2D> pointListener)
    {
        pointListeners.remove(pointListener);
    }
}