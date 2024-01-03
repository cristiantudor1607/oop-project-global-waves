package app.search.engines;

import app.enums.FilterType;
import app.player.entities.Song;
import app.management.Library;
import app.search.filters.AlbumFilter;
import app.search.filters.ArtistFilter;
import app.search.filters.Filter;
import app.search.filters.GenreFilter;
import app.search.filters.LyricsFilter;
import app.search.filters.NameFilter;
import app.search.filters.ReleaseYearFilter;
import app.search.filters.TagsFilter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SongEngine extends SearchEngine<Song> {

    public SongEngine(final Map<String, List<String>> filters) {
        super(filters);
    }

    /**
     * Returns a list of matched songs from the given list.
     * @param providedList The list of songs to be matched
     * @return A new list, that contains the matched songs. There is no modification on the
     * given list.
     */
    public List<Song> findInList(final List<Song> providedList) {
        List<Song> matchedSongs = providedList;

        for (Filter<Song> filter: filters) {
            matchedSongs = applyFilter(matchedSongs, filter);
        }

        return matchedSongs;
    }

    /**
     * Converts a (key, value) pair into a filter.
     * @param key The name of the filter
     * @param values The patterns of the filters
     * @return A specific filter, based on {@code key}'s value
     */
    @Override
    public Filter<Song> getFilterByNameAsString(@NonNull final String key,
                                                @NonNull final List<String> values) {
        if (values.isEmpty()) {
            return null;
        }

        return switch (FilterType.parseString(key)) {
            case NAME -> new NameFilter<>(values.get(0));
            case ALBUM -> new AlbumFilter(values.get(0));
            case ARTIST -> new ArtistFilter(values.get(0));
            case LYRICS -> new LyricsFilter(values.get(0));
            case TAGS -> new TagsFilter(values);
            case GENRE -> new GenreFilter(values.get(0));
            case RELEASE_YEAR -> new ReleaseYearFilter(values.get(0));
            default -> null;
        };
    }

    /**
     * Applies all {@code filters} on a list that contains all the songs from database.
     * It calls {@code findInList} multiple times.
     * @return A list of matched songs
     */
    @Override
    public List<Song> collectResults() {
        List<Song> results = new ArrayList<>();

        List<Song> preloadedSongs = Library.getInstance().getSongs();
        results.addAll(findInList(preloadedSongs));

        Map<String, List<Song>> addedSongs = Library.getInstance().getAddedSongs();
        for (List<Song> value: addedSongs.values()) {
            results.addAll(findInList(value));
        }

        return results;
    }

}
