package addax.simple;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Mamad
 * @version 1.0
 * @since 30/03/13
 */
public class RegExStringToActionMapper implements Function<String, SimpleAction> {
    private Map<Pattern, SimpleAction> word2ActionMap = Maps.newHashMap();

    @Override
    public SimpleAction apply(@Nullable String input) {
        if (input != null) {
            for (Map.Entry<Pattern, SimpleAction> entry : word2ActionMap.entrySet()) {
                if (entry.getKey().matcher(input).matches()) {
                    return entry.getValue();
                }
            }
            throw new RuntimeException("Unmapped action string: " + input);
        }
        return null;
    }

    public void register(String pattern, SimpleAction action) {
        word2ActionMap.put(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE), action);
    }
}
