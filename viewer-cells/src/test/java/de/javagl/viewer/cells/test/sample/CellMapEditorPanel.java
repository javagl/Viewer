package de.javagl.viewer.cells.test.sample;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.javagl.hexagon.HexagonGrid;
import de.javagl.hexagon.HexagonGrids;
import de.javagl.viewer.cells.CellMap;
import de.javagl.viewer.cells.CellMaps;

/**
 * A simple panel for editing a {@link CellMap}
 */
@SuppressWarnings({"javadoc", "serial"})
class CellMapEditorPanel extends JPanel
{
    private final JSpinner sizeSpinnerX;
    private final JSpinner sizeSpinnerY;
    private final JSpinner cellSizeSpinner;
    private final JCheckBox hexagonalCheckBox;
    private final JCheckBox verticalCheckBox;
    private final JCheckBox evenShiftedCheckBox;
    
    private final List<Consumer<CellMap>> cellMapListeners;
    
    CellMapEditorPanel()
    {
        super(new BorderLayout());
        
        this.cellMapListeners = new CopyOnWriteArrayList<Consumer<CellMap>>();

        JPanel controlPanel = new JPanel(new BorderLayout());

        JPanel sizesPanel = new JPanel(new GridLayout(0,2));
        
        ChangeListener changeListener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                notifyCellMapListeners(getCellMap());
            }
        };
        

        sizeSpinnerX = new JSpinner(new SpinnerNumberModel(4, 1, 10000, 1));
        sizeSpinnerX.addChangeListener(changeListener);
        sizesPanel.add(new JLabel("Size X:"));
        sizesPanel.add(sizeSpinnerX);

        sizeSpinnerY = new JSpinner(new SpinnerNumberModel(3, 1, 10000, 1));
        sizeSpinnerY.addChangeListener(changeListener);
        sizesPanel.add(new JLabel("Size Y:"));
        sizesPanel.add(sizeSpinnerY);

        cellSizeSpinner = new JSpinner(
            new SpinnerNumberModel(10.0, 1.0, 100.0, 1.0));
        cellSizeSpinner.addChangeListener(changeListener);
        sizesPanel.add(new JLabel("Cell size:"));
        sizesPanel.add(cellSizeSpinner);

        controlPanel.add(sizesPanel, BorderLayout.NORTH);

        
        ActionListener actionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                notifyCellMapListeners(getCellMap());
            }
        };
        
        JPanel checkBoxesPanel = new JPanel(new GridLayout(0,1));
        
        hexagonalCheckBox = new JCheckBox("Hexagonal", true);
        hexagonalCheckBox.addActionListener(actionListener);
        checkBoxesPanel.add(hexagonalCheckBox);

        verticalCheckBox = new JCheckBox("Vertical");
        verticalCheckBox.addActionListener(actionListener);
        checkBoxesPanel.add(verticalCheckBox);

        evenShiftedCheckBox = new JCheckBox("Even Shifted");
        evenShiftedCheckBox.addActionListener(actionListener);
        checkBoxesPanel.add(evenShiftedCheckBox);
        
        
        controlPanel.add(checkBoxesPanel, BorderLayout.CENTER);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    void addCellMapListener(Consumer<CellMap> cellMapListener)
    {
        cellMapListeners.add(cellMapListener);
    }

    void removeCellMapListener(Consumer<CellMap> cellMapListener)
    {
        cellMapListeners.remove(cellMapListener);
    }
    
    CellMap getCellMap()
    {
        if (isHexagonal())
        {
            HexagonGrid hexagonGrid = HexagonGrids.create(
                getCellSize(), isVertical(), isEvenShifted());
            CellMap cellMap = CellMaps.createHexagon(
                getSizeX(), getSizeY(), hexagonGrid);
            return cellMap;
        }

        CellMap cellMap = CellMaps.createRectangle(
            getSizeX(), getSizeY(), getCellSize(), getCellSize());
        return cellMap;
    }
    
    private void notifyCellMapListeners(CellMap cellMap)
    {
        for (Consumer<CellMap> cellMapListener : cellMapListeners)
        {
            cellMapListener.accept(cellMap);
        }
    }

    private int getSizeX()
    {
        Object object = sizeSpinnerX.getValue();
        Number number = (Number)object;
        return number.intValue();
    }
    private int getSizeY()
    {
        Object object = sizeSpinnerY.getValue();
        Number number = (Number)object;
        return number.intValue();
    }
    private double getCellSize()
    {
        Object object = cellSizeSpinner.getValue();
        Number number = (Number)object;
        return number.doubleValue();
    }
    private boolean isHexagonal()
    {
        return hexagonalCheckBox.isSelected();
    }
    private boolean isVertical()
    {
        return verticalCheckBox.isSelected();
    }
    private boolean isEvenShifted()
    {
        return evenShiftedCheckBox.isSelected();
    }
    

}