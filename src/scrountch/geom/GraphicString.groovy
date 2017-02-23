package scrountch.geom

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Rectangle2D

import static scrountch.GlobalCommons.KEEP
import static scrountch.GlobalCommons.NOT_SET
/**
 * A graphic Object that manages a String.
 * <BR>
 * @author bear amade
 */
// Date: 23/03/2016

class GraphicString extends GraphicObject {
    //class GraphicString implements GraphicObject {
    //class GraphicString implements GraphicObject, I18NMethodNaming {

    /**
     * the managed string: changing it my have strange display effects
     */
    String string;

    /**
     * the font to draw the string: changing it my have strange display effects.
     * If null the default font of the Cell will be used
     */
    Font font;

    /**
     * X coordinate of the enclosing rectangle
     */
    double coorX;

    /**
     * Y coordinate of the enclosing rectangle
     */
    double coorY;

    //TODO : add fillPaint if not null then draw outline and fill
    public GraphicString(String string, double coorx, double coory, Font font, Color drawPaint, Paint fillPaint, Stroke stroke) {
        this.string = string;
        if(coorx <= KEEP) {
            coorx = NOT_SET
        }
        if(coory <= KEEP) {
            coory = NOT_SET
        }
        this.coorX = coorx
        this.coorY = coory
        this.font = font
        this.drawPaint = drawPaint
        this.fillPaint = fillPaint
        if((stroke!= null) && (fillPaint == null)) {
            throw new IllegalArgumentException("cannot have a stroke without a fillPaint")
        }
        this.stroke = stroke
    }
    /**
     * creates a Graphic Object that draws a String.
     * @param string
     * @param font
     * @param drawPaint
     * @param coorx
     * @param coory
     */
    public GraphicString(String string, double coorx, double coory, Font font, Color drawPaint) {
        this(string, coorx, coory, font, drawPaint, null,null)
    }

    /**
     * creates a Graphic Object that draws a String.
     * Font and Color will be that of the enclosing Cell.
     * @param string
     * @param coorx
     * @param coory
     */
    public GraphicString(String string, double coorx, double coory) {
        this(string, coorx, coory,null, null)
    }

    /**
     * creates a Graphic Object that draws a String.
     * The String will be centered in the enclosing Cell
     * Font and Color will be that of the enclosing Cell.
     * Do not share across Cells with different dimensions.
     * @param string
     */
    public GraphicString(String string) {
        this(string, NOT_SET, NOT_SET, null, null)
    }

    /**
     * creates a Graphic Object that draws a String.
     * The String will be centered in the enclosing Cell
     * Color will be that of the enclosing Cell.
     * Do not share across Cells with different dimensions.
     * @param string
     * @param font
     */
    public GraphicString(String string, Font font) {
        this(string, NOT_SET, NOT_SET, font, null)
    }

    /**
     * creates a Graphic Object that draws a String.
     * The String will be centered in the enclosing Cell
     * Font will be that of the enclosing Cell.
     * Do not share across Cells with different dimensions.
     * @param string
     * @param drawPaint
     */
    public GraphicString(String string, Color drawPaint) {
        this(string,NOT_SET,NOT_SET, null, drawPaint)
    }

    /**
     * creates a Graphic Object that draws a String.
     * The String will be centered in the enclosing Cell
     * Do not share across Cells with different dimensions.
     * @param string
     * @param font
     * @param drawPaint
     */
    public GraphicString(String string, Font font, Color drawPaint) {
        this(string, NOT_SET, NOT_SET, font, drawPaint)
    }

    /**
     * creates a Graphic Object that draws a String.
     * Color will be that of the enclosing Cell
     * @param string
     * @param font
     * @param coorx
     * @param coory
     */
    public GraphicString(String string,  double coorx, double coory, Font font) {
        this(string,coorx, coory, font, null)
    }

    /**
     * @return the original X coordinate of the enclosing rectangle
     */
    @Override
    int getOriginalX() {
        return coorX;
    }

    /**
     * @return the original Y coordinate of the enclosing rectangle
     */
    @Override
    int getOriginalY() {
        return coorY;
    }

    /**
     * <B>do not invoke this method</B>: you can't get the height of the String if it's not displayed
     * in a Cell: use  <TT>getStdHeight()</TT> instead
     * @return NOT_SET constant
     */
    @Override
    int getOriginalHeight() {
        return NOT_SET
    }

    /**
     * the standard height of this String if displayed without transformation in a given Cell
     * @param curCell
     * @return
     */
    int getStdHeight(Cell curCell) {
        Graphics2D gr = curCell.graphics
        // get metrics from the graphics
        Font curfont
        if(this.font != null) {
            curfont = this.font
        } else {
            curfont = curCell.font
        }
        FontMetrics metrics = gr.getFontMetrics(curfont);
        // get the height of a line of text in this
        // font and render context
        return metrics.getHeight();
    }

