import scrountch.geom.Cell
import scrountch.geom.SFrame

import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

SFrame frame = new SFrame(150,150)
Cell cell = frame.getCell()
Rectangle2D.Double rectangle = new Rectangle2D.Double(0,0,150,0.01)

Ellipse2D.Double ellipse = new Ellipse2D.Double(1,1, 30, 30)
Ellipse2D.Double ellipse2 = new Ellipse2D.Double(-1,-1, 30, 30)

println (" should be false " + ellipse.intersects(rectangle))
println (" should be true " + ellipse2.intersects(rectangle))

