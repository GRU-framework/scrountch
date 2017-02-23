package scrountch.geom

import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D

/**
 * internal class. to be used by scrountch code only.
 *
 * @author bear amade
 */
// Date: 05/04/2016

class Rotation {
    enum Kind { ORIGIN, CENTER}

    Kind kind;
    double angle ;

    /**
     * creates a rotation description
     * @param kind
     * @param initialAngle in degrees, clockwise
     */
    public Rotation(Kind kind, double initialAngle) {
        this.kind = kind;
        this.angle = initialAngle
    }

    /**
     * adds an angle to rotation
     * @param angle
     */
    /*
    public Rotation plus(int angle) {
        this.angle = (this.angle + angle) % 360
        return this
    }
    */

    public String toString() {
        return "rotation: $kind -> $angle"
    }

    /**
     * creates an AffineTransform for a given Bounds object based on this Instance specification
     * @param rectangle
     * @return
     */
    AffineTransform transformIt(Rectangle2D rectangle) {
        AffineTransform res = null
        switch(kind) {
            case Kind.ORIGIN :
                 return AffineTransform.getRotateInstance(Math.toRadians(angle), rectangle.x, rectangle.y)
                break
            case Kind.CENTER :
                  double dx = rectangle.centerX
                //double dx = rectangle.x + (rectangle.width/2)
                  double dy = rectangle.centerY
                //double dy = rectangle.y + (rectangle.height/2)
                return AffineTransform.getRotateInstance(Math.toRadians(angle), dx, dy)
                break ;
        }
        return res ;
    }
}
