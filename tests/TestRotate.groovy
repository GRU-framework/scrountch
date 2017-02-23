


import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.GraphicString
import scrountch.geom.SFrame

import java.awt.*
/**
 *
 * @author bamade
 */
// Date: 02/04/2016

class TestRotate {
    public static void main(String[] args) {
        SFrame frame = new SFrame(500,500)
        GraphicString grString = new GraphicString("Hello World")
        Cell cell1 = frame.getCell(0)
        cell1.addToGraphics(grString)
        grString.centerRotation(45)

        Rectangle rect = new Rectangle(0,0, 90, 90)
        GraphicShape grShape = new GraphicShape(rect)
        grShape.centerRotation(45)
        cell1.addToGraphics(grShape)


        for(int ix = 0; ix < 5 ;ix++) {
            Thread.sleep(1000)
            grString.translation(10,10)
            grShape.translation(10, 10)
            cell1.forceRepaint()
        }

    }
}
