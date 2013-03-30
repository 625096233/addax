package addax.simple;

import addax.Action;
import addax.Context;

import java.io.Serializable;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public abstract class SimpleAction implements Action<String>, Serializable {
    private static final long serialVersionUID = 1l;

    /**
     * Pop a string from the context stack and prepend it to the current token
     *
     * @return pop and prepend action
     */
    public static SimpleAction popAndPrepend(final String key) {
        return new PopAndPrependAction(key);
    }

    /**
     * push the current value into the context stack
     *
     * @return push action
     */
    public static SimpleAction push() {
        return new PushAction();
    }

    /**
     * Store the current token with the given key in the context
     *
     * @param key the key for storing current token
     * @return this
     */
    public static SimpleAction setAs(final String key) {
        return new SetAsAction(key);
    }

    /**
     * Store the key value pair in the context
     *
     * @param key   the item key
     * @param value the item value
     * @return this
     */
    public static SimpleAction set(String key, String value) {
        return new SetValueAction(key, value);
    }

    private static class SetValueAction extends SimpleAction {
        private static final long serialVersionUID = 1l;
        String key, value;

        private SetValueAction(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            context.set(key, value);
            return this;
        }
    }

    private static class PopAndPrependAction extends SimpleAction {
        private static final long serialVersionUID = 1l;
        private final String key;

        public PopAndPrependAction(String key) {
            this.key = key;
        }

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            context.set(key, context.pop() + " " + word);
            return this;
        }
    }

    private static class PushAction extends SimpleAction {
        private static final long serialVersionUID = 1l;

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            context.push(word);
            return this;
        }
    }

    private static class SetAsAction extends SimpleAction {
        private static final long serialVersionUID = 1l;
        private final String key;

        public SetAsAction(String key) {
            this.key = key;
        }

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            context.set(key, word);
            return this;
        }
    }
}
