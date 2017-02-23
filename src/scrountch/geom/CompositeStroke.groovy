package scrountch.geom

import java.awt.Shape
import java.awt.Stroke

/**
 *
 * @author bamade
 */
// Date: 17/04/2016

class CompositeStroke implements Stroke {
    Stroke[] strokes
    public CompositeStroke(Stroke... str) {
        this.strokes = str
    }
    @Override
    Shape createStrokedShape(Shape p) {
        Shape res = p
        for(Stroke stroke : strokes) {
            res = stroke.createStrokedShape(res)
        }
        return res
    }
}
