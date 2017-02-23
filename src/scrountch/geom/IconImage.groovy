package scrountch.geom

import javax.swing.*
import java.awt.*
import java.awt.image.BufferedImage

/**
 * AN Image which has been loaded from a File or an URL.
 * Note : should be extended to support sophisticated handling of images (java.awt.iconImage)
 * @author bear amade
 */
// Date: 23/03/2016

class IconImage extends ImageIcon  {
   // class IconImage extends ImageIcon implements I18NMethodNaming {

    /**
     * loads an Image from a file
     * @param fileName
     */
    public IconImage(String fileName) {
        super(fileName)
    }

    /**
     * loads an Image form an URL. loading from https may not work!
     * @param url
     */
    public IconImage(URL url) {
        super(url)
    }

    public IconImage(BufferedImage bufrImage){
        super(bufrImage)
    }

    /**
     * @return the Image to be displayed
     */
    public Image getImage() {
        // change to allow sophisticated subclasses of Image
        return super.getImage()
    }

}
