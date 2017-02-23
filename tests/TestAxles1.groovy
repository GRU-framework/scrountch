import scrountch.Fab
import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.SFrame

import java.awt.*

/**
 *
 * @author bamade
 */
// Date: 25/04/2016

class TestAxles1 {
    public static void main(String[] args) {
        int width=300
        int height= 200
        SFrame frame = new SFrame("axle", width, height,3)
        Cell cell = frame.getCell(0)
        Shape horiz = Fab.horizontalAxle(-50,250,1)
        println horiz.bounds2D
        Shape vertical = Fab.verticalAxle(-50,150,1)
        println vertical.bounds2D
        Shape axles = Fab.combine(horiz, vertical)
        GraphicShape horShape = new GraphicShape(axles, Fab.forceCenter(), Fab.forceCenter())
        cell.addToGraphics(horShape)
        Cell cell2 = frame.getCell(1)
        Shape axlesTick = Fab.axlesTicks(-50,250,-50, 150,1,20)
        println axlesTick.bounds2D
        GraphicShape shape2 = new GraphicShape(axlesTick, Fab.forceCenter(), Fab.forceCenter())
        cell2.addToGraphics(shape2)
        Cell cell3 = frame.getCell(2)
        Shape axlesTick2 = Fab.axlesTicks(-50,250,-50, 150,1,20,4)
        GraphicShape shape3 = new GraphicShape(axlesTick2, Fab.forceCenter(), Fab.forceCenter())
        cell3.addToGraphics(shape3)
    }
}
