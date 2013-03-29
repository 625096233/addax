package addax.primitive.string;

import addax.Action;
import addax.Context;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public abstract class StringAction implements Action<String> {
    /**
     * Pop a string from the context stack and prepend it to the current token
     *
     * @return pop and prepend action
     */
    public static StringAction popAndPrepend(final String key) {
        return new StringAction() {
            @Override
            public StringAction execute(String word, Context<String> context) {
                context.set(key, context.pop() + " " + word);
                return this;
            }
        };
    }

    /**
     * push the current value into the context stack
     *
     * @return push action
     */
    public static StringAction push() {
        return new StringAction() {
            @Override
            public StringAction execute(String word, Context<String> context) {
                context.push(word);
                return this;
            }
        };
    }

    /**
     * Store the current token with the given key in the context
     *
     * @param key the key for storing current token
     * @return this
     */
    public static StringAction setAs(final String key) {
        return new StringAction() {
            @Override
            public StringAction execute(String word, Context<String> context) {
                context.set(key, word);
                return this;
            }
        };
    }

    /**
     * Store the key value pair in the context
     *
     * @param key   the item key
     * @param value the item value
     * @return this
     */
    public static StringAction set(String key, String value) {
        return new SetValueAction(key, value);
    }

    private static class SetValueAction extends StringAction {
        String key, value;

        private SetValueAction(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public StringAction execute(String word, Context<String> context) {
            context.set(key, value);
            return this;
        }
    }

}
