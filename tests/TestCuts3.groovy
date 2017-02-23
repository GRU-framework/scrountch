import scrountch.geom.*

import java.awt.*
import java.awt.geom.Rectangle2D

/**
 *
 * @author bamade
 */
// Date: 10/05/2016

    IconImage icon = new IconImage("sousaphone.jpg")
    GraphicImage grimage1 = new GraphicImage(icon,0,0,50)
    int height = grimage1.getOriginalHeight()
    int width = grimage1.getOriginalWidth()
    println "$height $width"

    SFrame frame = new SFrame("sousa",  width, height,2)
    Cell cell1= frame.getCell(0)
    Cell cell2= frame.getCell(1)
    cell1.addToGraphics(grimage1)
cell1.onSelection({ Rectangle2D rect ->
    Image cut = ImageUtils.imageCapture(cell1, rect)
    Image pixou = ImageUtils.pixelate(cut,5)
    IconImage img = new IconImage(pixou)
    GraphicImage graphicImage = new GraphicImage(img, rect.x, rect.y)
    cell2.addToGraphics(graphicImage)

})