package globalwaves.commands.search.utils;

import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public abstract class SearchEngine <T extends PlayableEntity> {
    protected List<Filter<T>> filters;

    public SearchEngine(Map<String, List<String>> filters) {
        this.filters = collectFilters(filters);
    }

    public List<T> applyFilter(List<T> entitiesList, Filter<T> filter) {
        List<T> matchedEntity = new ArrayList<>();

        for (T entity : entitiesList) {
            if (filter.matches(entity))
                matchedEntity.add(entity);
        }

        return matchedEntity;
    }

    abstract public List<Filter<T>> collectFilters(Map<String, List<String>> filters);
    abstract public List<T> collectResults();

}
