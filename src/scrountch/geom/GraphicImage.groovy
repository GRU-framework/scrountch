package scrountch.geom

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D
import java.util.function.UnaryOperator

import static scrountch.GlobalCommons.KEEP
import static scrountch.GlobalCommons.NOT_SET
/**
 * A graphic Object that handles an Image. The iconImage data is loaded through an <TT>IconImage</TT> object.
 * Other data specific to instances of this class:
 * <UL>
 *     <LI> the initial coordinates position (coorX, coorY) though they can be changed this is discouraged
 *      <LI> the scale scale that can be changed any time
 * </UL>
 * The same graphic Image can be shared across different Cells of the same size
 * but not if Cells have different sizes
 * @author bear amade
 */
// Date: 23/03/2016

class GraphicImage extends GraphicObject {
    //class GraphicImage implements GraphicObject {
    //class GraphicImage implements GraphicObject, I18NMethodNaming {
    /**
     * the initial iconImage to display
     */
    IconImage iconImage;

    /**
     * the initial X coordinate
     */
    double coorX ;

    /**
     * the initial Y coordinate
     */
    double coorY ;

    /**
     * the display scale: can be changed before display. it's a percentage so 20 is 20/100D
     */
    int scale = 100;

    /**
     * the function to change the displayed iconImage : do not change directly and use
     * <TT>setImageHandler</TT>
     */
    Closure imageHandler ;


    /**
     * creates  a graphic object to display an iconImage
     * @param img the IconImage to display
     * @param coorx the initial x coordinate
     * @param coory the initial y coordinate
     * @param scale the display scale (percentage)
     */
    public GraphicImage(IconImage img, double coorx, double coory, int scale) {
        this.iconImage = img
        if(coorx <= KEEP) {
            coorx = NOT_SET
        }
        if(coory <= KEEP) {
            coory = NOT_SET
        }
        this.coorY = coory
        this.coorX = coorx
        this.scale = scale
    }

    /**
     * create a GraphicImage with a scale of 100
     * @param img
     * @param coorx
     * @param coory
     */
    public GraphicImage(IconImage img, double coorx, double coory) {
        this(img, coorx, coory, 100)
    }

    /**
     * creates a centered GraphicImage with a scale of 100.
     * The center is that of the Cell when displayed: so do not share the same instance
     * across Cells of different sizes!
     * @param img
     */
    public GraphicImage(IconImage img) {
        this(img, NOT_SET, NOT_SET, 100)
    }

    /**
     * creates a centered GraphicImageA.
     * The center is that of the Cell when displayed: so do not share the same instance
     * across Cells of different sizes!
     * @param img
     * @param scale
     */
    public GraphicImage(IconImage img, int scale) {
        this(img, NOT_SET, NOT_SET, scale)
    }

    /**
     * sets a transformation of iconImage content. The function implementing the argument
     * should have an Image argument and yield another Image (for instance by modifying colors)
     * it should NOT change the size of the iconImage.
     * @param closure transformation function (can be null)
     */
    public void setImageHandler( Closure<Image> closure) {
        if(closure == null){
            imageHandler = null
            return
        }
        Class[] parms = closure.parameterTypes
        if (parms.length != 1 && parms[0] instanceof Image) {
            throw new IllegalArgumentException("ImageHandler should have only one Image parameter")
        }
        imageHandler = closure
    }

    public void setImageHandler(UnaryOperator<Image> func) {
        setImageHandler {Image img -> func.apply(img)}
    }

    /**
     * @return the height of the contained iconImage without resize scale
     */
    public int getModelHeight() {
        return iconImage.getIconHeight()
    }

    /**
     * @return the width of the contained iconImage without resize scale
     */
    public int getModelWidth() {
        return iconImage.iconWidth
    }

    /**
     * @return the original X coordinate (before any transformation)
     */
    @Override
    int getOriginalX() {
        return coorX ;
    }

    /**
     * @return the original Y coordinate (before any transformation)
     */
    @Override
    int getOriginalY() {
        return  coorY ;
    }

    /**
     * @return the iconImage height with resize scale (before any transformation).
     * Beware: value may change if scale is changed!
     */
    @Override
    public int getOriginalHeight(){
        return getModelHeight() * (scale / 100.0)
    }

    /**
     * @return the iconImage width with resize scale (before any transformation).
     * Beware: value may change if scale is changed!
     */
    @Override
    public int getOriginalWidth() {
        return getModelWidth() * (scale /100.0)
    }

    /**
     * the enclosing Rectangle after the iconImage has been through translation and other transformation
     * but before rotation
     * @param currentCell the Cell in which the graphic object is displayed
     * @return
     */
    @Override
    Rectangle2D getBoundsBeforeRotation(Cell currentCell) {
        AffineTransform transformation = getTransformationBeforeRotation()
        return boundsBeforeRotation(transformation)
    }

    @Override
    protected void initArea(Cell cell) {
        Area rawArea = ImageUtils.getNonTransparentArea(iconImage.image)
        initialArea = rawArea.createTransformedArea(AffineTransform.getTranslateInstance(getOriginalX(), getOriginalY()))

    }
/**
     * internal method
     * @param transformation
     * @return
     */
    protected Rectangle2D boundsBeforeRotation (AffineTransform transformation) {
        Rectangle2D oldBounds = new Rectangle2D.Double(getOriginalX(), getOriginalY(), getOriginalWidth(),
                getOriginalHeight())
        if(transformation == null) {
            return oldBounds
        }
        return transformation.createTransformedShape(oldBounds).bounds2D
    }

    /**
     * internal method: do not invoke!
     * the first time this method is invoked the coordinates for this graphic object
     * are set (if "centered"). So that's why  this graphic Object should not be shared
     * across cells of different sizes
     * @param currentCell
     * @param graphics2D
     * @return
     */
    @Override
    synchronized boolean draw(Cell currentCell, Graphics2D graphics2D) {
        Image img = iconImage.getImage()
        int dimY = img.getHeight(Commons.observer) * (scale /100.0)
        int dimX = img.getWidth(Commons.observer) * (scale /100.0)
        if(coorX == NOT_SET) {
            coorX = (currentCell.getWidth()/2) - (dimX/2)
        }
        if(coorY == NOT_SET) {
            coorY = (currentCell.getHeight()/2) -(dimY/2)
        }
        // duplicated code that may change
        AffineTransform previousTrans = getTransformationBeforeRotation()
        if(this.rotation != null) {
            Rectangle2D rect = boundsBeforeRotation(previousTrans)
            AffineTransform transformer = this.rotation.transformIt(rect)
            if (previousTrans != null) {
                transformer.concatenate(previousTrans)
            }
            graphics2D.transform(transformer)
        } else {
            if(previousTrans != null) {
                graphics2D.transform(previousTrans)
            }
        }

        if(imageHandler != null) {
            Image curImage = img
            img = imageHandler(curImage)
            //println( "$curImage $img")
        }
        /*
         now  for  current Area the img is necessary
         for drawing needed: currentCell, img, graphics2D
         */
        ImgObserver observer = currentCell.mapObservers.get(this)
        if(observer == null) {
            observer = new ImgObserver(currentCell, img)
            currentCell.mapObservers.put(this, observer)
        }
        //graphics2D.drawImage(img,coorX, coorY,dimX, dimY, currentCell)
        observer.setContext(graphics2D)
        //observer.imageUpdate(img,8,coorX, coorY,dimX, dimY)
        int posX = coorX
        int posY = coorY
        graphics2D.drawImage(img,posX, posY ,dimX, dimY, observer)
        return true
    }

}
