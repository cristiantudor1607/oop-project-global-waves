package globalwaves.commands.search.utils;

import globalwaves.commands.enums.FilterType;
import globalwaves.commands.enums.SearchType;
import globalwaves.commands.search.utils.filters.ArtistPageFilter;
import globalwaves.commands.search.utils.filters.Filter;
import globalwaves.commands.search.utils.filters.HostPageFilter;
import globalwaves.player.entities.library.Library;
import globalwaves.player.entities.paging.Page;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class PageFinder extends SearchEngine<Page> {
    private final SearchType.Type typeOfUser;

    public PageFinder(final Map<String, List<String>> filters,
                      final SearchType.Type typeOfUser) {
        this.typeOfUser = typeOfUser;
        this.filters = collectFilters(filters);
    }

    @Override
    public Filter<Page> getFilterByNameAsString(@NonNull String key, @NonNull List<String> values) {
        if (values.isEmpty())
            return null;

        FilterType.Type filterType = FilterType.parseString(key, typeOfUser);

        if (filterType == FilterType.Type.ARTIST_USERNAME)
            return new ArtistPageFilter(values.get(0));

        if (filterType == FilterType.Type.HOST_USERNAME)
            return new HostPageFilter(values.get(0));

        return null;
    }

    public List<Page> findInDatabase(final List<Page> providedPages) {
        List<Page> pages = providedPages;

        for (Filter<Page> filter: filters)
            pages = applyFilter(pages, filter);

        return pages;
    }

    @Override
    public List<Page> collectResults() {
        Library database = Library.getInstance();

        if (typeOfUser == SearchType.Type.ARTIST)
            return findInDatabase(database.getArtistPages());

        if (typeOfUser == SearchType.Type.HOST)
            return findInDatabase(database.getHostPages());

        return null;
    }
}
