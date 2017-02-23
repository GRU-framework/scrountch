

import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.GraphicString
import scrountch.geom.SFrame

import java.awt.*

import static java.awt.Color.BLUE

/**
 *
 * @author bamade
 */
// Date: 29/03/2016

class TestGraphicsObjects {
    public static void main(String[] args) {
        SFrame frame = new SFrame(100,150, 5)
        Cell cell1 = frame.getCell(0)
        GraphicString gr1 = new GraphicString("Hello World")
        cell1.addToGraphics(gr1)

        Cell cell2 = frame.getCell(1)
        GraphicString gr2 = new GraphicString("Hello World", BLUE)
        cell2.addToGraphics(gr2)

        Rectangle rect = new Rectangle(5,5, 90, 90)
        GraphicShape grShape = new GraphicShape(rect)
        Cell cell3 = frame.getCell(2)
        cell3.addToGraphics(grShape)

        Cell cell4 = frame.getCell(3)
        GraphicShape grShape2 = new GraphicShape(rect, null, BLUE)
        cell4.addToGraphics(grShape2)


    }
}
