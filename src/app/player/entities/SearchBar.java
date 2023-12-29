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

    public PageFinder
    choosePager(@NonNull final String type,
                @NonNull final Map<String, List<String>> filters) {
        return switch (SearchType.parseString(type)) {
            case HOST -> new PageFinder(filters, SearchType.Type.HOST);
            case ARTIST -> new PageFinder(filters, SearchType.Type.ARTIST);
            default -> null;
        };
    }

    public void search(@NonNull final String type,
                       @NonNull final Map<String, List<String>> filters) {
        // One of them will return null, so we'll use the one that isn't null
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

    public List<String> getPagesAsNames() {
        if (pages == null)
            return null;

        List<Page> relevantResults =
                EngineResultsParser.getRelevantResults(pages);

        return EngineResultsParser.getUsernamesFromPages(relevantResults);
    }

    public List<String> getResultsAsNames() {
        if (results == null)
            return null;

        List<? extends PlayableEntity> relevantResults =
                EngineResultsParser.getRelevantResults(results);

        return EngineResultsParser.getNamesFromList(relevantResults);
    }

    public boolean hasSearchedPages() {
        return typeOfSearch == SearchResult.PAGE;
    }

    public boolean hasSearchedPlayableEntities() {
        return typeOfSearch == SearchResult.PLAYABLE_ENTITY;
    }

    public boolean hasNoSearchResult() {
        return switch (typeOfSearch) {
            case PLAYABLE_ENTITY -> results.isEmpty();
            case PAGE -> pages.isEmpty();
        };
    }

    public boolean invalidItem(int index) {
        return switch (typeOfSearch) {
            case PLAYABLE_ENTITY -> index > results.size() || index < 0;
            case PAGE -> index > pages.size() || index < 0;
        };
    }

    public void resetResults() {
        results = null;
    }

    public boolean wasNotInvoked() {
        if (typeOfSearch == null)
            return true;

        return switch (typeOfSearch) {
            case PLAYABLE_ENTITY -> results == null;
            case PAGE -> pages == null;
        };
    }

    public PlayableEntity getResultAtIndex(int index) {
        return results.get(index);
    }

    public Page getPageAtIndex(int index) {
        return pages.get(index);
    }
}