    int getStdAscent(Cell curCell) {
        Graphics2D gr = curCell.graphics
        // get metrics from the graphics
        Font curfont
        if(this.font != null) {
            curfont = this.font
        } else {
            curfont = curCell.font
        }
        FontMetrics metrics = gr.getFontMetrics(curfont);
        return metrics.ascent
    }

    int getStdDescent(Cell curCell) {

        Graphics2D gr = curCell.graphics
        // get metrics from the graphics
        Font curfont
        if(this.font != null) {
            curfont = this.font
        } else {
            curfont = curCell.font
        }
        FontMetrics metrics = gr.getFontMetrics(curfont);
        return metrics.descent
    }

    /**
     * <B>do not invoke this method</B>: you can't get the width of the String if it's not displayed
     * in a Cell: use  <TT>getStdHeight()</TT> instead
     * @return NOT_SET constant
     */
    @Override
    int getOriginalWidth() {
        return NOT_SET
    }

    /**
     * the standard width of this String if displayed without transformation in a given Cell
     * @param curCell
     * @return
     */
    int getStdWidth(Cell curCell) {
        Graphics2D gr = curCell.graphics
        // get metrics from the graphics
        Font curfont
        if (font != null) {
            curfont = this.font
        } else {
            curfont = curCell.font
        }
        FontMetrics metrics = gr.getFontMetrics(curfont);
        return metrics.stringWidth(string)
    }

    /**
     * the enclosing Rectangle after the string has been through translation and other transformation
     * but before rotation
     * @param currentCell the Cell in which the graphic object is displayed
     * @return
     */
    @Override
    Rectangle2D getBoundsBeforeRotation(Cell curCell) {
        AffineTransform transformation = getTransformationBeforeRotation()
        return boundsBeforeRotation(curCell, transformation)
    }

    @Override
    protected void initArea(Cell cell) {
        Rectangle rect = new Rectangle(originalX, originalY,getStdWidth(cell), getStdHeight(cell))
        initialArea = new Area(rect)
    }
/**
     * internal method
     * @param curCell
     * @param transformation
     * @return
     */
    protected Rectangle2D boundsBeforeRotation(Cell curCell, AffineTransform transformation) {
        //strangely correct even with outline!
        int dimX = this.getStdWidth(curCell)
        int dimY = this.getStdHeight(curCell)
        Rectangle2D rect = new Rectangle2D.Double(coorX, coorY, dimX, dimY)
        if (transformation != null) {
            return transformation.createTransformedShape(rect).bounds2D
        }
        return rect

    }

/**
 * internal method: do not use!
 * @param currentCell
 * @param graphics2D
 * @return
 */
    @Override
    synchronized boolean draw(Cell currentCell, Graphics2D context) {
        if (this.drawPaint != null) {
            context.color = this.drawPaint
        }

        if (this.font != null) {
            context.font = this.font
        }
        //TODO fill paint with outline
        if (coorX == NOT_SET) {
            int dimX = this.getStdWidth(currentCell)
            coorX = Math.abs((currentCell.getWidth() / 2) - (dimX / 2))
            this.coorX = coorX
        }
        int  dimY = this.getStdHeight(currentCell)
        if (coorY == NOT_SET) {
         //   int  dimY = this.getStdHeight(currentCell)
            coorY = Math.abs((currentCell.getHeight() / 2) - (Math.abs(dimY) / 2))
            this.coorY = coorY
        }
        // duplicated because code may change
        AffineTransform previousTrans = getTransformationBeforeRotation()
        if (this.rotation != null) {
            Rectangle2D rect = boundsBeforeRotation(currentCell, previousTrans)
            AffineTransform transformer = this.rotation.transformIt(rect)
            if (previousTrans != null) {
                transformer.concatenate(previousTrans)
            }
            context.transform(transformer)
        } else {
            if (previousTrans != null) {
                context.transform(previousTrans)
            }
        }
        /*
        for Collision Area needs context (beware of transfo
        draw needs kep context
         */
        int posX = coorX
        int posY = coorY
        if(this.fillPaint == null) {
            context.drawString(this.string, posX, posY)
        } else {
            Shape outline = ImageUtils.shapeFromString(coorX, coorY,this.string, context.font, context)
            /**
            println( "$posX, $posY $dimY $outline.bounds")
            /**/
            if(stroke != null) {
                context.setStroke(stroke)
            }
            context.draw(outline)
            context.setPaintMode()
            context.paint = this.fillPaint
            context.fill(outline)
        }
        return true
    }


}
