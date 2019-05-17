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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import de.javagl.viewer.MouseControl;
import de.javagl.viewer.Viewer;

/**
 * A class for creating a {@link MouseControl} that is only a "link" between 
 * multiple viewer instances, applying the same mouse operations to all 
 * viewers.
 */
public class LinkedMouseControls
{
    /**
     * A handle that is returned by 
     * {@link LinkedMouseControls#connect(Collection)} and that may be used
     * to later {@link #disconnect()} the viewers.
     */
    public static class Handle
    {
        /**
         * The viewers
         */
        private final List<Viewer> viewers;
        
        /**
         * The controls
         */
        private final List<LinkedMouseControl> controls;
        
        /**
         * Creates a new instance
         * 
         * @param viewers The viewers
         * @param controls The controls
         */
        Handle(List<Viewer> viewers, List<LinkedMouseControl> controls)
        {
            this.viewers = viewers;
            this.controls = controls;
        }
        
        /**
         * Disconnect the viewers that have been connected by the call to
         * {@link LinkedMouseControls#connect(Collection)} that created
         * this handle.
         */
        public void disconnect()
        {
            for (int i = 0; i < viewers.size(); i++)
            {
                Viewer viewer = viewers.get(i);
                LinkedMouseControl control = controls.get(i);
                viewer.removeMouseListener(control);
                viewer.removeMouseMotionListener(control);
                viewer.removeMouseWheelListener(control);
            }
        }
    }
    
    /**
     * Connect the given viewer instances
     * 
     * @param viewers The viewers
     * @return The {@link Handle} that may be used to disconnect the viewers
     */
    public static Handle connect(Collection<? extends Viewer> viewers)
    {
        List<Viewer> allViewers = new ArrayList<Viewer>(viewers);
        List<LinkedMouseControl> allControls =
            new ArrayList<LinkedMouseControl>();
        for (int i = 0; i < allViewers.size(); i++)
        {
            LinkedMouseControl control = new LinkedMouseControl();
            allControls.add(control);

            Viewer viewer = allViewers.get(i);
            viewer.addMouseListener(control);
            viewer.addMouseMotionListener(control);
            viewer.addMouseWheelListener(control);
        }
        
        for (int i = 0; i < allViewers.size(); i++)
        {
            LinkedMouseControl control = allControls.get(i);
            for (int j = 0; j < allViewers.size(); j++)
            {
                if (j == i)
                {
                    continue;
                }
                Viewer otherViewer = allViewers.get(j);
                LinkedMouseControl otherControl = allControls.get(j);
                control.connect(otherViewer, otherControl);
            }
        }
        return new Handle(allViewers, allControls);
    }
    
    /**
     * Implementation of the mouse control that performs the linking.
     * All events will be dispatched to other components.
     */
    private static class LinkedMouseControl implements MouseControl
    {
        /**
         * The destination components to dispatch the events to
         */
        private final List<Component> destinations;

        /**
         * The other mouse controls of the link
         */
        private List<LinkedMouseControl> others;
        
        /**
         * Whether this mouse control is currently dispatching an event
         */
        private boolean dispatching;
        
        /**
         * Creates a new instance
         */
        LinkedMouseControl()
        {
            this.destinations = new ArrayList<Component>();
            this.others = new ArrayList<LinkedMouseControl>();
        }
        
        /**
         * Connect this control to the given destination with the given
         * control
         * 
         * @param destination The destination
         * @param other The other control
         */
        void connect(Component destination, LinkedMouseControl other)
        {
            destinations.add(destination);
            others.add(other);
        }
        
        @Override
        public void mouseMoved(MouseEvent e)
        {
            dispatch(e);
        }
        
        @Override
        public void mouseDragged(MouseEvent e)
        {
            dispatch(e);
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            dispatch(e);
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
            dispatch(e);
        }
        
        @Override
        public void mousePressed(MouseEvent e)
        {
            dispatch(e);
        }
        
        @Override
        public void mouseExited(MouseEvent e)
        {
            dispatch(e);
        }
        
        @Override
        public void mouseEntered(MouseEvent e)
        {
            dispatch(e);
        }
        
        @Override
        public void mouseClicked(MouseEvent e)
        {
            dispatch(e);
        }
        
        /**
         * Dispatch the given event to the destination component, if there
         * is currently no dispatching in progress on this or the other
         * linked mouse control
         * 
         * @param e The mouse event
         */
        private void dispatch(MouseEvent e)
        {
            if (dispatching)
            {
                return;
            }
            for (LinkedMouseControl other : others)
            {
                if (other.dispatching)
                {
                    return;
                }
            }
            dispatching = true;
            
            for (Component destination : destinations)
            {
                // By default, the SwingUtilities#convertMouseEvent method will 
                // convert the location of the event. For MouseWheelEvents and
                // pressed/released/clicked events, it is necessary to pretend 
                // that the event happened at the same location as the source 
                // (but in the destination component)
                MouseEvent convertedEvent = null;
                if (e instanceof MouseWheelEvent)
                {
                    MouseWheelEvent sourceWheelEvent = (MouseWheelEvent)e;
                    convertedEvent = new MouseWheelEvent(destination,
                        sourceWheelEvent.getID(),
                        sourceWheelEvent.getWhen(),
                        sourceWheelEvent.getModifiers() | 
                            sourceWheelEvent.getModifiersEx(),
                        e.getX(),e.getY(), 
                        sourceWheelEvent.getXOnScreen(),
                        sourceWheelEvent.getYOnScreen(),
                        sourceWheelEvent.getClickCount(),
                        sourceWheelEvent.isPopupTrigger(),
                        sourceWheelEvent.getScrollType(),
                        sourceWheelEvent.getScrollAmount(),
                        sourceWheelEvent.getWheelRotation());
                }
                else if (
                    e.getID() == MouseEvent.MOUSE_PRESSED ||
                    e.getID() == MouseEvent.MOUSE_RELEASED ||
                    e.getID() == MouseEvent.MOUSE_CLICKED)
                {
                    convertedEvent = new MouseEvent(destination,
                        e.getID(),
                        e.getWhen(),
                        e.getModifiers() | e.getModifiersEx(),
                        e.getX(),e.getY(), 
                        e.getClickCount(),
                        e.isPopupTrigger());
                }
                else
                {
                    convertedEvent = SwingUtilities.convertMouseEvent(
                        e.getComponent(), e, destination);
                }
                
                //System.out.println("Dispatch "+e+" to "+destination);
                //System.out.println("as       "+convertedEvent);
                
                destination.dispatchEvent(convertedEvent);
            }
            dispatching = false;
        }
    }    
}
