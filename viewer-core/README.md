# viewer-core

A Java Swing Panel that allows rotation, translation and zooming

![ViewerScreenshot01.png](/screenshots/ViewerScreenshot01.png)

The `Viewer` class is a `JPanel` that allows rotating, 
translating and zooming the painted objects. The actual
painting is therefore delegated to the `Painter` interface,
which receives information about the world-to-screen 
transformation as an `AffineTransform`, and may paint the
the transformed elements.

The basic usage is demonstrated in the 
[ViewerTest](https://github.com/javagl/Viewer/blob/master/src/test/java/de/javagl/viewer/test/ViewerTest.java)
class, which also demonstrates some of the additional 
capabilities, and uses two implementations of the 
`Painter` interface to show the crucial difference between
applying a transform to the `Graphics2D`, and applying
a transform to the painted objects.

The control of the viewer is usually done with `MouseListener`s,
`MouseMotionListener`s and `MouseWheelListener`s. Default 
implementations (for default controls) are summarized in the
`MouseControls` class, which can conveniently be attached to
the viewer. 


# Changes

0.X.X : 
  * ...

0.1.1 :
  * The default `MouseControl` now allows rotation and translation
    only when no modifier (SHIFT, ALT, ALT-GR or CTRL) is pressed.
    This allows adding specific functionality for the cases where
    these modifiers are pressed.

0.1.0 :
  * Project restructuring, combining the different viewer libraries

0.0.3 : 

  * Replaced `GeomUtils` class with dependency to `de.javagl:geom:0.0.1`
  * Added zoom limiting in `Viewer` to prevent rendering errors for very
    large or very small zooming factors
  * Added the `painters` package, containing default `Painter` implementations
  * Added methods in the `Painters` class to create transformed and 
    composed `Painter` instances.

0.0.2 : 

  * Added convenience methods in viewer
  * Handled the case that the state of maintaining the
    aspect ratio interferes with setting the world area

0.0.1 : 

  * Initial commit
