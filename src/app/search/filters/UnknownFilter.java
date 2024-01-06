package app.search.filters;

public class UnknownFilter<T> implements Filter<T>{
    /**
     * Returns true, so an unknown filter won't affect the searching.
     * @param matchingObject The object to be matched
     * @return {@code true}
     */
    @Override
    public boolean matches(T matchingObject) {
        return true;
    }
}
