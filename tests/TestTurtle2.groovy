import scrountch.Fab
import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.SFrame
import scrountch.geom.Turtle

import java.awt.*

/**
 *
 * @author bamade
 */
// Date: 31/03/2016

class TestTurtle2 {
    public static void main(String[] args) {
        SFrame frame = new SFrame(500,300,3)
        Turtle turtle = new Turtle()
        turtle.turnLeft(180)
        turtle.forward(100)
        turtle.turnLeft(90)
        turtle.forward(50)
        turtle.turnLeft(90)
        turtle.forward(150)
        GraphicShape shape = new GraphicShape(turtle,
                Color.RED,null,new BasicStroke(4))
        Cell cell = frame.getCell(1)
        Turtle turtle2 = new Turtle()
        turtle2.forward(100)
        turtle2.turnRight(90)
        turtle2.forward(50)
        turtle2.turnRight(90)
        turtle2.forward(100)
        turtle2.turnRight(90)
        turtle2.forward(50)
        GraphicShape shape2 = new GraphicShape(turtle2,
                Color.RED,null,new BasicStroke(4))
        Cell cell0 = frame.getCell(0)
        cell0.addToGraphics(shape2)
        cell.addToGraphics(shape)
        Turtle turtle3 = new Turtle()
        turtle3.forward(50)
        turtle3.turnRight(90)
        turtle3.forward(50)
        turtle3.turnRight(90)
        turtle3.forward(100)
        turtle3.turnRight(90)
        turtle3.forward(50)
        turtle3.turnRight(90)
        turtle3.forward(50)
        GraphicShape shape3 = new GraphicShape(turtle3,Fab.forceCenter(), Fab.forceCenter(),
                Color.RED,null,new BasicStroke(4))
        Cell cell2 = frame.getCell(2)
        cell2.addToGraphics(shape3)

    }
}
