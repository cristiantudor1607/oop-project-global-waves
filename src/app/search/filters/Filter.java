package app.search.filters;

public interface Filter<T> {

    /**
     * It checks if the object is matching some criteria. The criteria can be anything,
     * it should be defined in the child class.
     * @param matchingObject The object to be matched
     * @return true, if it matches, false otherwise
     */
    boolean  matches(T matchingObject);
}
