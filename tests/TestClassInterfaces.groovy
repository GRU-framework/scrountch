import java.awt.Rectangle
import java.awt.geom.Ellipse2D

/**
 *a bizarre feature of inner classes! do not know about
 * interfaces
 * @author bamade
 */
// Date: 25/04/2016

class TestClassInterfaces {
    public static void main(String[] args) {
        Ellipse2D.Double ellipse = new Ellipse2D.Double()
        Class aClass = ellipse.getClass()
        Class[] interfazes = aClass.getInterfaces()
        println interfazes
        Rectangle rect = new Rectangle()
        aClass = rect.getClass()
        interfazes = aClass.getInterfaces()
        println interfazes
    }
}
