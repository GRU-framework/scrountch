package scrountch.utils

import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

/**
 * this is used to create a single Listener Object
 * that can be added or removed from a Component.
 * Implementation: for sure there is AWTEventMultiCaster!
 * @author bamade
 */
// Date: 29/04/2016

class SelectionListener {
    MouseListener mouseListener
    MouseMotionListener motionListener

    public SelectionListener(MouseListener mL, MouseMotionListener movL) {
        this.mouseListener = mL
        this.motionListener = movL
    }

}
