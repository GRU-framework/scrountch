package scrountch.geom

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D

import static scrountch.GlobalCommons.*

/**
 * A GraphicObject managing a <TT>Shape</TT> to be drawn by a Cell.
 * @author bear amade
 */
// Date: 23/03/2016


class GraphicShape extends GraphicObject {
    //class GraphicShape implements GraphicObject {
    //class GraphicShape implements GraphicObject, I18NMethodNaming {
    /**
     * the Shape to be drawn  (cannot be changed,
     * but shapes such as Path, Turtle, etc. can evolve between repaints)
     */
    final Shape shape;

    /**
     *  The initial X coordinate
     */
    double coorX ;

    /**
     *  The initial Y coordinate
     */
     double coorY ;



    /**
     * creates a GraphicObject with Shape and Draw attributes
     * @param shape
     * @param drawPaint
     * @param fillPaint
     * @param stroke
     */
    public GraphicShape( Shape shape, double x, double y ,Color drawPaint, Paint fillPaint, Stroke stroke) {
        this.shape = shape
        this.drawPaint = drawPaint
        this.fillPaint = fillPaint
        this.stroke = stroke
        /* TODO: specification problem: which is the meaning of NOT_SET ?
        in this case?
        0 or centered? if 0 this is not coherent with other GraphicObjects
        */

        if(x <= FORCE_CENTER || y <= FORCE_CENTER) {
            coorX = x
            coorY = y
        } else {
            Rectangle2D bounds = shape.bounds2D
            int keeps = 0
            if(x == KEEP ) {
                keeps++
                this.coorX = bounds.x
            }
            if(y == KEEP) {
                keeps+=2
                this.coorY = bounds.y
            }
            switch (keeps) {
                // we translate except if bounds are 0
                case 0 :
                    if( (x==0D && y == 0D)) {
                        this.coorX = x
                        this.coorY = y
                    } else { // translation
                        this.translateX = x
                        this.translateY = y
                        this.initialTranslateX = x
                        this.initialTranslateY = y
                       //this.initialTranslation = AffineTransform.getTranslateInstance(x, y)
                    }
                    break
                // we keep X but not Y
                case 1 :
                    this.translateY = y
                    this.initialTranslateY = y
                    //this.initialTranslation = AffineTransform.getTranslateInstance(0, y)
                    break ;
                // we keep Y but not X
                case 2 :
                    this.initialTranslateX = x
                    this.translateX = x
                    //this.initialTranslation = AffineTransform.getTranslateInstance(x, 0)
                    break ;
                // we keep both so do not change translation
                case 3 : break
            }

        }

    }

    /**
     * creates a Graphic centered shape with a specific drawPaint (not the one of the containing Cell)
     * @param shape
     * @param drawPaint
     */
    public GraphicShape( Shape shape, Color drawPaint) {
        this(shape,NOT_SET,NOT_SET, drawPaint, null, null)
    }

    /**
     * creates a Graphic shape with a specific drawPaint (not the one of the containing Cell)
     * @param shape
     * @param x
     * @param y
     * @param drawPaint
     */
    public GraphicShape( Shape shape, double x, double y ,Color drawPaint) {
        this(shape,x,y, drawPaint, null, null)
    }

    /**
     * creates a Graphic centered shape with specific drawPaint and fillPaint
     * @param shape
     * @param drawPaint
     * @param fillPaint
     */
    public GraphicShape( Shape shape, Color drawPaint, Paint fillPaint) {
        this(shape,NOT_SET,NOT_SET, drawPaint,fillPaint, null)
    }

    /**
     * creates a Graphic shape with specific drawPaint and fillPaint
     * @param shape
     * @param x
     * @param y
     * @param drawPaint
     * @param fillPaint
     */
    public GraphicShape( Shape shape, double x, double y, Color drawPaint, Paint fillPaint) {
        this(shape,x,y, drawPaint,fillPaint, null)
    }

    /**
     * creates a centered shape
     * @param shape
     * @param drawPaint
     * @param fillPaint
     * @param stroke
     */
    public GraphicShape( Shape shape, Color drawPaint, Paint fillPaint,Stroke stroke) {
        this(shape,NOT_SET,NOT_SET, drawPaint,fillPaint, stroke)
    }
    /**
     * creates a Graphic centered shape with specific Stroke
     * @param shape
     * @param stroke
     */
    public GraphicShape( Shape shape, Stroke stroke) {
       this(shape,NOT_SET,NOT_SET, null, null, stroke)
    }

