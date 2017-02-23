package scrountch.geom

import java.util.function.DoubleUnaryOperator

/**
 *
 * @author bamade
 */
// Date: 21/04/2016

class LineCurve extends DotCurve {

    public LineCurve(double minX, double maxX, double minY, double maxY, Closure  incrementFunc, double scale, Closure
            function) {
        super(minX,maxX,minY,maxY,incrementFunc,scale,function,null)
    }
    public LineCurve(double minX, double maxX, double minY, double maxY, Closure  incrementFunc, double scale, Closure function, Closure drawFunc) {
        super(minX, maxX, minY, maxY,incrementFunc, scale, function, drawFunc,null)
    }
    public LineCurve(double minX, double maxX, double minY, double maxY, double increment, double scale, Closure
            function) {
        // intial dot
        super(minX,maxX,minY,maxY,increment,scale,function)
    }

    public LineCurve(double minX, double maxX, double minY, double maxY, double increment, double scale, DoubleUnaryOperator
            function) {
        // intial dot
        super(minX,maxX,minY,maxY,increment,scale,function)
    }

    public LineCurve(double minX, double maxX, double minY, double maxY, double increment, double scale, Closure function, Closure drawFunction) {
        super(minX, maxX, minY, maxY, increment, scale, function, drawFunction, null)
    }
    public void drawAction(int step, double coorX, double coorY) {
        //println "$lastX $lastY + $coorX $coorY"
        lineTo(coorX, coorY)
        //append(new Line2D.Double(lastX, lastY, coorX,coorY) , false)
   }
}
