package scrountch.geom

/**
 * TODO: real curved shape when points are far away from each other
 * to be implemented (complex)
 * @author bamade
 */
// Date: 21/04/2016

class CurvedCurve extends DotCurve {

    CurvedCurve(double minX, double maxX, double minY, double maxY, double increment, double scale, Closure function) {
        super(minX, maxX, minY, maxY, increment, scale, function)
    }

    public void instanceInit() {
    }

    /**
     * beware instance method invoked from super ctor!
     * no instance init performed
     * use subInit to initialize instance data
     * and invoke it the first time drawAction is invoked
     * @param coorX
     * @param coorY
     */
    public void drawAction(double coorX, double coorY) {

    }
}