    /**
     * creates a Graphic shape with specific Stroke
     * @param shape
     * @param x
     * @param y
     * @param stroke
     */
    public GraphicShape( Shape shape, double x, double y ,Stroke stroke) {
        this(shape,x,y, null, null, stroke)
    }

    /**
     * creates a Graphic Object with a centered Shape. Rendering attributes are those of the Cell
     * in which it is displayed.
     * @param shape
     */
    public GraphicShape( Shape shape) {
        this(shape, NOT_SET, NOT_SET, null, null,null)
    }

    /**
     * creates a Graphic Object with a Shape. Rendering attributes are those of the Cell
     * in which it is displayed.
     * @param shape
     * @param x
     * @param y
     */
    public GraphicShape( Shape shape, double x, double y) {
        this(shape, x, y, null, null,null)
    }

    /**
     *
     * @return a transformed Shape before any rotation
     */
    public Shape getRealShape() {
        AffineTransform transformer = getTransformationBeforeRotation() ;
        if(transformer == null) {
            return shape
        } else {
            return transformer.createTransformedShape(shape)
        }
    }

    /**
     * @return  original X coordinate
     */
    @Override
    int getOriginalX() {
        return coorX
    }

    /**
     * @return  original Y coordinate
     */
    @Override
    int getOriginalY() {
        return coorY
    }

    /**
     * @return  original height of the shape (before any transformation).
     * beware: may change between repaints if the Shape evolves (Path, Turtle,..)
     */
    @Override
    int getOriginalHeight() {
        return shape.bounds.height
    }

    /**
     * @return  original width of the shape (before any transformation).
     * beware: may change between repaints if the Shape evolves (Path, Turtle,..)
     */
    @Override
    int getOriginalWidth() {
        return shape.bounds.width
    }

    /**
     * the enclosing Rectangle after the shape has been through translation and other transformation
     * but before rotation
     * @param currentCell the Cell in which the graphic object is displayed
     * @return
     */
    @Override
    Rectangle2D getBoundsBeforeRotation(Cell currentCell) {
        AffineTransform transformation = getTransformationBeforeRotation()
        return boundsWithPreviousRotation(transformation)
    }

    @Override
    protected void initArea(Cell cell) {
        initialArea = new Area(shape)
    }
/**
     * internal method
     * @param transformation
     * @return
     */
    protected Rectangle2D boundsWithPreviousRotation(AffineTransform transformation) {
        if(transformation == null) {
            return shape.bounds2D
        }
        return transformation.createTransformedShape(shape).bounds2D
    }

    /**
     * internal method: do not use!
     * @param currentCell
     * @param graphics2D
     * @return
     */
    @Override
   synchronized boolean draw(Cell currentCell, Graphics2D graphics2D) {
        if(this.drawPaint != null) {
            graphics2D.color = this.drawPaint
        }
        if(this.stroke != null){
            graphics2D.stroke = this.stroke
        }

        if(coorX <= FORCE_CENTER || coorY <= FORCE_CENTER){ // we put coordinates to the middle
            Rectangle2D bounds = shape.bounds2D
            double initTransX = 0
            double initTransY = 0
            if(coorX <= FORCE_CENTER) {
                initTransX  = (currentCell.width/2) - (bounds.width/2)
                if(coorX == FORCE_CENTER) {
                    initTransX -= bounds.x
                }
                coorX = initTransX
            }
            if( coorY <= FORCE_CENTER) {
                initTransY  = (currentCell.height/2) - (bounds.height/2)
                if(coorY == FORCE_CENTER) {
                    initTransY -= bounds.y
                }
                coorY = initTransY
            }
            // 4 lines were : this.initialTranslation = AffineTransform.getTranslateInstance(initTransX, initTransY)
            this.translateX = initTransX
            this.translateY = initTransY
            this.initialTranslateX = initTransX
            this.initialTranslateY = initTransY
        }
        // duplicated because code may change
        AffineTransform previousTrans = getTransformationBeforeRotation()
        if(this.rotation != null) {
            Rectangle2D rect = boundsWithPreviousRotation(previousTrans)
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
        /*
        the Graphics2D.transform should be preserved
         for Collision initialArea needs transformed
         draw needs a transformed graphics2D
         */
        // voir comment changer les X,Y du shape
        //println graphics2D.transform
        graphics2D.draw(this.shape)
        if(this.fillPaint != null) {
            graphics2D.setPaintMode()
            graphics2D.paint = this.fillPaint
            graphics2D.fill(this.shape)
        }
        return true
    }
}
