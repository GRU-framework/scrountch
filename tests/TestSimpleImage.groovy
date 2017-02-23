


import scrountch.Fab
import scrountch.geom.Cell
import scrountch.geom.GraphicImage
import scrountch.geom.SFrame
/**
 *
 * @author bamade
 */
// Date: 05/04/2016

class TestSimpleImage {
    public static void main(String[] args) {
        SFrame frame = new SFrame(400, 400)
        Cell cell1 = frame.getCell(0)
        println System.getProperty("user.dir")
        GraphicImage graphicImage = Fab.createGraphicImageFromFile("heart.png", 20)
        println graphicImage.iconImage.image
        cell1.addToGraphics(graphicImage)

    }
}
