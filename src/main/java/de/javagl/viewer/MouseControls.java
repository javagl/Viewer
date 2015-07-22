/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.viewer;

/**
 * Methods to create {@link MouseControl} instances
 */
public class MouseControls
{
    /**
     * Create a default {@link MouseControl} for the given {@link Viewer},
     * where rotation and non-uniform scaling are allowed. See 
     * {@link #createDefault(Viewer, boolean, boolean)} for details. 
     * 
     * @param viewer The {@link Viewer}
     * @return The {@link MouseControl}
     */
    public static MouseControl createDefault(Viewer viewer)
    {
        return createDefault(viewer, true, true);
    }
    
    /**
     * Create a default {@link MouseControl} for the given {@link Viewer}.<br>
     * <br>
     * The default interaction pattern is as follows:
     * <ul>
     *   <li>Left mouse drags: Rotate</li>
     *   <li>Right mouse drags: Translate</li>
     *   <li>
     *     Mouse wheel: Zoom uniformly
     *     <ul>
     *       <li>Shift + Mouse wheel: Zoom along x</li>
     *       <li>Ctrl + Mouse wheel: Zoom along y</li>
     *     </ul>  
     *   </li>
     * </ul>
     * The rotation always happens around the mouse position where the 
     * drag started. Zooming always refers to the current mouse position.
     * However, details about this interaction (e.g. the <i>speed</i> of
     * the rotation and zooming) are not specified.
     * 
     * @param viewer The {@link Viewer}
     * @param rotationAllowed Whether rotations are allowed
     * @param nonUniformScalingAllowed Whether non-uniform scaling along
     * the x- and y axis is allowed
     * @return The {@link MouseControl}
     */
    public static MouseControl createDefault(
        Viewer viewer, 
        boolean rotationAllowed, 
        boolean nonUniformScalingAllowed)
    {
        DefaultMouseControl defaultMouseControl =  
            new DefaultMouseControl(viewer);
        defaultMouseControl.setRotationAllowed(rotationAllowed);
        defaultMouseControl.setNonUniformScalingAllowed(
            nonUniformScalingAllowed);
        return defaultMouseControl;
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private MouseControls()
    {
        // Private constructor to prevent instantiation
    }
}
