package addax.simple;

import addax.Context;
import addax.State;
import addax.Transition;
import addax.UnrecognizableInputException;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * A string state with unique name.
 *
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class SimpleState implements State<String>, Serializable {
    private static final long serialVersionUID = 1l;

    private Map<Transition<String>, State<String>> transitionsMap = Maps.newHashMap();
    private final String name;

    public static SimpleState newState(String name) {
        return new SimpleState(name);
    }

    /**
     * Create a new state with the given name
     *
     * @param name name should be unique.
     */
    public SimpleState(String name) {
        this.name = name;
    }

    @Override
    public SimpleState skip(Transition<String> transition) {
        transitionsMap.put(transition, this);
        return this;
    }

    @Override
    public SimpleState remove(Transition<String> transition) {
        transitionsMap.remove(transition);
        return this;
    }

    @Override
    public SimpleState move(Transition<String> transition, State<String> target) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleState that = (SimpleState) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
