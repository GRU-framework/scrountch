import scrountch.geom.Cell
import scrountch.geom.CompositeStroke
import scrountch.geom.GraphicShape
import scrountch.geom.SFrame

import java.awt.*
/**
 *
 * @author bamade
 */
// Date: 17/04/2016
Rectangle rect = new Rectangle(40,40, 80, 40)
GraphicShape shape = new GraphicShape(rect,0,0, new CompositeStroke( new BasicStroke( 10 ), new BasicStroke( 2 ) ) );
//GraphicShape shape = new GraphicShape(rect,new CompositeStroke( new BasicStroke( 10f ) ) );
//GraphicShape shape = new GraphicShape(rect  );
SFrame frame = new SFrame(200,200)
Cell cell = frame.getCell()
cell.addNoRepaint(shape)


