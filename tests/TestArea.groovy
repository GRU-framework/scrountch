import scrountch.Fab
import scrountch.geom.*

import java.awt.*
import java.awt.image.BufferedImage
/**
 *
 * @author bamade
 */
// Date: 22/04/2016

class TestArea {
    public static void main(String[] args) {
        int width = 200
        int height = 200
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[width*height]
        int rgb = Color.LIGHT_GRAY.brighter().getRGB()
        for(int ix = 0; ix < pixels.length; ix++)(
            pixels[ix] = rgb
        )
        image.setRGB(0, 0, width, height, pixels, 0, 0);
       // Rectangle2D hole = new Rectangle2D.Double(10,10,50,50)
        int [] hole = new int[50]
        image.setRGB(10,10,50,50,hole,0,0)

        GraphicImage graphicImage = new GraphicImage(new IconImage(image))

        GraphicShape shapeRect = Fab.createGraphicRectangle(0,0,200,200)
        shapeRect.setFillPaint(Color.YELLOW)
        SFrame frame = new SFrame(200,200)
        Cell cell = frame.getCell()
        cell.addToGraphics(shapeRect)
        cell.addToGraphics(graphicImage)
        cell.forceRepaint()
    }
}
