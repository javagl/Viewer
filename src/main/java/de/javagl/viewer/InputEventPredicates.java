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

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.function.Predicate;

/**
 * Predicates for input events.<br>
 * <br>
 * This class is not part of the public API, and may be omitted in the future.
 */
class InputEventPredicates
{
    /**
     * Returns a predicate that is always false
     * 
     * @return The predicate
     */
    static <T> Predicate<T> alwaysFalse()
    {
        return Predicates.create(t -> false, "false");
    }
    
    /**
     * Returns a predicate that is always true
     * 
     * @return The predicate
     */
    static <T> Predicate<T> alwaysTrue()
    {
        return Predicates.create(t -> true, "true");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * is a MOUSE_PRESSED event
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> mousePressed()
    {
        return Predicates.create(
            t -> t.getID() == MouseEvent.MOUSE_PRESSED, "mousePressed");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * is a MOUSE_RELEASED event
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> mouseReleased()
    {
        return Predicates.create(
            t -> t.getID() == MouseEvent.MOUSE_RELEASED, "mouseReleased");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * is a MOUSE_CLICKED event
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> mouseClicked()
    {
        return Predicates.create(
            t -> t.getID() == MouseEvent.MOUSE_CLICKED, "mouseClicked");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * is a MOUSE_MOVED event
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> mouseMoved()
    {
        return Predicates.create(
            t -> t.getID() == MouseEvent.MOUSE_MOVED, "mouseMoved");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * is a MOUSE_DRAGGED event
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> mouseDragged()
    {
        return Predicates.create(
            t -> t.getID() == MouseEvent.MOUSE_DRAGGED, "mouseDragged");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * is a MOUSE_WHEEL event
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> mouseWheel()
    {
        return Predicates.create(
            t -> t.getID() == MouseEvent.MOUSE_WHEEL, "mouseWheel");
    }

    /**
     * Returns a predicate that checks whether the given input event
     * was created while the SHIFT button was pressed
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> shiftDown()
    {
        return Predicates.create(
            t -> t.isShiftDown(), "shiftDown");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * was created while the ALT button was pressed
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> altDown()
    {
        return Predicates.create(
            t -> t.isAltDown(), "altDown");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * was created while the ALT_GRAPH button was pressed
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> altGraphDown()
    {
        return Predicates.create(
            t -> t.isAltGraphDown(), "altGraphDown");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * was created while the CONTROL button was pressed
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> controlDown()
    {
        return Predicates.create(
            t -> t.isControlDown(), "controlDown");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * was created while the META button was pressed
     * 
     * @return The predicate
     */
    static <T extends InputEvent> Predicate<T> metaDown()
    {
        return Predicates.create(
            t -> t.isMetaDown(), "metaDown");
    }

    /**
     * Returns a predicate that checks whether the given mouse event
     * is a popup trigger
     * 
     * @return The predicate
     */
    static <T extends MouseEvent> Predicate<T> popupTrigger()
    {
        return Predicates.create(
            t -> t.isPopupTrigger(), "popupTrigger");
    }
    
    /**
     * Returns a predicate that checks whether the given input event
     * was created while the respective mouse button was pressed
     * 
     * @param button The button
     * @return The predicate
     * @throws IllegalArgumentException If the given button is not 1, 2 or 3
     */
    static <T extends InputEvent> Predicate<T> buttonDown(int button)
    {
        return Predicates.create(
            t -> is(t, maskForButton(button)), "buttonDown("+button+")");
    }

    
    /**
     * Returns the input event mask for the specified mouse button. The 
     * button must be 1, 2 or 3
     * 
     * @param button The button
     * @return The input event mask
     * @throws IllegalArgumentException If the given button is not 1, 2 or 3
     */
    private static int maskForButton(int button)
    {
        switch (button)
        {
            case 1: return InputEvent.BUTTON1_DOWN_MASK;
            case 2: return InputEvent.BUTTON2_DOWN_MASK;
            case 3: return InputEvent.BUTTON3_DOWN_MASK;
            default:
        }
        throw new IllegalArgumentException(
            "Button must be 1, 2 or 3");
    }
    
    /**
     * Returns whether the specified flags are set in the extended modifiers
     * of the given event
     * 
     * @param e The event
     * @param flags The flags
     * @return Whether the flags are set
     */
    private static boolean is(InputEvent e, int flags)
    {
        return (e.getModifiersEx() & flags) == flags;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private InputEventPredicates()
    {
        // Private constructor to prevent instantiation
    }

}
