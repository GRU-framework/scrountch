import scrountch.Fab
import scrountch.geom.*

import java.awt.image.BufferedImageFilter

/**
 *
 * @author bamade
 */
// Date: 24/04/2016

class TestImages1 {
    public static void main(String[] args) {
        IconImage icon = new IconImage("sousaphone.jpg")
        GraphicImage grimage1 = new GraphicImage(icon,0,0,30)
        int height = grimage1.getOriginalHeight()
        int width = grimage1.getOriginalWidth()
        println "$height $width"

        SFrame frame = new SFrame("sousa",  width, height,2,2)
        Cell cell1= frame.getCell(0,0)
        Cell cell2= frame.getCell(1,0)
        Cell cell3= frame.getCell(0,1)
        Cell cell4= frame.getCell(1,1)
        GraphicImage grimage2 = new GraphicImage(icon,0,0,30)
        grimage2.imageHandler = {img-> ImageUtils.grey( img, "greyTest")}
        GraphicImage grimage3 = new GraphicImage(icon,0,0,30)
        //grimage3.imageHandler = {img-> ImageUtils.negative(img, "negTest")}
        grimage3.imageHandler = {img-> ImageUtils.sepia(img, "sepiaTest")}
        GraphicImage grimage4 = new GraphicImage(icon,0,0,30)
        grimage4.imageHandler = {img-> ImageUtils.applyFilter( img, "invert", new BufferedImageFilter(ImageUtils.INVERT))}

        cell1.addToGraphics(grimage1)
        cell2.addToGraphics(grimage2)
        cell3.addToGraphics(grimage3)
        cell4.addToGraphics(grimage4)

        for(int ix =0 ; ix < 5 ;ix++) {
            Fab.pause(300)
            cell1.forceRepaint()
            cell2.forceRepaint()
            cell3.forceRepaint()
            cell4.forceRepaint()
            println('repaint')
        }
    }
}
