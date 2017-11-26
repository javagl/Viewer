package de.javagl.viewer.cells.test.sample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.javagl.viewer.cells.BasicCellPainter;
import de.javagl.viewer.cells.Cell;
import de.javagl.viewer.cells.CellMap;
import de.javagl.viewer.cells.CellMapPanel;

/**
 * A simple panel for showing a {@link CellMap} with a {@link BasicCellPainter}
 */
@SuppressWarnings({"javadoc", "serial"})
class ViewerCellsSamplePanel extends JPanel
{
    private final CellMapPanel cellMapPanel;
    private final BasicCellPainter basicCellPainter;
    
    ViewerCellsSamplePanel()
    {
        super(new BorderLayout());
        
        cellMapPanel = new CellMapPanel();
        add(cellMapPanel, BorderLayout.CENTER);
        
        basicCellPainter = new BasicCellPainter()
        {
            @Override
            protected void paintCellContent(
                Graphics2D g, AffineTransform worldToScreen,
                double w, double h, Cell cell)
            {
                paintCellContentShape(g, worldToScreen, w, h, cell);
                
                // Example of custom rendering code
                if (cell.getX() == 2 && cell.getY() == 2)
                {
                    g.setColor(Color.RED);
                    g.draw(worldToScreen.createTransformedShape(
                        new Line2D.Double(0,0,1,1)));
                    g.draw(worldToScreen.createTransformedShape(
                        new Line2D.Double(0,1,1,0)));
                }
                
            }
        };
        basicCellPainter.setLabelFunction(
            (cell) -> cell.getX() == 2 && cell.getY() == 2 ? 
                "Custom painting" : cell.getX()+","+cell.getY());
        basicCellPainter.setLabelPaint(Color.BLACK);

        cellMapPanel.addCellPainter(basicCellPainter, 0);
        
        
        JPanel controlPanel = new JPanel();
        
        CellMapEditorPanel cellMapEditorPanel = new CellMapEditorPanel();
        Consumer<CellMap> cellMapListener = new Consumer<CellMap>()
        {
            @Override
            public void accept(CellMap t)
            {
                cellMapPanel.setCellMap(t);
                cellMapPanel.repaint();
            }
        };
        cellMapEditorPanel.addCellMapListener(cellMapListener);
        cellMapPanel.setCellMap(cellMapEditorPanel.getCellMap());
        controlPanel.add(cellMapEditorPanel);
        
        JPanel paintsPanel = new JPanel(new GridLayout(0,1));
        
        paintsPanel.add(createPaintSelectionPanel("Draw", 
            t -> basicCellPainter.setDrawPaint(t), Color.BLACK));
        paintsPanel.add(createPaintSelectionPanel("Fill", 
            t -> basicCellPainter.setFillPaint(t), Color.GRAY));

        paintsPanel.add(createPaintSelectionPanel("ContentDraw", 
            t -> basicCellPainter.setContentDrawPaint(t), Color.BLUE));
        paintsPanel.add(createPaintSelectionPanel("ContentFill", 
            t -> basicCellPainter.setContentFillPaint(t), Color.CYAN));
        
        controlPanel.add(paintsPanel);
        
        
        
        JPanel fontsPanel = new JPanel(new BorderLayout());
        
        JCheckBox transformingLabelsCheckBox = 
            new JCheckBox("Transform Labels", false);
        transformingLabelsCheckBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean b = transformingLabelsCheckBox.isSelected();
                if (b)
                {
                    basicCellPainter.setLabelFont(
                        new Font("Dialog", Font.PLAIN, 1).deriveFont(0.2f));
                }
                else
                {
                    basicCellPainter.setLabelFont(
                        new Font("Dialog", Font.PLAIN, 1).deriveFont(10.0f));
                }
                basicCellPainter.setTransformingLabels(b);
                cellMapPanel.repaint();
            }
        });
        
        fontsPanel.add(transformingLabelsCheckBox, BorderLayout.NORTH);
        
        controlPanel.add(fontsPanel);
        
        
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createPaintSelectionPanel(
        String name, Consumer<Paint> consumerMethod, Paint defaultPaint)
    {
        JPanel p = new JPanel(new GridLayout(1,2));
        p.add(new JLabel(name));
        
        PaintSelectionPanel paintSelectionPanel = new PaintSelectionPanel();
        Consumer<Paint> paintListener = new Consumer<Paint>()
        {
            @Override
            public void accept(Paint t)
            {
                consumerMethod.accept(t);
                cellMapPanel.repaint();
            }
        };
        paintSelectionPanel.addPaintListener(paintListener);
        paintSelectionPanel.setSelectedPaint(defaultPaint);
        p.add(paintSelectionPanel);
        return p;
    }
    
    CellMapPanel getCellMapPanel()
    {
        return cellMapPanel;
    }
}