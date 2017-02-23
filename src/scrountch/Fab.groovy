package scrountch

import org.codehaus.groovy.tools.Compiler
import scrountch.geom.*

import javax.swing.*
import java.awt.*
import java.awt.geom.*
import java.util.logging.ConsoleHandler
import java.util.logging.Handler
import java.util.logging.Level

import static scrountch.GlobalCommons.LOG
import static scrountch.GlobalCommons.NOT_SET

/**
 * this is a class that defines functions for beginners.
 * Most are factories and all are subject to method name translation.
 * @author bear amade
 */
// Date: 26/03/2016

class Fab {
    private Fab() {}
    /**
     * internal method to translate static method names
     * @param name
     * @param args
     * @return
     */
    //static Class curclazz = Fab.class
    static volatile int  compteur = 0


    static $static_methodMissing(String name, args){
        Class curclazz = Fab.class
        String shortName = curclazz.getSimpleName()
        //println ("CLASS : $shortName $name")
        ResourceBundle mBundle = GlobalCommons.methBundle;
        if( mBundle != null) {
            String methodName = null
            try {
                methodName = mBundle.getString(shortName+'#'+name)
            } catch (Exception exc) {
                throw new MissingMethodException(name, curclazz, args)
            }
            // optimisation ??
            /* avoid << : creates lots of methods */
            /* does not WORK!
            Fab.metaClass.static."$methodName" =  { argz ->
                //int cpt = compteur
                //println "invoked $cpt $methodName"
                Fab.metaClass.invokeStaticMethod(curclazz,methodName, argz)}
            compteur++
            /**/
           curclazz.metaClass.invokeStaticMethod(Fab.class, methodName, args)
        } else {
            throw new MissingMethodException(name, curclazz, args)
        }
    }


    static Random randomizer = new Random();
    static Level defaultLevel = LOG.getLevel()
    static java.util.List<String> listLogConcerns = []
    static Map<Handler,Level> mapLevels = [:]

    static Compiler groovyCompiler = new Compiler(ShellGen.getCompilerConfiguration(null))

    //////////// STD FACTORIES

    /***
     * utility to compile classes
     * @param fileNames
     */
    static public void compileFiles(String[] fileNames){
        groovyCompiler.compile(fileNames)
    }

    static public void compileFiles(String fileName) {
        String[] args = [fileName]
        groovyCompiler.compile(args)
    }

    /**
     *
     * @param upperBound
     * @return a random int between zero and  upperBound (excluded)
     */
    static int randomInt(int upperBound) {
        return randomizer.nextInt(upperBound) ;
    }

    /**
     * utility that pops a Dialog to fetch a String
     * @param invite the message shown by the Dialog
     * @return the string typed bu the user
     */
    static String readln(String invite) {
        return javax.swing.JOptionPane.showInputDialog(null, invite, "?",
        JOptionPane.QUESTION_MESSAGE, GlobalCommons.scrountchIcon_small, null, null)
        //return javax.swing.JOptionPane.showInputDialog(invite)
    }

    /**
     *  makes the current thread sleep
     * @param delay in microseconds
     */
    static void pause(int delay) {
        Thread.sleep(delay)
    }

    /**
     *  a simplified logging substitute (to be used by beginners unaware of Logging features).
     *  Specifies a message for a specific topic (domain). The message will be delivered
     *  only if traces are enabled.
     * @param message
     * @param domain
     */
    static void trace(String message, String domain) {
        if(listLogConcerns.contains(domain)) {
            LOG.fine(message)
        }
    }

    /**
     *  enables traces for a given topic (domain) of for various topics.
     *  The traces are considered Level.FINE logs.
     * @param domains
     */
    static void enableTraces(String... domains) {
        if(domains.length == 0) {
            stopTraces()
        } else {
            LOG.setLevel(Level.FINE)
            Handler[] handlers = LOG.getHandlers()
            if(handlers.length == 0) {
                Handler defHandler = new ConsoleHandler()
                defHandler.setLevel(Level.FINE)
                mapLevels.put(defHandler, Level.FINE)
                LOG.addHandler(defHandler)
            }
            for(Handler handler: handlers){
                Level curLevel = handler.getLevel()
                mapLevels.put(handler, curLevel)
                if( curLevel > Level.FINE) {
                    handler.setLevel(Level.FINE)
                }
            }
            for(String dom: domains){
                listLogConcerns.add(dom)
            }
        }
    }

