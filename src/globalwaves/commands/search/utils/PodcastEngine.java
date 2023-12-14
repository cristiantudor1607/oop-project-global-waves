package globalwaves.commands.search.utils;


import globalwaves.commands.enums.FilterType;
import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.NameFilter;
import globalwaves.commands.search.utils.filters.OwnerFilter;
import globalwaves.player.entities.Podcast;
import globalwaves.player.entities.library.Library;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PodcastEngine extends SearchEngine<Podcast> {

    public PodcastEngine(final Map<String, List<String>> filters) {
        super(filters);
    }

    @Override
    public Filter<Podcast> getFilterByNameAsString(@NonNull String key,
                                                   @NonNull List<String> values) {
        if (values.isEmpty())
            return null;

        return switch (FilterType.parseString(key)) {
            case NAME -> new NameFilter<>(values.get(0));
            case OWNER -> new OwnerFilter<>(values.get(0));
            default -> null;
        };
    }

    /**
     * Applies ALL filters on library entities. It starts with the list of all podcasts
     * from the library , and filter through them
     * @return A List of entities of the same type, that matched the filters
     */
    @Override
    public List<Podcast> collectResults() {
        Library database = Library.getInstance();
        List<Podcast> matchedPodcasts = new ArrayList<>(database.getPodcasts());

        for (List<Podcast> podcasts: database.getAddedPodcasts().values())
            matchedPodcasts.addAll(podcasts);


        for (Filter<Podcast> filter : filters) {
            matchedPodcasts = applyFilter(matchedPodcasts, filter);
        }

        return matchedPodcasts;
    }

}
