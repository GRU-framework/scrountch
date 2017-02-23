import scrountch.Fab
import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.SFrame

import java.awt.*
import java.awt.geom.Ellipse2D
/**
 *
 * @author bamade
 */
// Date: 13/04/2016

SFrame frame = new SFrame("balle", 300, 300 )
Color[] colors = [Color.WHITE, Color.RED]
float[] ratios = [0.0, 1.0]
int dim = 150
int radius = dim/2
RadialGradientPaint paint = new RadialGradientPaint(radius, radius, radius,ratios,colors)
Ellipse2D ellipse = Fab.createEllipse( dim, dim)
GraphicShape gShape = new GraphicShape(ellipse, Cell.TRANSPARENT, paint)
//GraphicShape gShape = new GraphicShape(ellipse)
Cell cell = frame.getCell()
cell.addToGraphics(gShape)
/*
cell.forceRepaint()
for(int ix = 0 ; ix < 5; ix++) {
    Fab.pause(300)
    gShape.translation(10,10)
    cell.forceRepaint()
}
*/


 
