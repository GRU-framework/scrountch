package scrountch

/**
 * The script used by the scrountchConsole:
 * <UL>
 *     <LI> all the factories in <TT>Fab</TT> can be invoked directly as functions
 * </UL>
 * @author bear amade
 */
// Date: 30/03/2016

abstract class ScrountchScript  extends Script{

    def methodMissing(String name, args){
        Fab.metaClass.invokeStaticMethod(Fab.class,name, args)
    }
}
