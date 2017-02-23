import scrountch.Fab
import scrountch.geom.*

import java.awt.*

/**
 *
 * @author bamade
 */
// Date: 21/04/2016

class TestDotSinus {
    public static void main(String[] args) {
        DotCurve dotCurve = new DotCurve(-3.20,+3.20,-1, +1, 0.1, 100, {double x -> Math.sin(x)})
        GraphicShape graphicShap = new GraphicShape(dotCurve, Fab.forceCenter(), Fab.forceCenter())
        SFrame frame = new SFrame(650,220,3,1)
        Cell cell = frame.getCell(0,0)
        cell.addToGraphics(graphicShap)
        LineCurve lineCurve = new LineCurve(-3.20,+3.20,-1, +1, 0.1, 100, { double x -> Math.sin(x)})
        GraphicShape graphicShap2 = new GraphicShape(lineCurve, Fab.forceCenter(), Fab.forceCenter())
        Shape axles = Fab.axlesTicks(-3.20,+3.20,-1,1,100,0.1,5)
        GraphicShape axlesGraf = Fab.centeredGraphicShape(axles, Color.BLUE)
        //println "width" + graphicShap2.getOriginalWidth()
        Cell cell1 = frame.getCell(1,0)
        cell1.addToGraphics(graphicShap2)
        cell1.addToGraphics(axlesGraf)
        SimpleHistogram histo = new SimpleHistogram(-3.20,+3.20,-1, +1, 0.1, 100, { double x -> Math.sin(x)})
        GraphicShape graphicShap3 = new GraphicShape(histo, Fab.forceCenter(), Fab.forceCenter())
        //println "width" + graphicShap2.getOriginalWidth()
        Cell cell2 = frame.getCell(2,0)
        cell2.addToGraphics(graphicShap3)
    }

}
