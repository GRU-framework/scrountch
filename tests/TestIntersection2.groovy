import scrountch.geom.Cell
import scrountch.geom.SFrame

import java.awt.geom.Area
import java.awt.geom.Ellipse2D

SFrame frame = new SFrame(150,150)
Cell cell = frame.getCell()

Ellipse2D.Double ellipse = new Ellipse2D.Double(1,1, 30, 30)
Ellipse2D.Double ellipse2 = new Ellipse2D.Double(-1,-1, 30, 30)
Area area1 = new Area(ellipse)
Area area2 = new Area(ellipse2)

Area test1 = area1.clone()
Area test2 = area2.clone()

test1.intersect(cell.upperBorder)
test2.intersect(cell.upperBorder)
println (" should be true " + (test1.isEmpty()))
println (" should be false " + (test2.isEmpty()))

println (" should be false " + cell.borderCollision(cell.upperBorder, ellipse))
println (" should be true " + cell.borderCollision(cell.upperBorder, ellipse2))
