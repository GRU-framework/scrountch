package scrountch

import org.codehaus.groovy.control.ParserPlugin
import org.codehaus.groovy.control.ParserPluginFactory

/**
 *
 * @author GLafforge
 */
// Date: 28/02/2017

class SourcePreProcessor extends ParserPluginFactory {
    ParserPlugin createParserPlugin() {
        new SourceModifierParserPlugin()
    }
}
