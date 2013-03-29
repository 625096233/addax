package addax;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public interface StateMachine<T> {
    StateMachine<T> reset();

    StateMachine<T> read(T token) throws UnrecognizableInputException;

    StateMachine<T> read(T... tokens) throws UnrecognizableInputException;

    StateMachine<T> read(Iterable<T> tokens) throws UnrecognizableInputException;

    StateMachine<T> read(Iterator<T> tokens) throws UnrecognizableInputException;

    State<T> getStartState();

    Set<State<T>> getAllStates();

    Context<T> getContext();

    StateMachine<T> save(OutputStream out) throws IOException;

    StateMachine<T> load(InputStream in) throws IOException;
}
