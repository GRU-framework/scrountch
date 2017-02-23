package scrountch.geom

import javax.swing.*
import javax.swing.plaf.LayerUI
import java.awt.*
import java.awt.geom.Rectangle2D

/**
 *
 * @author bamade
 */
// Date: 02/05/2016

class GlassOverCell extends LayerUI<JComponent> {
    Cell coveredCell
    Rectangle2D.Double rectangle
    @Override
    public void paint(Graphics g, JComponent c) {
        //super.paint(g, c);

    }

    public synchronized void showSelection(Graphics2D gr,Rectangle2D.Double rect) {
        this.rectangle = rect
        // todo : get rid of Graphics2D arg
        // does not work :
         //glassPaint(gr)
    }

    public synchronized void  glassPaint(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = coveredCell.getWidth();
        int h = coveredCell.getHeight();
        g2.setPaint(Cell.TRANSPARENT)
        g2.fillRect(0,0,w,h)
        //println("$g $w $h")
        if( rectangle != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2.setPaint(new GradientPaint(0, 0, Color.yellow, 0, h, Color.red));
            g2.fill(rectangle);
            g2.setColor(Color.WHITE)
            g2.draw(rectangle)
        }
        g2.dispose();
    }

    public GlassOverCell(Cell cell) {
        coveredCell = cell
        cell.layer0 = this
    }

    //except specific event for
    /*
    protected void processMouseMotionEvent(MouseEvent e) {
        coveredCell.processMouseMotionEvent(e)
    }

    protected void processMouseEvent(MouseEvent e) {
        coveredCell.processMouseEvent(e)
    }
    */
}
