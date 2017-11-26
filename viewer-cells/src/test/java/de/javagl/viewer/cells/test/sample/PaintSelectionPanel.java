package de.javagl.viewer.cells.test.sample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * A simple panel for selecting a {@link Paint}
 */
@SuppressWarnings({"javadoc", "serial"})
class PaintSelectionPanel extends JPanel
{
    private final JComboBox<Paint> paintComboBox;
    private final List<Consumer<Paint>> paintListeners;
    
    PaintSelectionPanel()
    {
        super(new BorderLayout(1,1));

        paintListeners = new CopyOnWriteArrayList<Consumer<Paint>>();

        List<Paint> paints = new ArrayList<Paint>();
        paints.add(null);
        paints.add(Color.GRAY);
        paints.add(Color.WHITE);
        paints.add(Color.RED);
        paints.add(Color.YELLOW);
        paints.add(Color.GREEN);
        paints.add(Color.CYAN);
        paints.add(Color.BLUE);
        paints.add(Color.MAGENTA);
        paints.add(Color.BLACK);
        
        GradientPaint g0 = new GradientPaint(
            new Point(0,0), new Color(255,64,64), 
            new Point(1,1), new Color(64,0,0));
        paints.add(g0);

        GradientPaint g1 = new GradientPaint(
            new Point(0,0), new Color(64,255,64), 
            new Point(1,1), new Color(0,64,0));
        paints.add(g1);

        GradientPaint g2 = new GradientPaint(
            new Point(0,0), new Color(64,64,255), 
            new Point(1,1), new Color(0,0,64));
        paints.add(g2);
        
        paintComboBox = new JComboBox<Paint>(
            paints.toArray(new Paint[0]));
        
        paintComboBox.setPreferredSize(new Dimension(100,30));
        
        ListCellRenderer<Paint> renderer = new ListCellRenderer<Paint>()
        {
            private PaintPanel paintPanel = new PaintPanel();
            
            @Override
            public Component getListCellRendererComponent(
                JList<? extends Paint> list, Paint value, int index,
                boolean isSelected, boolean cellHasFocus)
            {
                paintPanel.setPaint(value);
                return paintPanel;
            }
        };
        paintComboBox.setRenderer(renderer);
        
        paintComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object object = paintComboBox.getSelectedItem();
                Paint paint = (Paint)object;
                notifyPaintListeners(paint);
            }
        });
        
        add(paintComboBox, BorderLayout.NORTH);
    }

    private void notifyPaintListeners(Paint paint)
    {
        for (Consumer<Paint> paintListener : paintListeners)
        {
            paintListener.accept(paint);
        }
    }
    
    void setSelectedPaint(Paint paint)
    {
        paintComboBox.setSelectedItem(paint);
    }
    
    void addPaintListener(Consumer<Paint> paintListener)
    {
        paintListeners.add(paintListener);
    }
    
    void removePaintListener(Consumer<Paint> paintListener)
    {
        paintListeners.remove(paintListener);
    }
    
}