    /**
     * stops all tracing
     */
    static void stopTraces() {
        listLogConcerns.removeAll()
        LOG.setLevel(defaultLevel)
        for(Handler handler: LOG.getHandlers()){
            Level level = mapLevels.get(handler)
            if(level != null) {
                handler.setLevel(level)
            }
        }
    }

    /**
     * a utility that returns a constant to be used by other methods
     * @return
     */
    static double keep() {
        return GlobalCommons.KEEP
    }

    /**
     * a utility that returns a constant to be used by other methods
     * @return
     */
    static double forceCenter() {
        return GlobalCommons.FORCE_CENTER
    }

    /// AWT Factories

    static Polygon createPolygon(int[] xs, int[] ys) {
        if(xs.length != ys.length){
            throw new IllegalArgumentException("polygon x/y not same size")
        }
        return new Polygon(xs, ys, xs.length)
    }
    static GraphicShape createGraphicPolygon(int[] xs, int[] ys) {
        Polygon poly = createPolygon(xs, ys)
        return new GraphicShape(poly,0,0)
    }

    static Rectangle2D.Double  createRectangle( double width, double height) {
        return new Rectangle2D.Double(0,0,width,height)
    }

    static Rectangle2D.Double  createRectangle( double x, double y, double width, double height) {
        return new Rectangle2D.Double(x,y,width,height)
    }

    static  GraphicShape createGraphicRectangle(int x, int y, int width, int height) {
        Rectangle2D.Double rect = createRectangle( width,height)
        return new GraphicShape(rect,x, y)

    }
    static Ellipse2D.Double createEllipse( double width, double height) {
        return new Ellipse2D.Double(0,0, width, height)
    }
    static Ellipse2D.Double createEllipse( double x, double y ,double width, double height) {
        return new Ellipse2D.Double(x,y, width, height)
    }

    static GraphicShape createGraphicEllipse(int x, int y, int width, int height) {
        Ellipse2D.Double ellipse = createEllipse(width,height)
        return new GraphicShape(ellipse,x,y)
    }

    static Line2D.Double createLine( double x2, double y2) {
        return new Line2D.Double(0,0, x2,y2)
    }
    static Line2D.Double createLine(double x1, double y1, double x2, double y2) {
        return new Line2D.Double(x1,y1, x2,y2)
    }

    static GraphicShape createGraphicLine(int x1, int y1, int x2, int y2) {
        Line2D.Double line = createLine(x2,y2)
        return new GraphicShape(line,x1,y1)
    }

    /**
     * factory for <TT>BasicStroke</TT>
     */
    static Closure<BasicStroke> createStroke = BasicStroke.metaClass.&invokeConstructor as Closure<BasicStroke>

    /**
     * factory for <TT>Turtle</TT>
     */
    static Closure<Turtle> createTurtle = Turtle.metaClass.&invokeConstructor as Closure<Turtle>


    static IconImage createIconImageFromFile(String fileName) {
        return new IconImage(fileName)
    }

    static IconImage createIconImageFromURL(String URLname) {
       URL url = new URL(URLname)
        return new IconImage(url)
    }

    ///////// Factories for SFrame

    /**
     * factory for <TT>SFrame</TT>
     */
    static Closure<SFrame> createFrame = SFrame.metaClass.&invokeConstructor as Closure<SFrame>

    /////////// Factories for GraphicImage
    static GraphicImage createGraphicImageFromFile(String fileName, double coorx, double coory, int scale) {
       IconImage img = new IconImage(fileName)
        return new GraphicImage(img, coorx, coory, scale)
    }
    static GraphicImage createGraphicImageFromFile(String fileName, double coorx, double coory ) {
        return createGraphicImageFromFile(fileName, coorx, coory, 100)
    }

