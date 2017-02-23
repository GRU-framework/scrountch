package scrountch.geom

import java.awt.geom.Area

/**
 *
 * @author bamade
 */
// Date: 02/05/2016

class CollisionEvent {
    GraphicObject first
    GraphicObject second
    Area collisionArea
    CollisionSpec collisionSpec
    public CollisionEvent(GraphicObject first, GraphicObject second, Area collisionArea, CollisionSpec spec) {
        this.first = first
        this.second = second
        this.collisionArea = collisionArea
        this.collisionSpec = spec
    }
}
