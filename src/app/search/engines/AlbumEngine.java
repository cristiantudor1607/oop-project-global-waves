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

public class AlbumEngine extends SearchEngine<Album>{
    public AlbumEngine(Map<String, List<String>> filters) {
        super(filters);
    }

    @Override
    public Filter<Album> getFilterByNameAsString(@NonNull String key,
                                                 @NonNull List<String> values) {
        if (values.isEmpty())
            return null;

        return switch (FilterType.parseString(key)) {
            case NAME -> new NameFilter<>(values.get(0));
            case OWNER -> new OwnerFilter<>(values.get(0));
            case DESCRIPTION -> new DescriptionFilter(values.get(0));
            default -> null;
        };
    }

    @Override
    public List<Album> collectResults() {
        AdminBot adminBot = new AdminBot();

        List<Album> matchedAlbums = adminBot.getAllAlbums();

        for (Filter<Album> filter : filters)
            matchedAlbums = applyFilter(matchedAlbums, filter);

        return matchedAlbums;
    }
}
