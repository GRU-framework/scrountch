package scrountch

import scrountch.geom.Radial2D

import javax.swing.*
import java.awt.*
import java.awt.geom.Rectangle2D
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Static utilities for scrountch
 * @author bear amade
 */
// Date: 23/03/2016

class GlobalCommons {

    static ImageIcon scrountchIcon_small = new ImageIcon(GlobalCommons.class.getResource("/scrountch/images/scrountchIcon_small" +
            ".png") as URL)

    /**
     * constant to mark an int value as "not set"
     */
    public static final double NOT_SET = Integer.MIN_VALUE

    /**
     * forces a center disposition
     */

    public static final double FORCE_CENTER = Integer.MIN_VALUE + 8

    /**
     * cosntant to mark a value as "do not change" (the value is set elsewhere)
     */
    public static final double KEEP = Integer.MIN_VALUE + 16


    /**
     * the global logger for all scrountch codes
     */
    public static Logger LOG= Logger.getLogger("scrountch")

    /**
     * the user Locale: very important for names translations
     */
    static Locale curLocale = Locale.getDefault() ;

    /**
     * the resourceBundle used to translate method names
     * @see scrountch.geom.I18NMethodNaming
     */
    static ResourceBundle methBundle;
    static ResourceBundle messageBundle ;
    static {
        try {
            methBundle =  ResourceBundle.getBundle("scrountch/methods", curLocale)
        } catch(Exception exc) {
            LOG.log(Level.WARNING, " LOCALE : $curLocale", exc)

        }
        try {
            messageBundle =  ResourceBundle.getBundle("scrountch/messages", curLocale)
        } catch(Exception exc) {
            LOG.log(Level.WARNING, " LOCALE : $curLocale", exc)
        }
        metaInits()

    }

    /**
     * where methods are added to existing classes
     */
    static void metaInits() {
        Shape.metaClass.radialInfo = { ->
            Radial2D res = new Radial2D(delegate.getBounds2D())
            return res
        }
        Shape.metaClass.hypot = { ->
            Rectangle2D rect = delegate.getBounds2D()
            return Math.hypot(rect.height, rect.width)
        }

        Shape.metaClass.height = { ->
            delegate.getBounds2D().getHeight() ;
        }

        Shape.metaClass.width = { ->
            delegate.getBounds2D().getWidth() ;
        }

        Shape.metaClass.maxDim = { ->
            Rectangle2D rect = delegate.getBounds2D()
           return Math.max(rect.width, rect.height)
        }
    }
}
