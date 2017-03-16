package scrountch

import org.codehaus.groovy.antlr.AntlrParserPlugin
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.Reduction

import java.util.logging.Level

/**
 *
 * @author GLafforge
 * @author BAmade (for translate method: to be changed!)
 */
// Date: 28/02/2017

class SourceModifierParserPlugin extends AntlrParserPlugin {
    /*
     supposons ici que:
      - l'on charge le dictionnaire des traductions (par ex. tantQue -> while=
     */
    // à charger dans une ressource I18N
    static Properties dictionary
    static {
        try {
            if (!GlobalCommons.NO_KW_TRANSLATION) {
                ResourceBundle bundle = ResourceBundle.getBundle("scrountch/keywords", GlobalCommons.curLocale)
                // not very efficient but simplifiess queries
                Set<String> keys = bundle.keySet()
                if (keys.size() != 0) {
                    dictionary = new Properties()
                    for (String key : keys) {
                        dictionary.setProperty(key, bundle.getString(key))
                    }
                }
            }
        } catch (Exception exc) {
            GlobalCommons.LOG.log(Level.WARNING, " LOCALE : $GlobalCommons.curLocale", exc)
        }
    }
    static final char DOUBLE_QUOTE = '"' as char;
    static final char SIMPLE_QUOTE = '\'' as char;
    static final char BACKSLASH = '\\' as char;


    Reduction parseCST(SourceUnit sourceUnit, Reader reader) throws CompilationFailedException {
        if (dictionary == null) {
            super.parseCST(sourceUnit, reader)
        }
        def text = modifyTextSource(reader.text)
        StringReader stringReader = new StringReader(text)
        super.parseCST(sourceUnit, stringReader)
    }

    String modifyTextSource(text) {
        def lines = text.tokenize('\n')*.trim()
        def modified = lines.collect { line ->
            translate(line)
        }.join('\n')
        //println modified
        modified
    }

    /*
    */

    String translate(String model) {
        StringBuilder strb = new StringBuilder();
        boolean inString = false
        boolean doubleQuotedString = false
        char[] charArray = model.toCharArray()
        int max = charArray.length
        int index = 0
        while (index < max) {
            char curChar = charArray[index]
            if (!inString) { // Not inside String
                //Do we start an identifier?
                int accumulate = 0
                if (Character.isJavaIdentifierStart(curChar)) {
                    accumulate = 1
                }
                int specialChars = startOfSpecial(charArray, index)
                if (specialChars != 0) {
                    accumulate = specialChars
                }
                // then we accumulate and translate
                if (accumulate != 0) {
                    index = parseWord(strb, charArray, index, accumulate)
                } else {// not in an identifier
                    if (curChar == DOUBLE_QUOTE) {
                        inString = true
                        doubleQuotedString = true
                    } else if (curChar == SIMPLE_QUOTE) {
                        inString = true
                        doubleQuotedString = false
                    }
                    strb.append(curChar)
                    index++
                }
            } else { // inside String
                // we just try to get know if String ends
                // we accumulate
                // until end of String (but skip escaped endOfString)
                if (doubleQuotedString) {
                    if (DOUBLE_QUOTE == curChar) {
                        if (charArray[index - 1] != BACKSLASH) {
                            inString = false
                            doubleQuotedString = false
                        }
                    }

                } else { // simplequote
                    if (SIMPLE_QUOTE == curChar) {
                        if (charArray[index - 1] != BACKSLASH) {
                            inString = false
                            doubleQuotedString = false
                        }
                    }
                }
                strb.append(curChar)
                index++
            }
        }
        return strb.toString()
    }

    int parseWord(StringBuilder accumulator, char[] charArray, int index, int initialNumber) {
        StringBuilder wordBuilder = new StringBuilder()
        int curindex = index
        while (curindex < index + initialNumber) {
            wordBuilder.append(charArray[curindex])
            curindex++
        }
        while (curindex < charArray.length) {
            char curChar = charArray[curindex]
            int accumulate = 0;
            if (Character.isJavaIdentifierPart(curChar)) {
                accumulate = 1
            }
            int more = startOfSpecial(charArray, curindex)
            if (more != 0) {
                accumulate = more
            }
            if (accumulate == 0) break;
            for (int ix = 0; ix < accumulate; ix++) {
                wordBuilder.append(charArray[curindex])
                curindex++
            }
        }
        String toTranslate = wordBuilder.toString()
        //println toTranslate.length()
        String translated = dictionary.getProperty(toTranslate)
        if (translated == null) {
            accumulator.append(toTranslate)
            GlobalCommons.LOG.log(Level.FINE, "$toTranslate translates as $toTranslate")
        } else {
            GlobalCommons.LOG.log(Level.FINE, "$toTranslate translates as $translated")
            accumulator.append(translated)
        }
        return curindex
    }

    static final IntRange EMOTICONS = 0x1D000..0x1F77F
    static final IntRange MISCSYMBOLS = 0x2100..0x3299
    static final IntRange VARIATION_SELECTORS = 0xFE00..0xFE0F
    static final IntRange SUPPL_SYMBOLS = 0x1F900..0x1F9FF
    /*
   0x1F600...0x1F64F, // Emoticons
                 0x1F300...0x1F5FF, // Misc Symbols and Pictographs
                 0x1F680...0x1F6FF, // Transport and Map
                 0x2600...0x26FF,   // Misc symbols
                 0x2700...0x27BF,   // Dingbats
                 0xFE00...0xFE0F:   // Variation Selectors
     */

    int startOfSpecial(char[] array, int index) {
        int codePoint = Character.codePointAt(array, index)
        //println( Integer.toHexString(codePoint))
        if (isEmojiPart(codePoint)) {
            if (Character.isSurrogate(array[index])) return 2;
            return 1
        } else {
            if (Character.isSurrogate(array[index]) &&
                    Character.isJavaIdentifierPart(codePoint)) return 2
        }
        return 0
    }

    boolean isEmojiPart(int cp) {
        /*
          case 0x3030, 0x00AE, 0x00A9,// Special Characters
            0x1D000...0x1F77F,          // Emoticons
             0x2100...0x27BF,           // Misc symbols and Dingbats
            0xFE00...0xFE0F,            // Variation Selectors
            0x1F900...0x1F9FF:          // Supplemental Symbols and Pictographs
         */
        if (cp < 0x2100) return false
        //println "emoji $cp"
        return (
                //(cp== 0x3030) || // Wavy dash
                //(cp == 0x00AE) || // regsitered sign
                //(cp == 0x00A9) || // copyright sign
                // added this one "zero width joiner" already javaIdentifier part
                //(cp == 0x200D) ||
                (EMOTICONS.contains(cp)) ||
                        (MISCSYMBOLS.contains(cp)) ||
                        // already javaidentifier part
                        //(VARIATION_SELECTORS.contains(cp)) ||
                        (SUPPL_SYMBOLS.contains(cp))
        )
    }
}
