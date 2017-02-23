

import scrountch.Fab
import scrountch.geom.*

import java.awt.*

/**
 *
 * @author bamade
 */
// Date: 05/04/2016

class TestRotations {
    public static void main(String[] args) {
        SFrame frame = new SFrame(400,400,3)
        Cell cell1 = frame.getCell(0)
        Cell cell2 = frame.getCell(1)
        Cell cell3 = frame.getCell(2)
        Rectangle rect = new Rectangle(100,100, 50, 100)
        GraphicShape grShape = new GraphicShape(rect,0,0)
        cell1.addToGraphics(grShape)
        GraphicString grString = new GraphicString("Hello World", 100, 100 )
        cell2.addToGraphics(grString)
        GraphicImage graphicImage = Fab.createGraphicImageFromFile("heart.png",100,100, 20 )
        cell3.addToGraphics(graphicImage)

        for(int ix = 0 ; ix < 4 ; ix++) {
            Thread.sleep(1500)
            grShape.centerRotation(45)
            grString.centerRotation(45)
            graphicImage.centerRotation(45)
            cell1.forceRepaint()
            cell2.forceRepaint()
            cell3.forceRepaint()
        }
        Thread.sleep(1500)
        grShape.clearRotation()
        grString.clearRotation()
        graphicImage.clearRotation()

        for(int ix = 0 ; ix < 4 ; ix++) {
            Thread.sleep(1500)
            grShape.cornerRotation(45)
            grString.cornerRotation(45)
            graphicImage.cornerRotation(45)
            cell1.forceRepaint()
            cell2.forceRepaint()
            cell3.forceRepaint()
        }


    }
}