    static GraphicImage createGraphicImageFromFile(String fileName, int scale) {
        return createGraphicImageFromFile(fileName, NOT_SET, NOT_SET, scale)
    }

    static GraphicImage createGraphicImageFromFile(String fileName ) {
        return createGraphicImageFromFile(fileName, NOT_SET, NOT_SET, 100)
    }
    static GraphicImage createGraphicImageFromURL(String urlName, double coorx, double coory, int scale) {
        URL url = new URL(urlName)
        IconImage img = new IconImage(url)
        return new GraphicImage(img, coorx, coory, scale)
    }
    static GraphicImage createGraphicImageFromURL(String urlName, double coorx, double coory) {
        return createGraphicImageFromURL(urlName, coorx, coory, 100)
    }
    static GraphicImage createGraphicImageFromURL(String urlName,  int scale) {
        return createGraphicImageFromURL(urlName, NOT_SET, NOT_SET, scale)
    }
    static GraphicImage createGraphicImageFromURL(String urlName) {
        return createGraphicImageFromURL(urlName, NOT_SET, NOT_SET, 100)
    }

    static Closure<GraphicImage> createGraphicImage = GraphicImage.metaClass.&invokeConstructor as Closure<GraphicImage>

    // Factories for GraphicString

    /**
     * factory for <TT>GraphicString</TT>
     */
    static Closure<GraphicString> createGraphicString = GraphicString.metaClass.&invokeConstructor as Closure<GraphicString>

    // GRAPHIC SHAPE

    /**
     * factory for <TT>GraphicShape</TT>
     */
    static Closure<GraphicShape> createGraphicShape = GraphicShape.metaClass.&invokeConstructor as Closure<GraphicShape>

    static GraphicShape centeredGraphicShape(Shape shape, Color drawPaint, Paint fillPaint, Stroke stroke) {
        return new GraphicShape(shape, GlobalCommons.FORCE_CENTER, GlobalCommons.FORCE_CENTER, drawPaint, fillPaint, stroke)
    }

    static GraphicShape centeredGraphicShape(Shape shape) {
        return centeredGraphicShape(shape, null, null, null)
    }

    static GraphicShape centeredGraphicShape(Shape shape, Color drawPaint) {
        return centeredGraphicShape(shape, drawPaint, null, null)
    }

    static GraphicShape centeredGraphicShape(Shape shape, Color drawPaint, Paint fillPaint) {
        return centeredGraphicShape(shape, drawPaint, fillPaint, null)
    }

    static GraphicShape centeredGraphicShape(Shape shape, Color drawPaint,  Stroke stroke) {
        return centeredGraphicShape(shape, drawPaint, null, stroke)

    }

    static GraphicShape centeredGraphicShape(Shape shape,  Stroke stroke) {
        return centeredGraphicShape(shape, null, null, stroke)
    }

    // Factories for Button
    static SButton createButton(String ref, String label) {
        return new SButton(ref, label)
    }

    static Label createLabel(String message) {
        return new Label(message)
    }

    //static Path2D.Double createDotCurve = DotCurve.metaClass.&invokeConstructor as Path2D.Double
    static Closure<Path2D.Double> createDotCurve = DotCurve.metaClass.&invokeConstructor as Closure<Path2D.Double>

    //static Path2D.Double createLineCurve = LineCurve.metaClass.&invokeConstructor as Path2D.Double
    static Closure<Path2D.Double> createLineCurve = LineCurve.metaClass.&invokeConstructor as Closure<Path2D.Double>

    //static Path2D.Double createSimpleHistogram = SimpleHistogram.metaClass.&invokeConstructor as Path2D.Double
    static Closure<Path2D.Double> createSimpleHistogram = SimpleHistogram.metaClass.&invokeConstructor as Closure<Path2D.Double>

