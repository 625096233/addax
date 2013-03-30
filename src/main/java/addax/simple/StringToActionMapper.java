package addax.simple;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Map action strings to SimpleAction instances, this class maps static strings, use RegExStringToActionMapper for
 * generic action mapping.
 *
 * @author Mamad
 * @version 1.0
 * @since 30/03/13
 */
public class StringToActionMapper implements Function<String, SimpleAction> {
    private Map<String, SimpleAction> word2ActionMap = Maps.newHashMap();

    @Override
    public SimpleAction apply(@Nullable String input) {
        if (input != null) {
            if (!word2ActionMap.containsKey(input)) {
                throw new RuntimeException("Unmapped action string: " + input);
            }
            return word2ActionMap.get(input);
        }
        return null;
    }

    public void register(String word, SimpleAction action) {
        word2ActionMap.put(word, action);
    }
}
