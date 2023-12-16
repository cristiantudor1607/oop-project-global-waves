package app.properties;

public interface Visitable {
    String accept(Visitor v);
}
