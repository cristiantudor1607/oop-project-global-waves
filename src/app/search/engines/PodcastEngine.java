package app.search.engines;


import app.enums.FilterType;
import app.search.filters.Filter;
import app.search.filters.NameFilter;
import app.search.filters.OwnerFilter;
import app.player.entities.Podcast;
import app.management.Library;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PodcastEngine extends SearchEngine<Podcast> {

    public PodcastEngine(final Map<String, List<String>> filters) {
        super(filters);
    }

    /**
     * Converts a (key, value) pair into a filter.
     * @param key The name of the filter
     * @param values The patterns of the filters
     * @return A specific filter, based on {@code key}'s value
     */
    @Override
    public Filter<Podcast> getFilterByNameAsString(@NonNull final String key,
                                                   @NonNull final List<String> values) {
        Filter<Podcast> result = null;
        if (!values.isEmpty()) {
            result = switch (FilterType.parseString(key)) {
                case NAME -> new NameFilter<>(values.get(0));
                case OWNER -> new OwnerFilter<>(values.get(0));
                default -> null;
            };
        }

        return result;
    }

    /**
     * Applies all {@code filters} on the list that contains all podcasts from
     * database.
     * @return A list of matched podcasts
     */
    @Override
    public List<Podcast> collectResults() {
        Library database = Library.getInstance();
        List<Podcast> matchedPodcasts = new ArrayList<>(database.getPodcasts());

        for (List<Podcast> podcasts: database.getAddedPodcasts().values()) {
            matchedPodcasts.addAll(podcasts);
        }


        for (Filter<Podcast> filter : filters) {
            matchedPodcasts = applyFilter(matchedPodcasts, filter);
        }

        return matchedPodcasts;
    }

}
