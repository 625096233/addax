package addax.primitive.string;

import addax.Context;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Stack;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class StringContext implements Context<String> {
    private Map<String, String> values = Maps.newHashMap();
    private Stack<String> stack = new Stack<String>();

    @Override
    public StringContext set(String key, String value) {
        values.put(key, value);
        return this;
    }

    @Override
    public StringContext push(String value) {
        stack.push(value);
        return this;
    }

    @Override
    public String pop() {
        return stack.pop();
    }

    public Map<String, String> getValues() {
        return values;
    }

    @Override
    public StringContext clear() {
        values.clear();
        stack.clear();
        return this;
    }
}
