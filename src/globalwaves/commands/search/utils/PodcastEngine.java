package globalwaves.commands.search.utils;


import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.NameFilter;
import globalwaves.commands.search.utils.filters.OwnerFilter;
import globalwaves.commands.enums.FilterType;
import globalwaves.player.entities.Podcast;
import globalwaves.player.entities.library.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PodcastEngine extends SearchEngine<Podcast> {

    public PodcastEngine(Map<String, List<String>> filters) {
        super(filters);
    }

    @Override
    public List<Filter<Podcast>> collectFilters(Map<String, List<String>> filters) {
        List<Filter<Podcast>> requestedFilters = new ArrayList<>();

        for (String key: filters.keySet()) {
            Filter<Podcast> newFilter = null;
            String value = filters.get(key).get(0);

            switch (Objects.requireNonNull(FilterType.ParseString(key))) {
                case NAME -> newFilter = new NameFilter<>(value);
                case OWNER -> newFilter = new OwnerFilter<>(value);
            }

            requestedFilters.add(newFilter);
        }

        return requestedFilters;
    }

    @Override
    public List<Podcast> collectResults() {
        Library database = Library.getInstance();

        List<Podcast> matchedPodcasts = database.getPodcasts();

        for (Filter<Podcast> filter : filters)
            matchedPodcasts = applyFilter(matchedPodcasts, filter);

        return matchedPodcasts;
    }

}
