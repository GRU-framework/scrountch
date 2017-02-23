package scrountch.geom

import java.awt.geom.Rectangle2D
import java.util.function.DoubleUnaryOperator

/**
 * @author bamade
 */
// Date: 21/04/2016

class SimpleHistogram extends DotCurve {

    SimpleHistogram(double minX, double maxX, double minY, double maxY, double increment, double scale, Closure function) {
        super(minX, maxX, minY, maxY, increment, scale, function)
    }

    SimpleHistogram(double minX, double maxX, double minY, double maxY, double increment, double scale, DoubleUnaryOperator function) {
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
    public void drawAction(int step, double coorX, double coorY) {
        double width = incrementFunc(step, coorX) *scale
        Rectangle2D rect = null
        if(coorY < 0) {
             rect = new Rectangle2D.Double(1+ (coorX - (width/2)),
                     coorY, width -1, -coorY)
            append(rect,false)
        } else {
            rect = new Rectangle2D.Double(1+ (coorX - (width/2)),
                    0, width -1, coorY)
            append(rect,false)
        }
    }
}
