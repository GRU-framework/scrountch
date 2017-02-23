import scrountch.Fab
import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.SFrame

import java.awt.geom.Rectangle2D
/**
 *
 * @author bamade
 */
// Date: 29/03/2016

class TestGraphicsShapes1 {
    public static void main(String[] args) {
        SFrame frame = new SFrame(200,100, 4)
        Cell cell1 = frame.getCell(0)
        Rectangle2D.Double rectangle = Fab.createRectangle(60,40)
        GraphicShape gr1 = new GraphicShape(rectangle)
        cell1.addToGraphics(gr1)

        Cell cell2 = frame.getCell(1)
        GraphicShape gr2 = new GraphicShape(rectangle, 5, 5 )
        cell2.addToGraphics(gr2)

        GraphicShape grShape = new GraphicShape(rectangle, Fab.keep(), Fab.keep())
        Cell cell3 = frame.getCell(2)
        cell3.addToGraphics(grShape)

        Cell cell4 = frame.getCell(3)
        GraphicShape grShape2 = new GraphicShape(rectangle, 5, Fab.keep() )
        cell4.addToGraphics(grShape2)

        Rectangle2D.Double rect2 =  new Rectangle2D.Double(10,10, 60,40)
        GraphicShape gr1_1 = new GraphicShape(rect2)
        cell1.addToGraphics(gr1_1)
        GraphicShape gr2_1 = new GraphicShape(rect2,5 ,5)
        cell2.addToGraphics(gr2_1)
        GraphicShape gr3_1 = new GraphicShape(rect2,Fab.keep() ,Fab.keep())
        cell3.addToGraphics(gr3_1)
        GraphicShape gr4_1 = new GraphicShape(rect2,5 ,Fab.keep())
        cell4.addToGraphics(gr4_1)

    }
}
