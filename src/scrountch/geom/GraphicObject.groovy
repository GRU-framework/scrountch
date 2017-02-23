package scrountch.geom

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D

/**
 * the common properties and behaviours of Graphic objects managed by instance of <TT>Cell</TT>.
 * <BR>
 *  All graphic Objects have an original place in the Cell (x,y coordinates) but can be drawn
 *  at a different place after a translation or transformation of drawing coordinates.
 *  <BR>
 *   implementation  WARNING: circular dependency with the Cell class!
 * @author bear amade
 */
// Date: 23/03/2016

abstract class GraphicObject {
    //trait GraphicObject {
    //String name ;

    /**
     * inner class used to deal with moves along a direction.
     * the axis of the direction is defined with an angle.
     * the coordinates are polar (angle+length)
     */
    class PolarPath {
        double originX
        double originY
        double length
        final double angleInRadians
        final double angle

        public PolarPath(double angle, double x, double y) {
            this.angle = angle % 360
            this.angleInRadians = Math.toRadians(this.angle)
            this.originX = x
            this.originY = y
        }

        public double getTransX() {
            this.originX + (length * Math.cos(angleInRadians))
        }

        public double getTransY() {
            this.originY + (length * Math.sin(angleInRadians))
        }
    }

    /**
     * the Drawing Color (may be changed later to more sophisticated Paint object)
     */
    Color drawPaint;

    /**
     * the Paint to fill shapes (not used by images and strings)
     */
    Paint fillPaint;

    /**
     * the stroke to draw
     */
    Stroke stroke;

    /**
     * the shape to draw (null by default)
     */
    Shape clip

    /**
     * additional data to be carried at user's convenience
     */
    Object data;

    /**
     * additional transformation that will be performed once defined translation is executed
     * and before any rotation
     */
    private AffineTransform transform;

    /**
     * used to translate some graphic Object defaults to identity
     */
    //AffineTransform initialTranslation = new AffineTransform()

    /**
     * rotation that may be executed after all other transformations have been registered
     */
    Rotation rotation;

    /**
     * translation along x axis : executed before any other transformation
     */
    double translateX;
    double initialTranslateX

    /**
     * translation along y axis : executed before any other transformation
     */
    double translateY;
    double initialTranslateY

    /**
     * keeps axis information if moves along an axis are used
     */
    PolarPath polarPath;

    /**
     * initialized when involved in detection collision
     */
    Area initialArea
    /**
     * accumulates translation values (x for X axis, y for y axis)
     * @param x
     * @param y
     */
    public synchronized void translation(double x, double y) {
        polarPath = null
        translateX += x
        translateY += y
    }

    /**
     * voids any translation
     */
    public synchronized void home() {
        polarPath = null
        translateX = initialTranslateX
        translateY =  initialTranslateY
    }

    /**
     * reinitializes the translation vector
     * @param x
     * @param y
     */
    public synchronized void reinitTranslation(double x, double y) {
        polarPath = null
        translateX = initialTranslateX +x
        translateY = initialTranslateY +y
    }

    /**
     * creates an axis for movements of the GraphicObject
     * @param angle
     */
    public synchronized void direction(double angle) {
        polarPath = new PolarPath(angle, translateX, translateY)
    }

    public synchronized void direction(double angle, double length) {
        polarPath = new PolarPath(angle, translateX, translateY)
        polarPath.length = length
        translateX = polarPath.getTransX()
        translateY = polarPath.getTransY()
    }

    /**
     * moves along the predefined axis
     * @param length
     * @throws IllegalStateException  if no axis has been defined
     */
    public synchronized void moveAlong(double length) {
        if (polarPath == null) {
            throw new IllegalStateException("direction is not set")
        }
        polarPath.length += length
        translateX = polarPath.getTransX()
        translateY = polarPath.getTransY()
    }

    public synchronized void changeDirection(double angle) {
        if (polarPath == null) {
            polarPath = new PolarPath(angle, translateX, translateY)
        } else {
            double originalAngle = polarPath.angle
            polarPath = new PolarPath(originalAngle + angle, translateX, translateY)
        }
    }

