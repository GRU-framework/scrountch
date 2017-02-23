package scrountch.geom

import java.awt.Image
import java.awt.image.ImageObserver

/**
 * static utilities for geom package
 * @author bear amade
 */
// Date: 24/03/2016

class Commons {


    static ImageObserver observer = new ImageObserver() {
        @Override
        boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return true
        }
    }
}
