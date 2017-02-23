

import scrountch.geom.Cell
import scrountch.geom.GraphicShape
import scrountch.geom.SFrame
import scrountch.geom.Turtle

/**
 *
 * @author bamade
 */
// Date: 31/03/2016

class TestTurtle {
    public static void main(String[] args) {

        SFrame frame = new SFrame(300,300 )
        Turtle turtle = new Turtle(150,150)
        turtle.turnLeft(45)
        turtle.forward(50)
        turtle.turnLeft(45)
        turtle.forward(50)
        turtle.turnRight(135)
        turtle.forward(50)
        turtle.turnRight(90)
        turtle.quadForward(50,20)
        turtle.quadForward(50,-20)

        GraphicShape shape = new GraphicShape(turtle,0,0)
        Cell cell = frame.getCell()
        cell.addToGraphics(shape)
    }
}
