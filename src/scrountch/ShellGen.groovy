package scrountch

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.util.logging.Level
/**
 * Factories to create Groovy Shells
 * @author bear amade
 */
// Date: 06/04/2016

class ShellGen {
    /***
     * TODO: change the shell so that when parsing or running text it replaces translated tokens
     * <UL>
     *     <LI> keywords would translated with a simple text replacement with special char such as #tantQue -> while
     *     <LI> in text with different directions (such as arabic) may be we could change the "directional" tokens
     *      > to < , { to } and so on ... simpler way will be to read text and file the over way round.
     * </UL>
     * @param parent
     * @param binding
     * @param config
     * @return
     */
    public static GroovyShell factory(ClassLoader parent, Binding binding, CompilerConfiguration config) {
        CompilerConfiguration configuration = getCompilerConfiguration(config)
        return new GroovyShell(parent, binding, configuration)
    }
    // see groovy.ui.Console for class GroovyFileFilter

    /**
     * keeps translated classes and interfaces and the simple name
     * they will use as configurated entry
     */
    static Map<Class,String> translatedClasses = [:]

    /**
     *  first imports:
     *  <UL>
     *      <LI> static scrountch.Fab.*
     *      <LI> classes * from: scrountch.geom, scrountch, java.util.concurrent.atomic
     *      </UL>
     *   then  reads the scrountch/classes resources
     *   and register all classes where name are going to be translated
     *   these classes:
     *   <UL>
     *       <LI> are going to be imported (unless their translated name is the same as the key)
     *         <LI> if class is not an interface it's methodMissing is redirected
     *         to static method "missMethod"
     *         <LI> the class is registered in the map of translated
     *         classes and it's "simple name" is kept to be used in method
     *         translation resource
     *  </UL>
     *
     * @param config
     * @return
     */
    static CompilerConfiguration getCompilerConfiguration(CompilerConfiguration config) {
        if(config == null){
            config = new CompilerConfiguration()
        }
        ExpandoMetaClass.enableGlobally()
        // Add imports for script.
        def importCustomizer = new ImportCustomizer()
        importCustomizer.addStaticStars 'scrountch.Fab'
        importCustomizer.addStarImports('scrountch.geom', 'scrountch', 'java.util.concurrent.atomic')
        // now
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("scrountch/classes", GlobalCommons.curLocale)
            Set<String> keys = bundle.keySet()
            for (String key : keys) {
                String newName = bundle.getString(key)
                if (!key.equals(newName)) {
                    //println "import $key as $newName"
                    importCustomizer.addImport(newName, key)
                }
                try {
                    Class clazz = Class.forName(key)
                    if(!clazz.interface) {
                        clazz.metaClass."methodMissing" = { name, args -> missMethod(delegate, name, args) }
                    }
                    String className = key
                    Package pack = clazz.getPackage()
                    String packName = pack.getName()
                    if(packName.length() != 0) {
                        packName = packName+"."
                    }
                    String shortName = key.replace(packName,"")
                    translatedClasses.put(clazz, shortName)
                    // put keys in table
                } catch (Exception ex) {
                    GlobalCommons.LOG.log(Level.WARNING, "problem while setting methodMissing", ex)
                }
            }

        } catch (Exception exc) {
            GlobalCommons.LOG.log(Level.WARNING, "no internationalisation ressource for classes", exc)
        }
        config.addCompilationCustomizers(importCustomizer)

        config.setScriptBaseClass('scrountch.ScrountchScript')
        return config
    }

    /**
     * the method to be invoked by all classes where methodMissing has been
     * redirected:
     * <UL>
     *     <LI> if the current class has a translation
     *     then it is invoked and the metaclass has this method added
     *     <LI> if the super class is translated then
     *     if found the corresponding method is invoked and
     *     registered on the current class
     *     <LI> if an interface is registered then corresponding method
     *     is invoked and registered. BEWARE: if there is a name conflict
     *     the first name found will be invoked!
     *     <LI> if everything fails then a static method of Fab is tested
     *     <LI> then if it fails it fails!
     * <UL>
     * @param instance
     * @param name
     * @param args
     * @return
     */
    static def missMethod(Object instance, String name, args) {
        Class curclazz = instance.class
        Set<Class> setInterface = new HashSet<Class>()
        Class anotherClass = curclazz
        ResourceBundle mBundle = GlobalCommons.methBundle;
        if (mBundle != null) {
            String methodName = null
            String shortName = translatedClasses.get(anotherClass)
            while (methodName == null) {
                Class[] interf = anotherClass.interfaces
                for(Class clazz : interf) {
                    setInterface.add(clazz)
                }
                // does not work for inner classes! fuck! String shortName = anotherClass.getSimpleName()
                try {
                    try {
                        //println "$shortName  # $name"
                        methodName = mBundle.getString(shortName + '#' + name)
                    } catch (MissingResourceException e) {
                        try {
                            if (methodName == null) {
                                methodName = mBundle.getString(anotherClass.name + '#' + name)
                            }
                        }catch(MissingResourceException missExc){
                            anotherClass = anotherClass.superclass
                            shortName = translatedClasses.get(anotherClass)
                            if( shortName != null) {
                                continue
                            } else {
                                 // first let's try interfaces then Fab
                                   boolean foundInterface = false
                                   for(Class iClazz : setInterface) {
                                       shortName = translatedClasses.get(iClazz)
                                       if(shortName != null) {
                                           try {
                                               //println "$shortName  # $name"
                                               methodName = mBundle.getString(shortName + '#' + name)
                                               foundInterface = true
                                              MetaClass metaClass = iClazz.metaClass
                                               if(metaClass instanceof  ExpandoMetaClass) {
                                                   metaClass.registerInstanceMethod(name, { argz -> metaClass.invokeMethod(delegate, methodName, argz) })
                                               }
                                           } catch(MissingResourceException mexc){
                                               continue
                                           }
                                          break;
                                       }
                                   }
                                if(! foundInterface) {
                                    methodName = mBundle.getString('Fab#' + name)
                                    return Fab.metaClass.invokeStaticMethod(Fab, methodName, args)
                                }
                            }
                        }
                    }
                } catch (Exception exc) {
                    throw new MissingMethodException(name, curclazz, args)
                }
            }
            /*does not WORK! for java classes*/
            MetaClass meta = curclazz.metaClass
            //println "missing with $methodName $meta"
            if(meta instanceof  ExpandoMetaClass) {
                meta.registerInstanceMethod(name, { argz -> meta.invokeMethod(delegate, methodName, argz) })
            } else {
                //println meta
            }
            //println "$curclazz $methodName"
            /**/
            curclazz.metaClass.invokeMethod(instance, methodName, args)
        } else {
            throw new MissingMethodException(name, curclazz, args)
        }
    }
}
