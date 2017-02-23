package scrountch.geom

import java.awt.geom.Ellipse2D
import java.awt.geom.Path2D
import java.util.function.DoubleUnaryOperator

/**
 *
 * @author bamade
 */
// Date: 21/04/2016

class DotCurve extends Path2D.Double {
    double minX
    double maxX
    double minY
    double maxY
    Closure incrementFunc
    double scale
    Closure function
    Closure drawFunction
    Object[] additionalArgs = new Object[0]
    double lastX;
    double lastY;

    static boolean isMore(double a, double b, double scale)  {
        int aInt
        int bInt
        if (scale > 0)  {
            aInt = (int) (a *scale)
            bInt = (int) (b*scale)
        } else {
            aInt = (int) a
            bInt = (int) b
        }
        return aInt > bInt
    }

    static boolean isMoreOrEqual(double a, double b, double scale) {
        int aInt
        int bInt
        if (scale > 0)  {
            aInt = (int) (a *scale)
            bInt = (int) (b*scale)
        } else {
             aInt = (int) a
             bInt = (int) b
        }
        return aInt >= bInt
    }


    public DotCurve(double minX, double maxX, double minY, double maxY, Closure incrementFunc, double scale, Closure function, Closure drawFunction, Object... args) {
        if(args != null) {
           additionalArgs = args
        }
        instanceInit(additionalArgs)
        Class[] argsTypes = function.getParameterTypes()
        if((argsTypes.length != 1) && ! Double.TYPE.equals(argsTypes[0])) {
            throw new IllegalArgumentException("argument y= f(x) should be a Closure with just a double arg")
        }
        //TODO check for other Closure args
        // increment func: int, double
        // drawFunction Object, int, double, double
        this.minX = minX
        this.minY = minY
        this.maxX = maxX
        this.maxY = maxY
        this.incrementFunc = incrementFunc
        this.scale = scale
        this.function = function
        this.drawFunction = drawFunction
        this.additionalArgs = args
        // intial dot
        double coorY = java.lang.Double.MIN_VALUE+2
        double coorX = minX
        int step = 0
        while(true) {
            coorY = function(coorX)
            if(isMore(coorY , maxY, scale) || isMore(minY , coorY, scale)) {
                coorX += incrementFunc(step++, coorX)
               continue
            }
            if(isMore(coorX , maxX, scale)) return
            break
        }
        lastX = coorX *scale
        lastY = -(coorY *scale)
        moveTo(lastX, lastY)
        boolean doIt = true
        if(drawFunction != null) {
            doIt = drawFunction(this, step, lastX, lastY)
        }
        if(doIt) {
            drawAction(step, lastX, lastY)
        }
        step++
        //moveTo2(coorX * scale,-( coorY *scale))

        while(isMoreOrEqual(maxX, coorX, scale) ) {
        //while(coorX <= maxX ) {
            double incr = incrementFunc(step, coorX)
            coorX += incr
            if(isMore(coorX,  maxX, scale)) {
                break
            }
            coorY = function(coorX)
            if( isMore(coorY , maxY, scale) || isMore(minY,coorY,scale )) {
                step++
                continue
            }
            moveTo2(step++, coorX * scale, - (coorY *scale))
            if(incr == 0D ) break
        }
    }
    public DotCurve(double minX, double maxX, double minY, double maxY, double increment, double scale, Closure function, Closure drawFunction, Object... args) {
        this(minX, maxX, minY, maxY, {s,x->increment}, scale, function,drawFunction, args)
    }

    public DotCurve(double minX, double maxX, double minY, double maxY, double increment, double scale, Closure function, Closure drawFunction) {
        this(minX, maxX, minY, maxY, {s,x->increment}, scale, function,drawFunction, null)
    }

    public DotCurve(double minX, double maxX, double minY, double maxY, double increment, double scale, Closure function) {
        this(minX,maxX, minY,maxY,increment,scale,function,null)
    }

    public DotCurve(double minX, double maxX, double minY, double maxY, double increment, double scale,
                    DoubleUnaryOperator func) {
        this(minX,maxX, minY,maxY,increment,scale,{double x -> func.applyAsDouble(x)},null)

    }

    public void moveTo2(int step, double x, double y) {
        boolean doIt = true ;
        if(drawFunction != null) {
            doIt = drawFunction(this, step, x, y)
        }
        if(doIt) {
            drawAction(step, x, y)
        }
        lastX =x
        lastY = y
    }

    public void instanceInit(Object... args) {
    }

    /**
     * BEWARE: method invoked from constructor.
     * if overriden use method instanceInit() to initialize instance
     * @param coorX
     * @param coorY
     */
    public void drawAction(int step, double coorX, double coorY) {
        append(new Ellipse2D.Double(coorX,coorY, 1,1), false )
    }

    public String toString() {
        return super.toString()
    }
}
