package scrountch.geom

import java.awt.*
import java.awt.geom.Area
import java.awt.geom.Rectangle2D

/**
 * used to delimitate zones not handled by cell repaint but used for Collision detections.
 * these can be borders or any other zone inside the Cell
 * @author bamade
 */
// Date: 02/05/2016

class GraphicZone extends GraphicObject {
    int posX
    int posY
    int width
    int height

    /**
     * creates a rectangular Graphic Zone
     * @param x X coordinate
     * @param y Y coordinate
     * @param width
     * @param height
     */
    public GraphicZone (int x, int y, int width, int height){
        this.posX =x
        this.posY =y
        this.width = width
        this.height = height
        this.initialArea = new Area(new Rectangle(x,y, width, height))
    }

    /**
     * creates a GraphicZone from an Area
     * @param area
     */
    public GraphicZone(Area area) {
        this.initialArea = area
        Rectangle rect = area.bounds
        this.posX =rect.x
        this.posY =rect.y
        this.width = rect.width
        this.height = rect.height
    }
    @Override
    int getOriginalX() {
        return posX
    }

    @Override
    int getOriginalY() {
        return  posY
    }

    @Override
    int getOriginalHeight() {
        return  height
    }

    @Override
    int getOriginalWidth() {
        return width
    }

    @Override
    Rectangle2D getBoundsBeforeRotation(Cell currentCell) {
        return null
    }

    @Override
    protected void initArea(Cell cell) {

    }

    // may be could be drawn ... to be decided
    @Override
    boolean draw(Cell currentCell, Graphics2D graphics2D) {
        return false
    }
}
