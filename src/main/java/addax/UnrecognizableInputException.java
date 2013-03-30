package addax;

/**
 * @author Mamad
 * @version 1.0
 * @since 29/03/13
 */
public class UnrecognizableInputException extends Exception {
    private static final long serialVersionUID = 1l;

    public UnrecognizableInputException(String message) {
        super(message);
    }
}