    /**
     * (for black belts only) concatenate a transformation to the current transformation.
     * These transformations are executed after translation and before rotation
     * @param transformation
     */
    public synchronized void addTransformation(AffineTransform transformation) {
        if (transform == null) {
            transform = transformation
        } else {
            transform = transform.clone() as AffineTransform
            transform.concatenate(transformation)
        }
    }

    /**
     * clears any intermediate transformation
     */
    public synchronized void clearTransformations() {
        transform = null
    }

    /**
     * addNoRepaint an angle to a rotation around the origin of the bounding rectangle.
     * @param angle
     *  @throws IllegalArgumentException if a centered rotation is already  registered for this object
     */
    public synchronized void cornerRotation(double angle) {
        if (rotation == null) {
            rotation = new Rotation(Rotation.Kind.ORIGIN, 0)
        } else if (rotation.kind != Rotation.Kind.ORIGIN) {
            throw new IllegalArgumentException("can't combine origin and center rotation")
        }
        rotation.angle += angle
    }

    /**
     * addNoRepaint an angle to a rotation around the center of the bounding rectangle.
     * @param angle
     *  @throws IllegalArgumentException if a corner rotation is already  registered for this object
     */
    public synchronized void centerRotation(double angle) {
        if (rotation == null) {
            rotation = new Rotation(Rotation.Kind.CENTER, 0)
        } else if (rotation.kind != Rotation.Kind.CENTER) {
            throw new IllegalArgumentException("can't combine origin and center rotation")
        }
        rotation.angle += angle
    }

    /**
     * clears any registered rotation
     */
    public synchronized void clearRotation() {
        rotation = null
    }

    /**
     * gets a transformation resulting from the concatenation of any translation plus
     * any registered intermediate transformation.
     * @return a transformation to be executed before rotation
     */
    public synchronized AffineTransform getTransformationBeforeRotation() {
        AffineTransform res = null;

        if (transform != null) {
            res = transform
        }
        if ((translateX != 0) || (translateY != 0)) {
            AffineTransform transl = AffineTransform.getTranslateInstance(translateX, translateY)
            if (res == null) {
                res = transl
            } else {
                res = res.clone() as AffineTransform
                res.concatenate(transl)
            }
        }
        return res
        /*
        if (res != null) {
            res.concatenate(initialTranslation)
            return res
        } else {
            if (initialTranslation.isIdentity()) {
                return null
            } else {
                return initialTranslation
            }
        }
        */

    }

    /**
     * @return the original X position of this Graphic Object
     */
    abstract int getOriginalX();

    /**
     * @return the original Y position of this Graphic Object
     */
    abstract int getOriginalY();

    /**
     * <B>Warning</B>: optional operation (does not work for Strings).
     *
     * @return the original height of this Graphic Object
     */
    abstract int getOriginalHeight();

    /**
     * <B>Warning</B>: optional operation (does not work for Strings).
     *
     * @return the original width of this Graphic Object
     */
    abstract int getOriginalWidth();

    /**
     * @param currentCell the Cell for which this value is queried
     * @return the bounds of the current object before rotation
     */
    abstract Rectangle2D getBoundsBeforeRotation(Cell currentCell);

    /***
     * initial Area initialisation when involved in collision detection.
     * The Cell argument is unimportant (needed for Strings: but should yield same values for each Cell)
     */
    abstract protected void initArea(Cell cell)

    /**
     * @param currentCell the Cell for which this value is queried
     * @return the bounds of the current object once displayed (after any rotation)
     */
    public Rectangle2D getActualBounds(Cell currentCell) {
        Rectangle2D rect = getBoundsBeforeRotation(currentCell)
        if (this.rotation == null) {
            return rect
        }
        AffineTransform transformer = this.rotation.transformIt(rect)
        //TODO : may not be correct! check
        return transformer.createTransformedShape(rect).bounds2D
    }

    /**
     * (internal method: do not use!)
     * <BR>
     *     draws the current object in the cell
     * @param currentCell
     * @param graphics2D
     * @return true
     */
    abstract boolean draw(Cell currentCell, Graphics2D graphics2D);

}
