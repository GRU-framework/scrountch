import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

/**
 * @author bamade
 */
// Date: 25/04/2016

public class TestClazzInterfaces {
    public static void main(String[] args) {
            Ellipse2D.Double ellipse = new Ellipse2D.Double();
            Class aClass = ellipse.getClass();
            Class[] interfazes = aClass.getInterfaces();
        System.out.println(Arrays.toString(interfazes));
            Rectangle rect = new Rectangle();
            aClass = rect.getClass();
            interfazes = aClass.getInterfaces();
        System.out.println(Arrays.toString(interfazes));
    }
}
