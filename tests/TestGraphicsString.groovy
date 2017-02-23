


import scrountch.geom.Cell
import scrountch.geom.GraphicString
import scrountch.geom.SFrame
/**
 *
 * @author bamade
 */
// Date: 29/03/2016

class TestGraphicsString {
    public static void main(String[] args) {
        SFrame frame = new SFrame(100,150)
        Cell cell1 = frame.getCell(0)
        GraphicString gr1 = new GraphicString("Hello World")
        cell1.addToGraphics(gr1)
    }
}
