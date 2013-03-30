package addax.simple;

import addax.*;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class SimpleStateMachine implements Context<String>, StateMachine<String> {
    private static final int MAGIC = 9623;
    private Map<String, String> values = Maps.newHashMap();
    private Stack<String> stack = new Stack<String>();

    private State<String> startState, currentState;

    @Override
    public SimpleStateMachine set(String key, String value) {
        values.put(key, value);
        return this;
    }

    @Override
    public SimpleStateMachine push(String value) {
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
    public SimpleStateMachine clear() {
        values.clear();
        stack.clear();
        return this;
    }

    public SimpleStateMachine(SimpleState start) {
        this.currentState = this.startState = start;
    }

    public SimpleStateMachine read(String... words) throws UnrecognizableInputException {
        for (String word : words) {
            currentState = currentState.on(word, this);
        }
        return this;
    }

    public SimpleStateMachine read(Iterable<String> words) throws UnrecognizableInputException {
        for (String word : words) {
            currentState = currentState.on(word, this);
        }
        return this;
    }

    public SimpleStateMachine read(Iterator<String> words) throws UnrecognizableInputException {
        while (words.hasNext()) {
            String word = words.next();
            currentState = currentState.on(word, this);
        }
        return this;
    }

    public SimpleStateMachine read(String word) throws UnrecognizableInputException {
        currentState = currentState.on(word, this);
        return this;
    }

    public SimpleStateMachine reset() {
        clear();
        this.currentState = startState;
        return this;
    }

    public State<String> getStartState() {
        return currentState;
    }

    public SimpleStateMachine getContext() {
        return this;
    }

    @Override
    public Set<State<String>> getAllStates() {
        Set<State<String>> states = Sets.newHashSet();
        addStates(startState, states);
        return states;
    }

    private void addStates(State<String> state, Set<State<String>> set) {
        if (!set.contains(state)) {
            set.add(state);
            for (State<String> another : state.getTransitionsMap().values()) {
                addStates(another, set);
            }
        }
    }

    @Override
    public StateMachine<String> save(OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeInt(MAGIC);//magic
        oos.writeObject(startState);
        oos.flush();
        return this;
    }

    @Override
    public StateMachine<String> load(InputStream in) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(in);
        int magic = ois.readInt();
        if (magic != MAGIC) {
            throw new IOException("Input stream is corrupted.");
        }
        try {
            startState = (State<String>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Error in reading input stream.", e);
        }
        return this;
    }

    /**
     * Generate dot graph output for GraphViz
     *
     * @param writer writer to write the graph string into
     * @return this
     */
    public StateMachine<String> writeDotGraph(Writer writer) throws IOException {
        writer.write("digraph G {\n");
        Set<State<String>> states = getAllStates();
        for (State<String> state : states) {
            Map<Transition<String>, State<String>> map = state.getTransitionsMap();
            for (Map.Entry<Transition<String>, State<String>> entry : map.entrySet()) {
                writer.write(state.toString().replaceAll("-", "_"));
                writer.write(" -> ");
                writer.write(entry.getValue().toString().replaceAll("-", "_"));
                writer.write(" [label=\"");
                writer.write(entry.getKey().toString());
                writer.write("\"];\n");
            }
        }
        writer.write("\n}");
        writer.flush();
        return this;
    }
}
