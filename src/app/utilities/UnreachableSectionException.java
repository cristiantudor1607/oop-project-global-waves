package app.utilities;

public class UnreachableSectionException extends Exception{
    public UnreachableSectionException(final String errorMessage) {
        super(errorMessage);
    }
}
