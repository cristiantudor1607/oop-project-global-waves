package globalwaves.commands.search.utils;

import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public abstract class SearchEngine<T extends PlayableEntity> {
    protected List<Filter<T>> filters;

    public SearchEngine(final Map<String, List<String>> filters) {
        this.filters = collectFilters(filters);
    }

    /**
     * Applies the inner filters and returns a list of results
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
     * Makes a list of Filters based on Map previously returned by a method that
     * map the accepted categories
     * @param mappedFilters The filters mapped. Only the tags filter can have multiple
     *                elements in it's list
     * @return List of Filter objects. Basically is a conversion String - Filter
     */
    public abstract List<Filter<T>> collectFilters(Map<String, List<String>> mappedFilters);

    /**
     * Applies ALL filters on library entities. It starts with the list of all songs,
     * or all playlists, or all podcast, and filter through them
     * @return A List of entities of the same type, that matched the filters
     */
    public abstract List<T> collectResults();

}
