package app.search.engines;

import app.enums.FilterType;
import app.search.filters.DescriptionFilter;
import app.search.filters.Filter;
import app.search.filters.NameFilter;
import app.search.filters.OwnerFilter;
import app.player.entities.Album;
import app.users.AdminBot;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class AlbumEngine extends SearchEngine<Album> {
    public AlbumEngine(final Map<String, List<String>> filters) {
        super(filters);
    }

    /**
     * Converts a (key, value) pair into a filter.
     * @param key The name of the filter
     * @param values The patterns of the filters
     * @return A specific filter, based on {@code key}'s value
     */
    @Override
    public Filter<Album> getFilterByNameAsString(@NonNull final String key,
                                                 @NonNull final List<String> values) {
        if (values.isEmpty()) {
            return null;
        }

        return switch (FilterType.parseString(key)) {
            case NAME -> new NameFilter<>(values.get(0));
            case OWNER -> new OwnerFilter<>(values.get(0));
            case DESCRIPTION -> new DescriptionFilter(values.get(0));
            default -> null;
        };
    }

    /**
     * Applies all {@code filters} on a list that contains all albums from library.
     * @return A list of matched albums
     */
    @Override
    public List<Album> collectResults() {
        AdminBot adminBot = new AdminBot();

        List<Album> matchedAlbums = adminBot.getAllAlbums();

        for (Filter<Album> filter : filters) {
            matchedAlbums = applyFilter(matchedAlbums, filter);
        }

        return matchedAlbums;
    }
}
