package addax.simple;

import addax.Action;
import addax.Context;

import java.io.Serializable;
import java.util.EmptyStackException;

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
     * pop a string from context stack and put it as key in the context values
     *
     * @return pop action
     */
    public static SimpleAction pop(String key) {
        return new PopAction(key);
    }

    /**
     * pop all strings from context stack, concatenate them and put it as key in the context values
     *
     * @return pop action
     */
    public static SimpleAction popAll(String key) {
        return new PopAllAction(key);
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

    public static class PopAction extends SimpleAction {
        private static final long serialVersionUID = 1l;
        protected final String key;

        public PopAction(String key) {
            this.key = key;
        }

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            context.set(key, context.pop());
            return this;
        }
    }

    public static class PopAllAction extends PopAction {
        private static final long serialVersionUID = 1l;
        public PopAllAction(String key) {
            super(key);
        }

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            String popStr = "", token;
            try {
                while ((token = context.pop()) != null) {
                    popStr = token + " " + popStr;//prepend the token to the result
                }
            } catch (EmptyStackException e) {
                //ignored
            }

            if (popStr.length() > 1) {
                context.set(key, popStr.substring(0, popStr.length()-1));
            } else {
                context.set(key, popStr);
            }
            return this;
        }
    }

    public static class PopAndPrependAction extends PopAction {
        private static final long serialVersionUID = 1l;

        public PopAndPrependAction(String key) {
            super(key);
        }

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            context.set(key, context.pop() + " " + word);
            return this;
        }
    }

    public static class PushAction extends SimpleAction {
        private static final long serialVersionUID = 1l;

        @Override
        public SimpleAction execute(String word, Context<String> context) {
            context.push(word);
            return this;
        }
    }

    public static class SetAsAction extends SimpleAction {
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
