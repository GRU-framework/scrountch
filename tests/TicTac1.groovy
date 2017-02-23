import scrountch.geom.Cell
import scrountch.geom.GraphicString
import scrountch.geom.SFrame

import java.awt.*
/**
 *
 *
 * @author bamade
 */
// Date: 18/05/2016
Font font = new Font("Monospaced", Font.BOLD , 50)
GraphicString gr1 = new GraphicString("X", font, Color.RED)
gr1.stroke = new BasicStroke(4)
gr1.fillPaint= new GradientPaint(0, 0, Color.yellow, 0, 45, Color.red)
GraphicString gr2 = new GraphicString("O", font, Color.BLUE)
gr2.fillPaint= new GradientPaint(0, 0, Color.WHITE, 0, 45, Color.BLUE)
gr2.stroke = new BasicStroke(4)
SFrame frame = new SFrame(50,50, 3,3)
Cell center = frame.getCell(1,1)
center.addToGraphics(gr1)
Cell angle = frame.getCell(0,2)
angle.addToGraphics(gr2)
angle = frame.getCell(1,2)
angle.addToGraphics(gr1)
angle = frame.getCell(1,0)
angle.addToGraphics(gr2)
angle = frame.getCell(0,1)
angle.addToGraphics(gr1)

