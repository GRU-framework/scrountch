package scrountch.geom

import java.awt.geom.Rectangle2D

/**
 * this class is to be used by <TT>Shape</TT> objects
 * when rotations are at stake.
 * @author bamade
 */
// Date: 05/11/2016

class Radial2D {
    final double x;
    final double y;
    final double radius;

    public Radial2D(double x, double y, double radius) {
        this.x = x
        this.y = y
        this.radius = radius
    }

    public Radial2D(Rectangle2D bounds) {
        this.x = bounds.getCenterX()
        this.y = bounds.getCenterY()
        this.radius= Math.hypot(bounds.width, bounds.height)/2 ;
    }

    double getX() {
        return x
    }

    double getY() {
        return y
    }

    double getRadius() {
        return radius
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Radial2D{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", radius=").append(radius);
        sb.append('}');
        return sb.toString();
    }
}
