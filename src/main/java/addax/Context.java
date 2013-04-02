package addax;

import java.util.Map;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public interface Context<T> {
    Context<T> set(T key, T value);

    Context<T> push(T input);

    Context<T> clear();

    T pop();

    Map<T, T> getValues();
}
