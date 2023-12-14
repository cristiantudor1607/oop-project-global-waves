package globalwaves.commands.search.utils;

import globalwaves.commands.enums.FilterType;
import globalwaves.commands.search.utils.filters.DescriptionFilter;
import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.NameFilter;
import globalwaves.commands.search.utils.filters.OwnerFilter;
import globalwaves.player.entities.Album;
import globalwaves.player.entities.library.AdminBot;
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
