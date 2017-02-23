package scrountch

import groovy.transform.ThreadInterrupt
import groovy.ui.Console
import groovy.ui.GroovyFileFilter
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.codehaus.groovy.runtime.StackTraceUtils

import javax.swing.*
import java.util.logging.Level
import java.util.prefs.Preferences
/**
 * a subclass of GroovyConsole with some additional features:
 * <UL>
 *     <LI> all classes from package scrountch and scrountch.geom are imported
 *      <LI> all factories from class scrountch.Fab are static imported
 *      <LI> class names described in internationalisation resource "scrountch/classes" are translated
 *      in user's language.
 *      <LI> class <TT>ScrountchScript</TT> is used as script base class.
 * </UL>
 *
 * Since this code has some parts copied from initial <TT>groovy.ui.Console</TT>
 * Here is a License statement :
 <PRE>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 </PRE>

 * @author bear amade
 */
// Date: 29/03/2016

class ScrountchConsole extends Console {
    static ImageIcon scrountchIcon
    static {
        URL url = ScrountchConsole.class.getResource('/groovy/ui/ConsoleIcon.png')
        if(url != null) {
            scrountchIcon = new ImageIcon(url)
        }
    }

    static scrPrefs = Preferences.userNodeForPackage(ScrountchConsole)


    public static void main(String[] args) {
        if (args.length == 1 && args[0] == '--help') {
            println '''usage: scrountch [options] [filename]
options:
--help                               This Help message
-cp,-classpath,--classpath <path>    Specify classpath'''
            return
        }

        // full stack trace should not be logged to the output window - GROOVY-4663
        java.util.logging.Logger.getLogger(StackTraceUtils.STACK_LOG_NAME).useParentHandlers = false

        //when starting via main set the look and feel to system
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        /* ??? how to be sure to get a RootLoader
        ClassLoader loader = ScrountchConsole.class.classLoader
        ClassLoader parent = loader.parent

        RootLoader rootLoader = loader.rootLoader as RootLoader
        if(rootLoader == null) {
            rootLoader = new RootLoader( parent.getURLs(), null)
        }
        rootLoader.addURL(new URL('file:.'))
        def console = new ScrountchConsole(rootLoader, new Binding())
        */

       def console = new ScrountchConsole(ScrountchConsole.class.classLoader?.getRootLoader(), new Binding())
        // use setIconImage
        console.useScriptClassLoaderForScriptExecution = true
        console.run()
        if (args.length == 1) console.loadScriptFile(args[0] as File)
    }

    public ScrountchConsole(ClassLoader loader, Binding binding) {
        super(loader, binding)
        //println loader
        groovyFileFilter = new ScrountchFileFilter()
        showScriptInOutput = scrPrefs.getBoolean('showScriptInOutput', false)
    }

    void newScript(ClassLoader parent, Binding binding) {
        config = new CompilerConfiguration()
        if (threadInterrupt) config.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt))

        //shell = new GroovyShell(parent, binding, config)
        shell = ShellGen.factory(parent, binding, config)
    }

    void updateTitle() {
        if (frame.properties.containsKey('title')) {
            if (scriptFile != null) {
                frame.title = scriptFile.name + (dirty ? ' * ' : '') + ' - ScrountchConsole'
            } else {
                frame.title = 'ScrountchConsole'
            }
        }
    }

    boolean askToSaveFile() {
        if (scriptFile == null || !dirty) {
            return true
        }
        String messages = "Save changes to"
        try {
            messages = GlobalCommons.messageBundle.getString('Save_changes_to')
        } catch (Exception exc) {
          GlobalCommons.LOG.log(Level.WARNING, "nor I18N resource for Save_changes_to")
        }
        switch (JOptionPane.showConfirmDialog(frame,
                "$messages " + scriptFile.name + '?',
                'ScrountchConsole', JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, scrountchIcon)) {
            case JOptionPane.YES_OPTION:
                return fileSave()
            case JOptionPane.NO_OPTION:
                return true
            default:
                return false
        }
    }

    def askToInterruptScript() {
        if (!scriptRunning) return true
        String messages = "Script executing. Press 'OK' to attempt to interrupt it before exiting."
        try {
            messages = GlobalCommons.messageBundle.getString('Interrupt_question')
        } catch (Exception exc) {
            GlobalCommons.LOG.log(Level.WARNING, "nor I18N resource for Interrupt_question")
        }
        def rc = JOptionPane.showConfirmDialog(frame, messages,
                'ScrountchConsole', JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, scrountchIcon)
        if (rc == JOptionPane.OK_OPTION) {
            doInterrupt()
            return true
        } else {
            return false
        }
    }

    class ScrountchFileFilter extends GroovyFileFilter {
        static SCROUNTCH_SOURCE_EXTENSIONS = ['*.scrountch', '*.groovy', '*.gvy', '*.gy', '*.gsh', '*.story', '*.gpp', '*.grunit']
        static SCROUNTCH_SOURCE_EXT_DESC = SCROUNTCH_SOURCE_EXTENSIONS.join(',')

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true
            }
            SCROUNTCH_SOURCE_EXTENSIONS.find { it == getExtension(f) } ? true : false

        }

        public String getDescription() {
            "Scrountch Source Files ($SCROUNTCH_SOURCE_EXT_DESC)"
        }

    }
}
