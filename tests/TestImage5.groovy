/**
 *
 * @author bamade
 */
// Date: 01/05/2016

/**
 *
 * @author bamade
 */
// Date: 01/05/2016
/**
 *
 * @author bamade
 */
// Date: 30/04/2016
/**
 *
 * @author bamade
 */
// Date: 30/04/2016

import scrountch.Fab
import scrountch.geom.*

import java.awt.*
import java.awt.image.BufferedImageFilter

/**
 *
 * @author bamade
 */
// Date: 24/04/2016

class TestImages5 {
    public static void main(String[] args) {
        IconImage icon = new IconImage("sousaphone.jpg")
        GraphicImage grimage1 = new GraphicImage(icon,0,0,30)
        int height = grimage1.getOriginalHeight()
        int width = grimage1.getOriginalWidth()
        println "$height $width"

        SFrame frame = new SFrame("sousa",  width, height,3,1)
        Cell cell1= frame.getCell(0,0)
        Cell cell2= frame.getCell(1,0)
        Cell cell3= frame.getCell(2,0)
        GraphicImage grimage2 = new GraphicImage(icon,0,0,30)
        grimage2.imageHandler = {img->
            Image img2 = ImageUtils.applyFilter(img, "EDGE 5x5",new BufferedImageFilter(ImageUtils.EDGE5_5))
            ImageUtils.applyFilter( img2, "invert", new BufferedImageFilter(ImageUtils.INVERT))}
        GraphicImage grimage3= new GraphicImage(icon,0,0,30)
        grimage3.imageHandler = {img-> ImageUtils.applyFilter( img, "POSTER", new BufferedImageFilter(ImageUtils.POSTER))}

        cell1.addToGraphics(grimage1)
        cell2.addToGraphics(grimage2)
        cell3.addToGraphics(grimage3)

        for(int ix =0 ; ix < 5 ;ix++) {
            Fab.pause(1000)
            cell1.forceRepaint()
            cell2.forceRepaint()
            cell3.forceRepaint()
            println('repaint')
        }
    }
}





