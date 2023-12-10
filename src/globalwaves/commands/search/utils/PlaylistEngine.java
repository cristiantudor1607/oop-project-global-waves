package globalwaves.commands.search.utils;

import globalwaves.commands.enums.FilterType;
import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.NameFilter;
import globalwaves.commands.search.utils.filters.OwnerFilter;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.library.SuperAdmin;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class PlaylistEngine extends SearchEngine<Playlist> {
    private final String user;
    public PlaylistEngine(final Map<String, List<String>> filters, final String user) {
        super(filters);
        this.user = user;
    }

    @Override
    public Filter<Playlist> getFilterByNameAsString(@NonNull String key,
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
     * Applies ALL filters on all the user available playlists (user private playlists,
     * and all the public playlists.
     * @return A List of Playlists that matched the filters
     */
    @Override
    public List<Playlist> collectResults() {
        SuperAdmin interrogator = new SuperAdmin();

        List<Playlist> matchedPlaylists = interrogator.getAvailablePlaylists(user);

        for (Filter<Playlist> filter : filters) {
            matchedPlaylists = applyFilter(matchedPlaylists, filter);
        }

        return matchedPlaylists;
    }


}
