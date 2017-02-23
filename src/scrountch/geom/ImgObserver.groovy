package scrountch.geom

import scrountch.GlobalCommons

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.ImageObserver
import java.util.logging.Level

/**
 * instance of this class are used by Cells to attach an iconImage observer to each
 * GraphicImage.
 * The reason for this specific ImageObserver is that animated images invoke
 * constantly repaint events that clash with the Cell's repaint.
 * @author bamade
 */
// Date: 12/04/2016

class ImgObserver implements ImageObserver {
    final Image img;
    final Cell cell;
    AffineTransform transformation

    /**
     * An Oberver is created to link a Cell and a specific GraphicImage
     * @param cell final
     * @param img final
     */
    ImgObserver(Cell cell, Image img) {
        this.img = img
        this.cell = cell
    }

    /**
     * each time the corresponding GraphicImage is repainted its AffineTransForm
     * should be remembered (to be used by updates by animated images)
     * @param gr
     */
    public void setContext(Graphics2D gr) {
        this.transformation = gr.transform
    }

    /**
     * classic ImageUpdate of an observer.
     * Improant point: synchronized with Cell (so Cell repaint and forceRepaint events do not collide)
     * @param img
     * @param infoflags
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    @Override
    boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        int rate = -1;
        if ((infoflags & (FRAMEBITS | ALLBITS)) != 0) {
            rate = 0;
        }
        try {
            if (rate >= 0) {
                //cell.repaint(rate, x, y, width, height);
                Graphics2D graphics
                //println("begore imagUpdate")
                synchronized (cell) {
                    graphics = cell.graphics as Graphics2D
                    graphics.setTransform(transformation)
                    graphics.drawImage(img, x, y, width, height, null)
                }
                //println("after imagUpdate")
                cell.dump(graphics)
            }
        } catch (Exception exc) {
            GlobalCommons.LOG.log(Level.FINE, "ImageUpdate (interrupted?)", exc)
        }
        return (infoflags & (ALLBITS | ABORT)) == 0;

    }

}
