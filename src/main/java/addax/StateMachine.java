package addax;


import java.util.Iterator;

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

    State<T> getState();

    Context<T> getContext();
}
