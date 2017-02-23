import scrountch.geom.Cell
import scrountch.geom.CompositeImage
import scrountch.geom.GraphicImage
import scrountch.geom.IconImage
import scrountch.geom.ImageUtils
import scrountch.geom.SFrame

import javax.swing.ImageIcon
import java.awt.Image
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

/**
 *
 * @author bamade
 */
// Date: 15/05/2016
IconImage icon = new IconImage("basket.jpg")
BufferedImage img = ImageUtils.sepia(icon.image) as BufferedImage
CompositeImage compositeImage = new CompositeImage(img)
GraphicImage grimage1 = new GraphicImage(compositeImage)
int height = grimage1.getOriginalHeight()
int width = grimage1.getOriginalWidth()
println "$height $width"

SFrame frame = new SFrame("basket",  width, height)
Cell cell1= frame.getCell(0)
cell1.addToGraphics(grimage1)
cell1.onSelection({ Rectangle2D rect ->
    Image cut = ImageUtils.imageCapture(cell1, rect)
    Image pixou = ImageUtils.pixelate(cut,5)
    compositeImage.superposeIcon((int)rect.x, (int) rect.y, new ImageIcon(pixou))
    cell1.forceRepaint()

})
 
