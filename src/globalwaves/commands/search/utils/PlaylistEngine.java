package globalwaves.commands.search.utils;

import globalwaves.commands.enums.FilterType;
import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.NameFilter;
import globalwaves.commands.search.utils.filters.OwnerFilter;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.library.LibraryInterrogator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlaylistEngine extends SearchEngine<Playlist> {
    private final String user;
    public PlaylistEngine(final Map<String, List<String>> filters, final String user) {
        super(filters);
        this.user = user;
    }

    /**
     * Collects the filters for a search command of type playlist.
     * @param filters The filters mapped. Only the tags filter can have multiple
     *                elements in it's list
     * @return A List with Filters, which can contain only NameFilter or OwnerFilter, or
     * both
     */
    @Override
    public List<Filter<Playlist>> collectFilters(final Map<String, List<String>> filters) {
        List<Filter<Playlist>> requestedFilters = new ArrayList<>();

        for (String key: filters.keySet()) {
            Filter<Playlist> newFilter = null;
            String value = filters.get(key).get(0);

            switch (Objects.requireNonNull(FilterType.parseString(key))) {
                case NAME -> newFilter = new NameFilter<>(value);
                case OWNER -> newFilter = new OwnerFilter<>(value);
                default -> {

                }

            }

            requestedFilters.add(newFilter);
        }

        return requestedFilters;
    }

    /**
     * Applies ALL filters on all the user available playlists (user private playlists,
     * and all the public playlists.
     * @return A List of Playlists that matched the filters
     */
    @Override
    public List<Playlist> collectResults() {
        LibraryInterrogator interrogator = new LibraryInterrogator();

        List<Playlist> matchedPlaylists = interrogator.getAvailablePlaylists(user);

        for (Filter<Playlist> filter : filters) {
            matchedPlaylists = applyFilter(matchedPlaylists, filter);
        }

        return matchedPlaylists;
    }


}
