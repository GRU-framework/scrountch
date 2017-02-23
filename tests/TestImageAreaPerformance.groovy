import scrountch.Fab
import scrountch.geom.IconImage
import scrountch.geom.ImageUtils

import java.awt.*
import java.awt.geom.Area

/**
 *
 * @author bamade
 */
// Date: 02/05/2016
IconImage icon1 = Fab.createIconImageFromFile("draught1.jpg")
IconImage icon2 = Fab.createIconImageFromFile("duke0.gif")

Image img1 = icon1.image
Image img2 = icon2.image

long time = System.currentTimeMillis()
Area area1 = ImageUtils.getNonTransparentArea(img1)
println(System.currentTimeMillis() - time)
time = System.currentTimeMillis()
Area area2 = ImageUtils.getNonTransparentArea(img2)
println(System.currentTimeMillis() - time)

println area1.getBounds()
println area2.getBounds()

println area1.isRectangular()
println area2.isRectangular()

