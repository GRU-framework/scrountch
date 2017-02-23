import scrountch.geom.IconImage
import scrountch.geom.CompositeImage

/**
 *
 * @author bamade
 */
// Date: 22/04/2016

class TestImgCache {
    public static void main(String[] args) {
        IconImage icon = new IconImage("heart.png")
        CompositeImage imgCache = CompositeImage.factFromIcon(icon)
        println imgCache.bufferedImage
    }
}
