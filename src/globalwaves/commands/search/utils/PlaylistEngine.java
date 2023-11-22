package globalwaves.commands.search.utils;

import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.NameFilter;
import globalwaves.commands.search.utils.filters.OwnerFilter;
import globalwaves.commands.enums.FilterType;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.library.Library;
import globalwaves.player.entities.library.LibraryInterrogator;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlaylistEngine extends SearchEngine<Playlist>{
    private final String user;
    public PlaylistEngine(Map<String, List<String>> filters, final String user) {
        super(filters);
        this.user = user;
    }

    @Override
    public List<Filter<Playlist>> collectFilters(Map<String, List<String>> filters) {
        List<Filter<Playlist>> requestedFilters = new ArrayList<>();

        for (String key: filters.keySet()) {
            Filter<Playlist> newFilter = null;
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
    public List<Playlist> collectResults() {
        LibraryInterrogator interrogator = new LibraryInterrogator();

        List<Playlist> matchedPlaylists = interrogator.getAvailablePlaylists(user);

        for (Filter<Playlist> filter : filters)
            matchedPlaylists = applyFilter(matchedPlaylists, filter);

        return matchedPlaylists;
    }


}
