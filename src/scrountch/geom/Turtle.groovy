package scrountch.geom

import java.awt.geom.Path2D
import java.awt.geom.Point2D

/**
 * a Shape that can be drawn (references the Logo "turtle").
 * Simple movements specification can move the "turtle" and leave a trail (except when it jumps)
 * @author bear amade
 */
// Date: 30/03/2016

class Turtle extends Path2D.Double {
    //class Turtle extends Path2D.Double implements I18NMethodNaming{

    /**
     * angle in degrees to the right of the direction of the Turtle
     */
    double angle = 0

    /**
     * creates a Turtle at coordinates 0,0 facing the X axis
     */
    public Turtle() {
        this(0,0,0)
    }

    /**
     * creates a Turtle at coordinates x,y facing the X axis
     */
    public Turtle(int x, int y){
        this(x,y,0)
    }

    /**
     * creates a Turtle at coordinates x,y . A positive angle is clockwise
     * from the X axis (as if the turtle has turned angle to the right).
     * A negative angle is counterClockWise.
     */
    public Turtle(int x, int y, double angle){
        this.angle = angle
        moveTo(x, y)
    }

    /**
     * protected method
     * @param angle
     */
    protected void rotate(double angle) {
        this.angle += angle
        this.angle = this.angle %360
    }

    /**
     *  the turtle rotates to the right
     * @param angle a positive value in degrees
     */
    public void turnRight(double angle) {
        rotate(angle)
    }

    /**
     *  the turtle rotates to the left
     * @param angle a positive value in degrees
     */
    public void turnLeft(double angle) {
        rotate(-angle)
    }

    /**
     * advance the turtle to step number of points
     * @param step a positive value (number of points)
     */
    public void forward(int step) {
        Point2D point = this.getCurrentPoint()
        double coorX = point.x
        double coorY = point.y
        coorX += step * Math.cos(Math.toRadians(angle));
        coorY += step * Math.sin(Math.toRadians(angle));
        lineTo(coorX, coorY)
    }
    // what to do with negatives points?
    // quad create a cuve with a +- value for quad
    // negative is to the left, positive to the right

    /**
     * advance the turtle to "step" number of points
     * but the path is curved with the middle of the curve at the "gap" distance from the direct path
     * @param step
     * @param gap if positive the middle of the curve is at this distance (in points) to the right of the direct path
     * is negative to the left of the direct path.
     */
    public void quadForward(int step, int gap) {
        Point2D point = this.getCurrentPoint()
        double coorX = point.x
        double coorY = point.y
        // all this is stupid the trigonometry should be simpler!
        double angleRatioX = Math.cos(Math.toRadians(angle))
        double angleRatioY = Math.sin(Math.toRadians(angle))
        double xMiddle = coorX + (step/2D)* angleRatioX
        double yMiddle = coorY + (step/2D)* angleRatioY
        double quadX = 0
        double quadY = 0
        if(gap > 0)  {
            quadX = xMiddle + (gap * Math.cos(Math.toRadians(angle + 90)))
            quadY = yMiddle  + (gap * Math.sin(Math.toRadians(angle + 90)))
        } else {
            gap = -gap
            quadX = xMiddle + (gap * Math.cos(Math.toRadians(angle - 90)))
            quadY = yMiddle  + ( gap * Math.sin(Math.toRadians(angle - 90)))
        }

        coorX += step * angleRatioX;
        coorY += step * angleRatioY
        quadTo(quadX, quadY, coorX, coorY)
    }

    // beziers create any curve between 2 points with distance, height from the initial
    // distance cn't be negative, heigth + right - left

    /**
     * the turtle "jumps" to position x, y
     * @param x
     * @param y
     */
    public void jumpTo(int x, int y) {
        moveTo(x, y)
    }
}
