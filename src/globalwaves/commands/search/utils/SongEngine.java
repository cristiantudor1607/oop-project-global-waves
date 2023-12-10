package globalwaves.commands.search.utils;

import globalwaves.commands.enums.FilterType;
import globalwaves.commands.search.utils.filters.*;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.library.Library;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class SongEngine extends SearchEngine<Song> {

    public SongEngine(Map<String, List<String>> filters) {
        super(filters);
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
        Library database = Library.getInstance();
        List<Song> matchedSongs = database.getSongs();

        for (Filter<Song> filter: filters)
            matchedSongs = applyFilter(matchedSongs, filter);

        return matchedSongs;
    }

}
