import scrountch.Fab
import scrountch.geom.*

import java.awt.*
import java.awt.geom.Ellipse2D

/**
 *
 * @author bamade
 */
// Date: 14/05/2016

IconImage marguerite = Fab.createIconImageFromFile("marguerites.png")
IconImage simpson = Fab.createIconImageFromFile("simpson.jpg")
Image simpsonGrey = ImageUtils.sepia(simpson.image)
int hMar = marguerite.iconHeight
int wMar = marguerite.iconWidth
int hSimp = simpson.iconHeight
int wSimp = simpson.iconWidth
CompositeImage compositeMar = CompositeImage.factFromIcon(marguerite)
double ellipseW = wSimp*0.6D
double posH = (hMar/2) - (hSimp/2)
Shape oval = new Ellipse2D.Double((wMar/2) - (ellipseW/2),posH ,ellipseW, hSimp)
compositeMar.superposeClippedImage( (wMar/2)-(wSimp/2), posH,simpsonGrey, oval)
GraphicImage graphicImage = new GraphicImage(compositeMar)
SFrame frame = new SFrame(wMar,hMar)
Cell cell = frame.getCell()
cell.addToGraphics(graphicImage)

