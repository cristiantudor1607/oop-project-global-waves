package app.properties;

public interface Visitable {
    /**
     * Accepts the visitor and returns a string.
     * @param v The visitor
     * @return A string containing relevant data
     */
    String accept(Visitor v);
}
