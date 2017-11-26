# viewer-functions

A simple function plotter

![ViewerFunctionsScreenshot01.png](/screenshots/ViewerFunctionsScreenshot01.png)

The [FunctionPanel](https://github.com/javagl/ViewerFunctions/blob/master/src/main/java/de/javagl/viewer/functions/FunctionPanel.java) 
is a [Viewer](https://github.com/javagl/Viewer) for `DoubleFunction`s. 
It allows zooming and translating the view, and paints labeled axes
and legends. 

The basic usage is demonstrated in the 
[ViewerFunctionsTest](https://github.com/javagl/ViewerFunctions/blob/master/src/test/java/de/javagl/viewer/functions/test/ViewerFunctionsTest.java)
class.

Currently, the function plotter does not allow much configuration,
as the focus was on the ease of use: A few lines of codes are
sufficient to create a simple plot:

![ViewerFunctionsScreenshot02.png](/screenshots/ViewerFunctionsScreenshot02.png)


# Changes

0.1.0-SNAPSHOT :
  * Project restructuring, combining the different viewer libraries

0.0.2 : 

  * Removed `AxesPainter` and replaced it with the
    `CoordinateSystemPainter` that was added in version 0.0.3 
    of the main [Viewer](https://github.com/javagl/Viewer) project

0.0.1 : 

  * Initial commit