    static Path2D.Double horizontalAxle(double minX, double maxX,double scale) {
        // can be made simple but kept for other purposed
        //LineCurve line = new LineCurve(minX, maxX, 0,0, (maxX -minX), scale, {x->0})
        Path2D.Double line = new Path2D.Double()
        line.moveTo(minX*scale, 0)
        line.lineTo(maxX*scale, 0)
        return line
    }
    static Path2D.Double horizontalAxleTicks(double minX, double maxX,double scale, double ticksSpace) {
        Path2D.Double line = horizontalAxle(minX,maxX, scale)
        double realMax = maxX*scale
        for(double x =0 ; x <= realMax; x += (ticksSpace*scale)) {
            line.append(new Line2D.Double(x, -1, x, +1), false)
        }
        for(double x =0 ; x >= (minX*scale); x-= (ticksSpace*scale)){
            line.append(new Line2D.Double(x, -1, x, +1), false)
        }
        return line
    }
    static Path2D.Double horizontalAxleTicks(double minX, double maxX,double scale, double ticksSpace, int majorInterval) {

        Path2D.Double line = horizontalAxle(minX,maxX, scale)
        double realMax = maxX*scale
        int ct = 0
        for(double x =0 ; x <= realMax; x += (ticksSpace*scale)) {
            if((ct % majorInterval) == 0){
                line.append(new Line2D.Double(x, -3, x, +3), false)
            } else {
                line.append(new Line2D.Double(x, -1, x, +1), false)
            }
            ct++
        }
        ct = 0
        for(double x =0 ; x >= (minX*scale); x-= (ticksSpace*scale)){
            if((ct % majorInterval) == 0){
                line.append(new Line2D.Double(x, -3, x, +3), false)
            } else {
                line.append(new Line2D.Double(x, -1, x, +1), false)
            }
            ct++
        }
        return line
    }

    static Path2D.Double verticalAxle( double minY, double maxY, double scale) {
        Path2D.Double line =  horizontalAxle(minY, maxY, scale)
        AffineTransform transform = AffineTransform.getQuadrantRotateInstance(-1,0,0)
        return line.createTransformedShape(transform)
    }

    static Path2D.Double verticalAxleTicks( double minY, double maxY, double scale, double tickSpace) {
        Path2D.Double horiWithTicks = horizontalAxleTicks(minY,maxY, scale, tickSpace)
        AffineTransform transform = AffineTransform.getQuadrantRotateInstance(-1,0,0)
        return horiWithTicks.createTransformedShape(transform)
    }

    static Path2D.Double verticalAxleTicks( double minY, double maxY, double scale, double tickSpace, int majorInterval) {
        Path2D.Double horiz = horizontalAxleTicks(minY,maxY,scale, tickSpace, majorInterval)
        AffineTransform transform = AffineTransform.getQuadrantRotateInstance(-1,0,0)
        return horiz.createTransformedShape(transform)
    }

    static Path2D.Double axles(double minX, double maxX, double minY, double maxY, double scale) {
        Path2D.Double horiz = horizontalAxle(minX, maxX, scale)
        Path2D.Double vertic = verticalAxle(minY, maxY, scale)
        return combine(horiz, vertic)
    }
    static Path2D.Double axlesTicks(double minX, double maxX, double minY, double maxY, double scale, double tickSpace) {
        Path2D.Double horiz = horizontalAxleTicks(minX, maxX, scale,tickSpace)
        Path2D.Double vertic = verticalAxleTicks(minY, maxY, scale, tickSpace)
        return combine(horiz, vertic)
    }

    static Path2D.Double axlesTicks(double minX, double maxX, double minY, double maxY, double scale, double tickSpace, int majorInterval) {
        Path2D.Double horiz = horizontalAxleTicks(minX, maxX, scale,tickSpace, majorInterval)
        Path2D.Double vertic = verticalAxleTicks(minY, maxY, scale, tickSpace, majorInterval)
        return combine(horiz, vertic)
    }

    static Path2D.Double combine(Path2D.Double base, Shape... shapes) {
        Path2D.Double res = base.clone() as Path2D.Double
        for(Shape shape: shapes) {
            res.append(shape, false)
        }
        return res
    }

}
