package app.search.engines;

import app.enums.FilterType;
import app.search.filters.Filter;
import app.search.filters.NameFilter;
import app.search.filters.OwnerFilter;
import app.player.entities.Playlist;
import app.users.AdminBot;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class PlaylistEngine extends SearchEngine<Playlist> {
    private final String user;
    public PlaylistEngine(final Map<String, List<String>> filters, final String user) {
        super(filters);
        this.user = user;
    }

    /**
     * Converts a (key, value) pair into a filter.
     * @param key The name of the filter
     * @param values The patterns of the filters
     * @return A specific filter, based on {@code key}'s value
     */
    @Override
    public Filter<Playlist> getFilterByNameAsString(@NonNull final String key,
                                                    @NonNull final List<String> values) {
        if (values.isEmpty()) {
            return null;
        }

        return switch (FilterType.parseString(key)) {
            case NAME -> new NameFilter<>(values.get(0));
            case OWNER -> new OwnerFilter<>(values.get(0));
            default -> null;
        };
    }

    /**
     * Applies all {@code filters} on a list that contains all playlists from database.
     * @return A list of matched playlists
     */
    @Override
    public List<Playlist> collectResults() {
        AdminBot adminBot = new AdminBot();

        List<Playlist> matchedPlaylists = adminBot.getAvailablePlaylists(user);

        for (Filter<Playlist> filter : filters) {
            matchedPlaylists = applyFilter(matchedPlaylists, filter);
        }

        return matchedPlaylists;
    }


}
