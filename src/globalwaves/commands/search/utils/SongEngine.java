package globalwaves.commands.search.utils;

import globalwaves.commands.enums.FilterType;
import globalwaves.commands.search.utils.filters.*;
import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.library.Library;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SongEngine extends SearchEngine<Song> {

    public SongEngine(Map<String, List<String>> filters) {
        super(filters);
    }


    public List<Song> findInDatabase(final List<Song> providedDatabase) {
        List<Song> matchedSongs = providedDatabase;

        for (Filter<Song> filter: filters)
            matchedSongs = applyFilter(matchedSongs, filter);

        return matchedSongs;
    }

    @Override
    public Filter<Song> getFilterByNameAsString(@NonNull final String key,
                                                @NonNull final List<String> values) {
        if (values.isEmpty())
            return null;

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

    @Override
    public List<Song> collectResults() {
        List<Song> results = new ArrayList<>();

        List<Song> preloadedSongs = Library.getInstance().getPreLoadedSongs();
        results.addAll(findInDatabase(preloadedSongs));

        Map<String, List<Song>> addedSongs = Library.getInstance().getAddedSongs();
        for (List<Song> value: addedSongs.values()) {
            results.addAll(findInDatabase(value));
        }

        return results;
    }

}
