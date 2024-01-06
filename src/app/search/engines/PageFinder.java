package app.search.engines;

import app.enums.FilterType;
import app.enums.SearchType;
import app.search.filters.ArtistPageFilter;
import app.search.filters.Filter;
import app.search.filters.HostPageFilter;
import app.management.Library;
import app.pages.Page;
import app.search.filters.UnknownFilter;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class PageFinder extends SearchEngine<Page> {
    /**
     * The type of user to be searched. Inside the {@code PageFinder} class
     * it can only be {@code ARTIST} or {@code HOST}.
     */
    private final SearchType.Type typeOfUser;

    public PageFinder(final Map<String, List<String>> filters,
                      final SearchType.Type typeOfUser) {
        this.typeOfUser = typeOfUser;
        this.filters = collectFilters(filters);
    }

    /**
     * Returns a list of matched pages from the given list.
     * @param providedPages The list of pages to be matched
     * @return A new list, that contains the matched pages. There is no modification
     * on the given list.
     */
    public List<Page> findInList(final List<Page> providedPages) {
        List<Page> pages = providedPages;

        for (Filter<Page> filter: filters) {
            pages = applyFilter(pages, filter);
        }

        return pages;
    }

    /**
     * Converts a (key, value) pair into a filter.
     * @param key The name of the filter
     * @param values The patterns of the filters
     * @return A specific filter, based on {@code key}'s value
     */
    @Override
    public Filter<Page> getFilterByNameAsString(@NonNull final String key,
                                                @NonNull final List<String> values) {
        if (values.isEmpty()) {
            return null;
        }

        FilterType.Type filterType = FilterType.parseNameFilter(key, typeOfUser);

        if (filterType == FilterType.Type.ARTIST_USERNAME) {
            return new ArtistPageFilter(values.get(0));
        }

        if (filterType == FilterType.Type.HOST_USERNAME) {
            return new HostPageFilter(values.get(0));
        }

        return new UnknownFilter<>();
    }

    /**
     * Applies all {@code filters} on a list that contains either all
     * artist pages, or host pages, depending on {@code typeOfUser} field. It calls
     * {@code findInList} method.
     * @return A list of matched pages
     */
    @Override
    public List<Page> collectResults() {
        Library database = Library.getInstance();

        if (typeOfUser == SearchType.Type.ARTIST) {
            return findInList(database.getArtistPages());
        }

        if (typeOfUser == SearchType.Type.HOST) {
            return findInList(database.getHostPages());
        }

        return null;
    }
}
