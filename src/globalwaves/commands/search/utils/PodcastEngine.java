package globalwaves.commands.search.utils;


import globalwaves.commands.enums.FilterType;
import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.NameFilter;
import globalwaves.commands.search.utils.filters.OwnerFilter;
import globalwaves.player.entities.Podcast;
import globalwaves.player.entities.library.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PodcastEngine extends SearchEngine<Podcast> {

    public PodcastEngine(final Map<String, List<String>> filters) {
        super(filters);
    }

    /**
     * Method that acts like a converter, it gets a (key, value) pair from the mapped
     * filters Map, and gets a new Filter, specialised on matching by category specified
     * in key String (name or owner)
     * @param key The category that specifies which type of filter should be created
     * @param value The reference value of the filter
     * @return The new specialised Filter
     */
    public Filter<Podcast> convertToFilter(final String key, final String value) {
        switch (Objects.requireNonNull(FilterType.parseString(key))) {
            case NAME -> {
                return new NameFilter<>(value);
            }
            case OWNER -> {
                return new OwnerFilter<>(value);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Collects the filters for a search command of type podcast.
     * @param filters The filters mapped. Only the tags filter can have multiple
     *                elements in it's list
     * @return A List with Filters, which can contain only NameFilter or OwnerFilter, or
     * both
     */
    @Override
    public List<Filter<Podcast>> collectFilters(final Map<String, List<String>> filters) {
        List<Filter<Podcast>> requestedFilters = new ArrayList<>();

        for (String key: filters.keySet()) {
            String value = filters.get(key).get(0);

            Filter<Podcast> newFilter = convertToFilter(key, value);

            requestedFilters.add(newFilter);
        }

        return requestedFilters;
    }

    /**
     * Applies ALL filters on library entities. It starts with the list of all podcasts
     * from the library , and filter through them
     * @return A List of entities of the same type, that matched the filters
     */
    @Override
    public List<Podcast> collectResults() {
        Library database = Library.getInstance();

        List<Podcast> matchedPodcasts = database.getPodcasts();

        for (Filter<Podcast> filter : filters) {
            matchedPodcasts = applyFilter(matchedPodcasts, filter);
        }

        return matchedPodcasts;
    }

}
