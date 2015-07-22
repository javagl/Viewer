# Viewer
A Java Swing Panel that allows rotation, translation and zooming

![ViewerScreenshot01.png](/screenshots/ViewerScreenshot01.png)

The `Viewer` class is a `JPanel` that allows rotating, 
translating and zooming the painted objects. The actual
painting is therefore delegated to the `Painter` interface,
which receives information about the world-to-screen 
transormation as an `AffineTransform`, and may paint the
the transformed elements.

These control of the viewer is usually done with `MouseListener`s,
`MouseMotionListener`s and `MouseWheelListener`s. Default 
implementations (for default controls) are summarized in the
`MouseControls` class, which can conveniently be attached to
the viewer. 



