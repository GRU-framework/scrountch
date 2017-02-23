import scrountch.geom.Cell
import scrountch.geom.GraphicString
import scrountch.geom.SFrame

import java.awt.*

import static java.awt.Color.BLUE

/**
 *
 * @author bamade
 */
// Date: 12/05/2016
SFrame frame = new SFrame(250,100, 3)
Font font = new Font("Monospaced", Font.BOLD, 34)
GraphicString gr1 = new GraphicString("Wel§£ jetÿm", font)
gr1.fillPaint= Color.CYAN
GraphicString gr2 = new GraphicString("Wel§£ jetÿm", font, BLUE)
gr2.fillPaint= Color.WHITE
GraphicString gr3 = new GraphicString("Wel§£ jetÿm", 10, 0 ,font, BLUE)
gr3.fillPaint= Color.WHITE
gr3.stroke = new BasicStroke(3)
Cell cell1 = frame.getCell(0)
Cell cell2 = frame.getCell(1)
Cell cell3 = frame.getCell(2)
cell1.addToGraphics(gr1)
cell2.addToGraphics(gr2)
cell3.addToGraphics(gr3)

