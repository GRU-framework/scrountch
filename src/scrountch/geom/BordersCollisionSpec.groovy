package scrountch.geom

/**
 *
 * @author bamade
 */
// Date: 02/05/2016

class BordersCollisionSpec extends CollisionSpec {
    GraphicObject first
    public BordersCollisionSpec(GraphicObject first) {
        this.first = first
        isASet = true
    }
}
