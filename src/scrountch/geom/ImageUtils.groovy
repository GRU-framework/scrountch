package scrountch.geom

import scrountch.utils.Cache

import javax.swing.*
import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.awt.image.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator

/**
 *
 * @author bamade
 */
// Date: 23/04/2016

class ImageUtils {
    static Toolkit DEFAULT_TOOLKIT = Toolkit.getDefaultToolkit()
    static Robot ROBOT = new Robot()

    static BufferedImage imageCapture(Cell comp, Rectangle2D rect) {
        return imageCapture(comp, rect.getBounds())
    }

    static BufferedImage imageCapture(Cell comp, Rectangle rect) {
        int x = rect.x
        int y =rect.y
        int rectWidth = rect.width
        int rectHeight = rect.height
        int width = comp.getWidth()
        int height = comp.getHeight()
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        Graphics graphics = bufferedImage.createGraphics()
        graphics.setClip(rect)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        comp.update(graphics, false)

        Image xtract = bufferedImage.getSubimage(x,y, rectWidth, rectHeight)
        BufferedImage res = deepCopy(xtract)
        /* other implementation
        Point point = comp.getLocationOnScreen()
        point.translate(x,y)
        Rectangle newRect = new Rectangle(point, rect.getSize())
        BufferedImage res = ROBOT.createScreenCapture(newRect)
        */
        return res
    }

    // copied from the internet subimages wreck havoc
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(ImageIcon icon) {
        Image img = icon.getImage()
        return toBufferedImg(img)
    }

