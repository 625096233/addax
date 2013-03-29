package addax.primitive.string;

import addax.Context;
import addax.State;
import addax.Transition;
import addax.UnrecognizableInputException;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class StringState implements State<String> {
    private Map<Transition<String>, State<String>> transitionsMap = Maps.newHashMap();
    private final String name;

    public static StringState newState(String name) {
        return new StringState(name);
    }

    public StringState(String name) {
        this.name = name;
    }

    @Override
    public StringState skip(Transition<String> transition) {
        transitionsMap.put(transition, this);
        return this;
    }

    @Override
    public StringState remove(Transition<String> transition) {
        transitionsMap.remove(transition);
        return this;
    }

    @Override
    public StringState move(Transition<String> transition, State<String> target) {
        transitionsMap.put(transition, target);
        return this;
    }

    @Override
    public Map<Transition<String>, State<String>> getTransitionsMap() {
        return Collections.unmodifiableMap(transitionsMap);
    }

    @Override
    public State<String> on(String word, Context<String> context) throws UnrecognizableInputException {
        for (Map.Entry<Transition<String>, State<String>> entry : transitionsMap.entrySet()) {
            Transition<String> transition = entry.getKey();
            if (transition.accept(word, context)) {
                return entry.getValue();
            }
        }
        throw new UnrecognizableInputException("Unrecognizable input token:" + word);
    }

    @Override
    public String toString() {
        return name;
    }

}
