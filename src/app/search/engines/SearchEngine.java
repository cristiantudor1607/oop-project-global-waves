package app.search.engines;


import app.search.filters.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public abstract class SearchEngine<T> {
    protected List<Filter<T>> filters;

    public SearchEngine() { }

    public SearchEngine(final Map<String, List<String>> filters) {
        this.filters = collectFilters(filters);
    }

    /**
     * Makes a list of Filters based on Map previously returned by a method that
     * map the categories.
     * @param mappedFilters The mapped filters. Only the first element of the List
     *                      is used, if the key isn't "{@code tags}". For "{@code tags}"
     *                      all elements are used
     * @return A list of filters
     */
    public List<Filter<T>> collectFilters(final Map<String, List<String>> mappedFilters) {
        List<Filter<T>> requestedFilters = new ArrayList<>();
        for (String key: mappedFilters.keySet()) {
            List<String> values = mappedFilters.get(key);

            Filter<T> newFilter = getFilterByNameAsString(key, values);
            requestedFilters.add(newFilter);
        }

        return requestedFilters;
    }

    /**
     * Applies the filter and returns a list of results
     * @param entitiesList The list of entities to be filtered
     * @param filter The filter to be applied
     * @return A list with the objects that matched the filter
     */
    public List<T> applyFilter(final List<T> entitiesList, final Filter<T> filter) {
        List<T> matchedEntity = new ArrayList<>();

        for (T entity : entitiesList) {
            if (filter.matches(entity)) {
                matchedEntity.add(entity);
            }
        }

        return matchedEntity;
    }

    /**
     * Converts a (key, value) pair into a filter.
     * @param key The name of the filter
     * @param values The patterns of the filters
     * @return A specific filter, based on {@code key}'s value
     */
    public abstract Filter<T> getFilterByNameAsString(@NonNull String key,
                                                      @NonNull List<String> values);
    /**
     * Applies all {@code filters} on a list of library entities. The list
     * is provided from database, and it depends on the type {@code T}.
     * @return A list of matched entities
     */
    public abstract List<T> collectResults();

}
