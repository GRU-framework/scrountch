

import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.GraphicString
import scrountch.geom.SFrame

import java.awt.Rectangle

import static java.awt.Color.BLUE

/**
 *
 * @author bamade
 */
// Date: 02/04/2016

class TestTransform {
    public static void main(String[] args) {
        SFrame frame = new SFrame(300,300)
        GraphicString gr2 = new GraphicString("Hello World", BLUE)
        Cell cell1 = frame.getCell(0)
        cell1.addToGraphics(gr2)
        Rectangle rect = new Rectangle(5,5, 90, 90)
        GraphicShape grShape = new GraphicShape(rect,0,0)
        cell1.addToGraphics(grShape)
        for(int ix = 0; ix < 5 ;ix++) {
            Thread.sleep(300)
            grShape.translation(10, 20)
            cell1.forceRepaint()
            println (grShape.getBoundsBeforeRotation(cell1))
        }
    }
}
