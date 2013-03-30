package addax.simple;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

/**
 * @author Mamad
 * @version 1.0
 * @since 30/03/13
 */
public interface Tokeniser {
    Iterable<String> tokenise(String text);

    static Tokeniser DEFAULT = new BasicTokeniser();

    public static class BasicTokeniser implements Tokeniser {

        public static final Splitter SPLITTER = Splitter
                .on(CharMatcher.anyOf("\t ,;:?"))
                .trimResults()
                .omitEmptyStrings();

        @Override
        public Iterable<String> tokenise(String text) {
            if (text.charAt(text.length() - 1) == '.') {
                text = text.substring(0, text.length() - 1);
            }
            return SPLITTER.split(text);
        }
    }
}
