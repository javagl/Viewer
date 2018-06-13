# viewer-cells

A viewer and renderer for rectangle and hexagon cell maps

![ViewerCellsScreenshot01.png](/screenshots/ViewerCellsScreenshot01.png)

The [CellMapPanel](https://github.com/javagl/Viewer/blob/master/viewer-cells/src/main/java/de/javagl/viewer/cells/CellMapPanel.java) 
is a [Viewer](https://github.com/javagl/Viewer)
for maps of rectangular or [Hexagon](https://github.com/javagl/Hexagon)
cells. The maps may be zoomed, translated and rotated. Custom painting
operations may be performed for the cells. 
The [BasicCellPainter](https://github.com/javagl/Viewer/blob/master/viewer-cells/src/main/java/de/javagl/viewer/cells/BasicCellPainter.java)
class offers an easy way to configure how the cells are painted,
and to configure the border- and fill colors for the cell shapes,
labels and fonts. Additionally, it may serve as a base class for own
implementations of cell painters.

The basic usage is demonstrated in the 
[ViewerCellsTest](https://github.com/javagl/Viewer/blob/master/viewer-cells/src/test/java/de/javagl/viewer/cells/test/ViewerCellsTest.java)
class.


# Changes

0.1.0 :
  * Project restructuring, combining the different viewer libraries

0.0.2 : 

  * Refactored `BasicCellPainter` to use the `LabelPainter` from
    `de.javagl:viewer:0.0.3`

0.0.1 : 

  * Initial commit
