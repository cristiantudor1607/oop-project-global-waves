package app.player.entities;

import app.enums.SearchResult;
import app.enums.SearchType;
import app.pages.Page;
import app.properties.PlayableEntity;
import app.search.engines.AlbumEngine;
import app.search.engines.EngineResultsParser;
import app.search.engines.PageFinder;
import app.search.engines.PlaylistEngine;
import app.search.engines.PodcastEngine;
import app.search.engines.SearchEngine;
import app.search.engines.SongEngine;
import app.utilities.SortByCreationTime;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class SearchBar {
    private List<? extends PlayableEntity> results;
    private List<Page> pages;
    private SearchEngine<? extends PlayableEntity> engine;
    private PageFinder pager;
    private SearchResult typeOfSearch;
    private final String username;

    public SearchBar(final String username) {
        results = new ArrayList<>();
        pages = new ArrayList<>();
        this.username = username;
    }

    /**
     * Chooses a specific engine to be used for search entities.
     * @param type The type of search, as a string
     * @param filters The filters that will be applied
     * @return A specific SearchEngine, based on the {@code type} provided, or {@code null},
     * if the type isn't supported
     */
    public SearchEngine<? extends PlayableEntity>
    chooseEngine(@NonNull final String type,
                 @NonNull final Map<String, List<String>> filters) {
        return switch (SearchType.parseString(type)) {
            case SONG -> new SongEngine(filters);
            case PODCAST -> new PodcastEngine(filters);
            case PLAYLIST -> new PlaylistEngine(filters, username);
            case ALBUM -> new AlbumEngine(filters);
            default -> null;
        };
    }

    /**
     * Chooses a specific finder to be used for search pages.
     * @param type The type of search
     * @param filters The filters that will be applied
     * @return A specific PageFinder, based on the {@code type} provided, or {@code null},
     * if the type isn't supported
     */
    public PageFinder
    choosePager(@NonNull final String type,
                @NonNull final Map<String, List<String>> filters) {
        return switch (SearchType.parseString(type)) {
            case HOST -> new PageFinder(filters, SearchType.Type.HOST);
            case ARTIST -> new PageFinder(filters, SearchType.Type.ARTIST);
            default -> null;
        };
    }

    /**
     * Searches entities or pages.
     * @param type The type of search.
     * @param filters The filters that will be applied.
     */
    public void search(@NonNull final String type,
                       @NonNull final Map<String, List<String>> filters) {
        // Reset all results
        results = null;
        pages = null;

        // Choose the engine. One of them will return null,
        // so we'll use the one that isn't null
        engine = chooseEngine(type, filters);
        pager = choosePager(type, filters);


        if (engine != null) {
            typeOfSearch = SearchResult.PLAYABLE_ENTITY;
            results = engine.collectResults();
            results.sort(new SortByCreationTime());
        }

        if (pager != null) {
            typeOfSearch = SearchResult.PAGE;
            pages = pager.collectResults();
        }
    }

    /**
     * Deletes all previously found entities by resetting the {@code results} field.
     */
    public void resetResults() {
        results = null;
    }

    /**
     * Returns a list with the names of found pages. The method should be called only if
     * pages were searched before.
     * @return A list of strings, containing the names of the pages
     */
    public List<String> getPagesAsNames() {
        if (pages == null) {
            return null;
        }

        List<Page> relevantResults =
                EngineResultsParser.getRelevantResults(pages);

        return EngineResultsParser.getUsernamesFromPages(relevantResults);
    }

    /**
     * Returns a list with the names of found entities. The method should be called only if
     * entities were searched before.
     * @return A list of strings, containing the names of the entities
     */
    public List<String> getResultsAsNames() {
        if (results == null) {
            return null;
        }

        List<? extends PlayableEntity> relevantResults =
                EngineResultsParser.getRelevantResults(results);

        return EngineResultsParser.getNamesFromList(relevantResults);
    }

    /**
     * Checks if the user searched pages last time.
     * @return {@code true}, if there were pages searched, {@code false} otherwise
     */
    public boolean hasSearchedPages() {
        return typeOfSearch == SearchResult.PAGE;
    }

    /**
     * Returns the entity at specified index from {@code results} list. The method is intended to
     * be used to select an entity after a search is performed.
     * @param index The index of the result. It doesn't check if {@code index} is out of bounds.
     * @return The entity at specified index
     */
    public PlayableEntity getResultAtIndex(final int index) {
        return results.get(index);
    }

    /**
     * Returns the page at specified index from {@code pages} list. The method is intended to
     * be used to select a page after a search is performed.
     * @param index The index of the result. It doesn't check if {@code index} is out of bounds.
     * @return The page at specified index
     */
    public Page getPageAtIndex(final int index) {
        return pages.get(index);
    }

    /**
     * Checks if search returned no results.
     * @return {@code true}, if there is no result after searching, {@code false} otherwise
     */
    public boolean hasNoSearchResult() {
        return switch (typeOfSearch) {
            case PLAYABLE_ENTITY -> results.isEmpty();
            case PAGE -> pages.isEmpty();
        };
    }

    /**
     * Checks if the specified index is out of bounds. If refers to the last type of search.
     * @param index The index to be verified.
     * @return {@code true}, if the index is out of bounds, {@code false} otherwise
     */
    public boolean invalidItem(final int index) {
        return switch (typeOfSearch) {
            case PLAYABLE_ENTITY -> index > results.size() || index < 0;
            case PAGE -> index > pages.size() || index < 0;
        };
    }

    /**
     * Checks if the SearchBar was not used.
     * @return {@code true}, if the searchbar wasn't used, {@code false} otherwise
     */
    public boolean wasNotInvoked() {
        if (typeOfSearch == null) {
            return true;
        }

        return switch (typeOfSearch) {
            case PLAYABLE_ENTITY -> results == null;
            case PAGE -> pages == null;
        };
    }
}
