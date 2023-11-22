package globalwaves.commands.search.utils;

import globalwaves.commands.search.utils.filters.*;
import globalwaves.commands.enums.FilterType;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.library.Library;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class SongEngine extends SearchEngine<Song> {

    public SongEngine(Map<String, List<String>> filters) {
        super(filters);
    }

    @Override
    public List<Filter<Song>> collectFilters(Map<String, List<String>> filters) {
         /*
        Create a list of filters that will be applied
         */
        List<Filter<Song>> requestedFilters = new ArrayList<>();
        for (String key: filters.keySet()) {
            Filter<Song> newFilter = null;
            List<String> values = filters.get(key);
            /*
            Create a specific filter, depending on the key
             */
            switch (Objects.requireNonNull(FilterType.ParseString(key))) {
                case NAME -> newFilter = new NameFilter<>(values.get(0));
                case ALBUM -> newFilter = new AlbumFilter(values.get(0));
                case ARTIST -> newFilter = new ArtistFilter(values.get(0));
                case LYRICS -> newFilter = new LyricsFilter(values.get(0));
                case TAGS -> newFilter = new TagsFilter(values);
                case GENRE -> newFilter = new GenreFilter(values.get(0));
                case RELEASE_YEAR -> newFilter = new ReleaseYearFilter(values.get(0));
            }

            requestedFilters.add(newFilter);
        }

        return requestedFilters;
    }

    @Override
    public List<Song> collectResults() {
        Library database = Library.getInstance();
        List<Song> matchedSongs = new ArrayList<>();

        for (Song s : database.getSongs())
            matchedSongs.add(new Song(s));

        for (Filter<Song> filter: filters)
            matchedSongs = applyFilter(matchedSongs, filter);

        return matchedSongs;
    }

}
