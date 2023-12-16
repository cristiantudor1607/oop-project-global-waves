package app.search.filters;

public interface Filter<T> {

    /**
     * A matching function, that compares this instance with an object based
     * on some field, provided in class implementation
     * @param matchingObject The object to be compared
     * @return true, if it matches, false otherwise
     */
    boolean  matches(T matchingObject);
}
