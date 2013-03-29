package addax;

/**
 * @author Mamad
 * @version 1.0
 * @since 28/03/13
 */
public interface Action<T> {
    Action<T> execute(T input, Context<T> context);
}
