package scrountch.geom

import javax.swing.*
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

/*
TODO: print shapes and text on the image
print clipped images on the Composite
reduce or augment initial size
 */

/**
 * instances of this class are mainly supposed to hide other graphic Objects.
 * they are displayed in front of them and transparent "holes" let see underlying objects.
 * They can be used also to create compound images built from different images on top of each other.
 * @author bamade
 */
// Date: 22/04/2016

class CompositeImage extends IconImage{
    BufferedImage bufferedImage

    public CompositeImage(BufferedImage bufr){
        super(bufr)
        this.bufferedImage = bufr
    }

    /**
     * factory method to create an CompositeImage from an ImageIcon
     * @param icon
     * @return
     */
    public static CompositeImage factFromIcon(ImageIcon icon){
        if(icon.getIconWidth() < 0) {
            throw new IllegalArgumentException("icon not loaded")
        }
        BufferedImage bufr = ImageUtils.toBufferedImage(icon)
        CompositeImage res = new CompositeImage(bufr)
        return res
    }

    /**
     * creates an image that displays only a Rectangle of a given Color
     * @param width
     * @param height
     * @param color
     * @return
     */
    public static CompositeImage factory(int width, int height, Color color){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[width*height]
        int rgb = color.getRGB()
        for(int ix = 0; ix < pixels.length; ix++)(
                pixels[ix] = rgb
        )
        //image.setRGB(0, 0, width, height, pixels, 0, width);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        CompositeImage res = new CompositeImage(image)
        return res
    }

    public static CompositeImage defaultCover(Cell cell){
        return factory(cell.width, cell.height,cell.background)
    }

    protected Graphics2D getPaintGraphics() {
        Graphics2D g2 = this.bufferedImage.graphics as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        return g2
    }
    /**
     * mostly used with Transparent Colors to create 'holes' in the cache
     * @param x
     * @param y
     * @param width
     * @param heigth
     * @param color
     */
    public void changeZoneColor(int x, int y, int width, int height, Color color) {
        int [] hole = new int[width*height]
        int rgb = color.getRGB()
        for(int ix = 0 ; ix < hole.length; ix++){
           hole[ix]  = rgb
        }
        bufferedImage.setRGB(x,y,width,height,hole,0,width)
    }

    public void superposeIcon(int x, int y, ImageIcon icon) {
        if(icon.iconWidth < 0) {
            throw new IllegalArgumentException("icon not loaded")
        }
        BufferedImage bufr = ImageUtils.toBufferedImage(icon)
        int width = icon.iconWidth
        int height = icon.iconHeight
        superposeBuffer(x,y,width,height,bufr)
    }

    public void superposeBuffer(Rectangle2D rect, BufferedImage bufr){
        int x = rect.x
        int y = rect.y
        int width = rect.width
        int height = rect.height
        superposeBuffer(x,y,width,height,bufr)

    }
    public void superposeBuffer(int x, int y, int width, int height, BufferedImage bufr){
        /* */
        Graphics2D graphics = this.getPaintGraphics()
        graphics.drawImage(bufr,x,y,width,height,null)
        graphics.dispose()
        /**/
    }

    public void superposeIcon(Rectangle2D rect, ImageIcon icon) {
        int x = rect.x
        int y = rect.y
        int width = rect.width
        int height = rect.height
        superposeIcon(x,y, width,height,icon)
    }

    public void superposeIcon(int x, int y, int width, int height, ImageIcon icon) {
        Image image = icon.image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        BufferedImage bufrImage = ImageUtils.toBufferedImg(image)
        superposeBuffer(x,y, width,height,bufrImage)
    }

    public void superposeShape(double x, double y, Shape shape, Color color, Paint fillPaint, Stroke stroke) {
        Graphics2D graphics2D = this.getPaintGraphics()
        if(color != null) {
            graphics2D.color = color
        }
        if(stroke!= null) {
            graphics2D.stroke = stroke
        }
        graphics2D.setPaintMode()
        Shape todraw = AffineTransform.getTranslateInstance(x,y).createTransformedShape(shape)
        graphics2D.draw(todraw)
        if(fillPaint != null) {
            graphics2D.fill(todraw)
        }
        graphics2D.dispose()
    }

    public void superposeShape(double x, double y, Shape shape, Color color, Paint fillPaint) {
        this.superposeShape(x, y, shape, color, fillPaint, null)
    }

    public void superposeText(double x, double y, String text, Font font, Color color, Paint fillPaint, Stroke stroke) {
        Graphics2D graphics2D = this.getPaintGraphics()
        graphics2D.font = font
        graphics2D.color = color
        if(fillPaint == null) {
            if(stroke != null) {
                throw new IllegalArgumentException("cannot have a stroke with a null fillPaint")
            }
            graphics2D.drawString(text,(int)x,(int)y)
        } else {
            Shape toDraw = ImageUtils.shapeFromString(x,y,text,font,graphics2D)
            graphics2D.setPaintMode()
            graphics2D.draw(toDraw)
            if(fillPaint != null) {
                graphics2D.fill(toDraw)
            }
        }
        graphics2D.dispose()
    }

    public void superposeClippedImage(double x, double y, Image img, Shape clip) {
        Graphics2D graphics2D = this.getPaintGraphics()
        superposeClipdImage(x,y,img, clip, graphics2D)
    }

    private  static void superposeClipdImage(double x, double y, Image img, Shape clip, Graphics2D graphics2D) {
        graphics2D.setClip(clip)
        graphics2D.drawImage(img, (int)x, (int)y,null)
        graphics2D.dispose()

    }
    public void superposeClippedImage(double x, double y, Image img, String text, Font font) {
        Graphics2D graphics2D = this.getPaintGraphics()
        Shape toDraw = ImageUtils.shapeFromString(x,y,text,font,graphics2D)
        superposeClipdImage(x,y,img, toDraw, graphics2D)
    }

}
