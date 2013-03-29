package addax;

/**
 * @author Mamad
 * @version 1.0
 * @since 28/03/13
 */
public interface Transition<T> {
    boolean accept(T input, Context<T> context);
}