    public static BufferedImage toBufferedImg(Image img) {
        if(img instanceof BufferedImage){
            return img
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


    public static int[][] toArray2D(int[] tb, int width, int height) {
        if ((width * height) != tb.length) {
            throw new IllegalArgumentException("width*height incompatible with array size")
        }
        int[][] result = new int[height][width];
        int row = 0
        int col = 0
        for (int yx = 0; yx < tb.length; yx++) {
            result[row][col] = tb[yx];
            col++;
            if (col == width) {
                col = 0;
                row++;
            }
        }
        return result
    }


    public static Area getNonTransparentArea(Image colorImage) {
        BufferedImage bufr  = toBufferedImg(colorImage)
        int width = bufr.width
        int height = bufr.height
        Area res = new Area()
        for (int y = 0; y < height; y++) {
            int x = 0
            while (x < width) {
                //TODO: modify to just alpha transparency total (not to plain 0)
                while ((x < width) && (bufr.getRGB(x, y) & 0xFF000000) == 0) {
                    x++
                }
                if (x < width) {
                    int start = x
                    while ((x < width) && (bufr.getRGB(x, y) & 0xFF000000) != 0) {
                        x++
                    }
                    res.add(new Area(new Rectangle(x, y, x - start, 1)))
                }
            }
        }
        return res
    }

    static AtomicInteger counter = new AtomicInteger();
    static MediaTracker DUMMY_TRACKER = new MediaTracker(new Canvas())
    static Map<Component, MediaTracker> mapTracks = [:]
    static Cache<String, Image> cache = new Cache<String, Image>(7, 5)


    public static Image applyFilter(Image colorImage, ImageFilter filter) {
        return applyFilter(colorImage, null, filter)

    }

    public static Image applyFilter(Image colorImage, String key, ImageFilter filter) {
        Image imgRes
        if (key != null) {
            imgRes = cache.get(key)
            if (imgRes != null) {
                return imgRes
            }
        }
        ImageProducer producer = new FilteredImageSource(colorImage.getSource(), filter);
        imgRes = DEFAULT_TOOLKIT.createImage(producer);
        MediaTracker tracker = DUMMY_TRACKER
        int cpt = counter.getAndIncrement()
        tracker.addImage(imgRes, cpt)
        tracker.waitForID(cpt)
        if (key != null) {
            cache.put(key, imgRes)
        }
        return imgRes
    }


    public static Image grey(Image colorImage) {
       return grey(colorImage, null)
    }

    public static Image grey(Image colorImage, String key) {
        return pixelTransform( colorImage, key , { int rgb ->
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;
            int nrgb = ((0.2126 * r) + (0.7152 * g) + (0.0722 * b))
            int tr = (int) nrgb
            int tg = (int) nrgb
            int tb = (int) nrgb
            r = tr > 255 ? 255 : tr
            g = tg > 255 ? 255 : tg
            b = tb > 255 ? 255 : tb
            return (a << 24) | (r << 16) | (g << 8) | b;
        })
    }

    public static Image sepia(Image colorImage) {
        return sepia(colorImage, null)
    }

    public static Image sepia(Image colorImage, String key) {
        return pixelTransform( colorImage, key , { int rgb ->
            int a = (rgb >> 24) & 0xff;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;

            /*
             */

            /**/
            //calculate tr, tg, tb
            int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b) ;
            r = tr > 255 ? 255 : tr
            //if(r <25) r=25
            g = tg > 255 ? 255 : tg
            b = tb > 255 ? 255 : tb
            return (a << 24) | (r << 16) | (g << 8) | b;
            /**/
        })
    }

    public static Image pixelTransform(Image colorImage, String key, Closure trans) {
        if(key != null) {
            Image imgRes = cache.get(key)
            if (imgRes != null) {
                return imgRes
            }
        }
        BufferedImage bufr = toBufferedImg(colorImage)
        int width = bufr.width
        int height = bufr.height
        int type = bufr.getType()
        int[] intBuffer = new int[width * height]
        bufr.getRGB(0, 0, width, height, intBuffer, 0, width)
        Class[] argsTypes = trans.getParameterTypes()
        boolean useColor = (argsTypes[0] == Color.class)

        for (int ix = 0; ix < intBuffer.length; ix++) {
            int rgb = intBuffer[ix]
            if(useColor) {
                Color col = new Color(rgb,true)
                col = trans(col)
                intBuffer[ix] = col.getRGB()
            } else {
                intBuffer[ix] = trans(rgb) as int
            }
        }
        BufferedImage res = new BufferedImage(width, height, type)
        res.setRGB(0, 0, width, height, intBuffer, 0, width)
        if(key != null) {
            cache.put(key, res)
        }
        return res
    }

    public static Image pixelRGBTransform(Image colorImage, String key, IntUnaryOperator transformer) {
        return pixelTransform(colorImage,key, {int rgb -> transformer.applyAsInt(rgb)})
    }

    public static Image imageOp(Image colorImage, BufferedImageOp bufferedImageOp) {
        return imageOp(colorImage, null, bufferedImageOp)

    }

    public static Image imageOp(Image colorImage, String key, BufferedImageOp bufferedImageOp) {
        return  applyFilter(colorImage, key, new BufferedImageFilter(bufferedImageOp))
    }

    /**
     * float[] elements = { .1111f, .1111f, .1111f,
     .1111f, .1111f, .1111f,
     .1111f, .1111f, .1111f};
     static float[] blur3_3F = [ 0.1f, 0.1f, 0.1f, // 3x3 blur
     0.1f, 0.2f, 0.1f,
     0.1f, 0.1f, 0.1f ]
     */

    static float[] blur3_3F = [1 / 16f, 1 / 8f, 1 / 16f,
                               1 / 8f, 1 / 4f, 1 / 8f,
                               1 / 16f, 1 / 8f, 1 / 16f]
    static BufferedImageOp BLUR_3_3 = new ConvolveOp(new Kernel(3, 3, blur3_3F))


    static float[] sharpen3_3F = [-1.0f, -1.0f, -1.0f, // 3x3 sharpen
                                  -1.0f, 9.0f, -1.0f,
                                  -1.0f, -1.0f, -1.0f]

    static BufferedImageOp SHARPEN3_3 = new ConvolveOp(new Kernel(3, 3, sharpen3_3F))

    static float[] edge3_3F = [0.0f, -1.0f, 0.0f, // 3x3 edge
                               -1.0f, 5.0f, -1.0f,
                               0.0f, -1.0f, 0.0f]
    static BufferedImageOp EDGE3_3 = new ConvolveOp(new Kernel(3, 3, edge3_3F))

    static float[] edge5_5F = [-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, // 5x5 edge
                               -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                               -1.0f, -1.0f, 24.0f, -1.0f, -1.0f,
                               -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                               -1.0f, -1.0f, -1.0f, -1.0f, -1.0f]
    static BufferedImageOp EDGE5_5 = new ConvolveOp(new Kernel(5, 5, edge5_5F))

    static short[] invertTable = new short[256]
    static short[] posterTable = new short[256]
    static BufferedImageOp INVERT
    static BufferedImageOp POSTER
    static { // initTables
        for (int ix = 0; ix < 256; ix++) {
            invertTable[ix] = (short) 255 - ix;
            posterTable[ix] = (short) (ix - (ix %64))
        }
        INVERT = new LookupOp(new ShortLookupTable(0, invertTable), new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        POSTER = new LookupOp(new ShortLookupTable(0, posterTable), new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
    }

    static BufferedImage pixelate(Image colorImage, int pixSize) {
        return pixelate(colorImage, null, pixSize)

    }

    static BufferedImage pixelate(Image colorImage, String key, int pixSize) {
        if (key != null) {
            Image imgRes = cache.get(key)
            if (imgRes != null) {
                return imgRes
            }
        }
        BufferedImage img = toBufferedImg(colorImage)
        int type =  img.getType()
// Get the raster data (array of pixels)
        Raster src = img.getData();
        int height = src.getHeight()
        int width = src.getWidth()

// Create an identically-sized output raster
        WritableRaster dest = src.createCompatibleWritableRaster();

// Loop through every pixSize pixels, in both x and y directions
        /* this code does not work on subimages, hence the deepcopy */
        int destHeight = dest.getHeight()
        int destWidth = dest.getWidth()
        for (int y = 0; y < height; y += pixSize) {
            for (int x = 0; x < width; x += pixSize) {
                // Copy the pixel
                double[] pixel
                pixel = src.getPixel(x, y, (double[]) null);

                // "Paste" the pixel onto the surrounding pixSize by pixSize neighbors
                // Also make sure that our loop never goes outside the bounds of the image
                for (int yd = y; (yd < y + pixSize) && (yd < destHeight); yd++) {
                    for (int xd = x; (xd < x + pixSize) && (xd < destWidth); xd++) {
                        //println" $xd $yd $pixel $destWidth $destHeight"
                        dest.setPixel(xd, yd, pixel);
                    }
                }
            }
        }
        BufferedImage res = new BufferedImage(width, height, type)

// Save the raster back to the Image
        res.setData(dest);
        if (key != null) {
            cache.put(key, res)
        }
        return res;
    }

    public static Shape shapeFromString(double coorX, double coorY, String string, Font font, Graphics2D context) {
        FontRenderContext frc = context.getFontRenderContext();
        TextLayout textTl = new TextLayout(string, font, frc);
        //explanation: the outline is a PATH that starts at the baseline of the font!
        // so using the bounds does not work properly!
        // the ascent is NOT the correct measure!
        // the draw method of Textlayout may be correct but it's not what we want!
        //int ascent = getStdAscent(currentCell)
        // does not work ascent = (dimY-getStdDescent(currentCell))
        //Shape outline = textTl.getOutline(AffineTransform.getTranslateInstance(coorX, coorY + ascent))
        // does not work ... unless we find a better way
        //Shape outline = font.createGlyphVector(context.getFontMetrics().getFontRenderContext(), string).getOutline((float) coorX, (float) coorY);
        /*  does not work for some fonts
        Shape outline = textTl.getOutline(null)
        double[] initial = new double[6]
        outline.getPathIterator(null).currentSegment(initial)
        double init =  -initial[1]
        AffineTransform transform2 = context.transform
        transform2.translate(coorX, coorY +init)
        context.setTransform(transform2)
        /**/
        // seems to f*** work
        Shape outline = textTl.getOutline(null)
        int plus = Math.abs(outline.getBounds().y)
        outline = AffineTransform.getTranslateInstance(coorX, coorY+plus).createTransformedShape(outline)
        return outline
    }
}
