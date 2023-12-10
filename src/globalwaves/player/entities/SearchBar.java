package globalwaves.player.entities;

import globalwaves.commands.enums.SearchType;
import globalwaves.commands.search.utils.*;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class SearchBar {
    private List<? extends PlayableEntity> results;
    private SearchEngine<? extends PlayableEntity> engine;
    private String username;

    public SearchBar() {
        results = new ArrayList<>();
    }

    public SearchEngine<? extends PlayableEntity>
    chooseEngine(@NonNull final String type,
                 @NonNull final Map<String, List<String>> filters) {
        return switch (SearchType.parseString(type)) {
            case SONG -> new SongEngine(filters);
            case PODCAST -> new PodcastEngine(filters);
            case PLAYLIST -> new PlaylistEngine(filters, username);
            case UNKNOWN -> null;
        };
    }

    public void search(@NonNull final String type,
                       @NonNull final Map<String, List<String>> filters) {
        engine = chooseEngine(type, filters);
        results = engine.collectResults();
    }

    public List<String> getRelevantResultsAsNames() {
        if (results == null)
            return null;

        List<? extends PlayableEntity> relevantResults =
                EngineResultsParser.getRelevantResults(results);

        return EngineResultsParser.getNamesFromList(relevantResults);
    }

    public boolean hasNoSearchResult() {
        return results.isEmpty();
    }

    public boolean invalidItem(int index) {
        return  index <= results.size();
    }

    public void reset() {
        results = null;
    }

    public boolean wasNotInvoked() {
        return results == null;
    }

    public PlayableEntity getResultAtIndex(int index) {
        return results.get(index);
    }
}
