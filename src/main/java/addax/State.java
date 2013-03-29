package addax;

import java.util.Map;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public interface State<T> {
    /**
     * Configure move to target state on accepted transition
     *
     * @param transition input transition
     * @param target     target state
     * @return this
     */
    State<T> move(Transition<T> transition, State<T> target);

    /**
     * Skip to the current state on the input transition
     *
     * @param transition transition to skip
     * @return this
     */
    State<T> skip(Transition<T> transition);

    /**
     * Remove a transition from this state
     *
     * @param transition transition to be removed
     * @return this
     */
    State<T> remove(Transition<T> transition);

    /**
     * Handle input token
     *
     * @param token   input token
     * @param context state machine context
     * @return this
     */
    State<T> on(T token, Context<T> context) throws UnrecognizableInputException;

    Map<Transition<T>, State<T>> getTransitionsMap();
}
