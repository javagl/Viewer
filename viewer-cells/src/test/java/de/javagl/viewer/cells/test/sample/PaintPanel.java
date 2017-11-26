package de.javagl.viewer.cells.test.sample;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JPanel;

/**
 * A simple panel for showing a Paint
 */
@SuppressWarnings({"javadoc", "serial"})
class PaintPanel extends JPanel
{
    private Paint paint;
    
    void setPaint(Paint paint)
    {
        this.paint = paint;
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(100,30);
    }
    
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        if (paint != null)
        {
            g.setPaint(paint);
            g.scale(getWidth(), getHeight());
            g.fillRect(0, 0, 1, 1);
        }
        else
        {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.drawLine(0, 0, getWidth(), getHeight());
            g.drawLine(0, getHeight(), getWidth(), 0);
        }
    }
}