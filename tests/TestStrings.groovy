

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

class TestStrings {
    public static void main(String[] args) {
        SFrame frame = new SFrame(100,150, 5)
        GraphicString gr1 = new GraphicString("Hello World")
        GraphicString gr2 = new GraphicString("Hello World", BLUE)
        Cell cell1 = frame.getCell(0)
        Cell cell2 = frame.getCell(1)
        Cell cell3 = frame.getCell(2)
        cell1.addToGraphics(gr1)
        cell2.addToGraphics(gr2)
        cell3.addToGraphics(gr1)
        Rectangle rect = new Rectangle(5,5, 90, 90)
        GraphicShape grShape = new GraphicShape(rect,0,0)
        cell3.addToGraphics(grShape)
        GraphicShape grShape2 = new GraphicShape(rect, null, BLUE)
        Cell cell4 = frame.getCell(3)
        cell4.addToGraphics(grShape2)
        Cell cell5 = frame.getCell(4)
        cell5.addToGraphics(grShape)

    }
}
