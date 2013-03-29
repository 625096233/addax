package addax.primitive.string;

import addax.Context;
import addax.State;
import addax.StateMachine;
import addax.UnrecognizableInputException;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class StringStateMachine implements Context<String>, StateMachine<String> {
    private Map<String, String> values = Maps.newHashMap();
    private Stack<String> stack = new Stack<String>();

    private State<String> startState, currentState;

    @Override
    public StringStateMachine set(String key, String value) {
        values.put(key, value);
        return this;
    }

    @Override
    public StringStateMachine push(String value) {
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
    public StringStateMachine clear() {
        values.clear();
        stack.clear();
        return this;
    }

    public StringStateMachine(StringState start) {
        this.currentState = this.startState = start;
    }

    public StringStateMachine read(String... words) throws UnrecognizableInputException {
        for (String word : words) {
            currentState = currentState.on(word, this);
        }
        return this;
    }

    public StringStateMachine read(Iterable<String> words) throws UnrecognizableInputException {
        for (String word : words) {
            currentState = currentState.on(word, this);
        }
        return this;
    }

    public StringStateMachine read(Iterator<String> words) throws UnrecognizableInputException {
        while (words.hasNext()) {
            String word = words.next();
            currentState = currentState.on(word, this);
        }
        return this;
    }

    public StringStateMachine read(String word) throws UnrecognizableInputException {
        currentState = currentState.on(word, this);
        return this;
    }

    public StringStateMachine reset() {
        clear();
        this.currentState = startState;
        return this;
    }

    public State<String> getState() {
        return currentState;
    }

    public StringStateMachine getContext() {
        return this;
    }

}
