package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.SearchType;
import globalwaves.commands.outputs.SearchOutput;
import globalwaves.commands.search.utils.EngineResultsParser;
import globalwaves.commands.search.utils.PlaylistEngine;
import globalwaves.commands.search.utils.PodcastEngine;
import globalwaves.commands.search.utils.SearchEngine;
import globalwaves.commands.search.utils.SongEngine;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter @Setter
public class SearchInterrogator extends CommandObject {
    private String type;
    @JsonIgnore private Map<String, List<String>> filters;
    @JsonIgnore private SearchEngine<? extends PlayableEntity> engine;
    @JsonIgnore private List<? extends PlayableEntity> searchResults;

    /**
     * Method that chooses what Search Engine should be used, based on the type
     * provided at input
     */
    public SearchEngine<? extends PlayableEntity> chooseEngine() {
        return switch (Objects.requireNonNull(SearchType.parseString(type))) {
            case SONG -> new SongEngine(filters);
            case PODCAST -> new PodcastEngine(filters);
            case PLAYLIST -> new PlaylistEngine(filters, username);
            default -> null;
        };
    }

    /**
     * Method that executes the search command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public JsonNode execute(final ActionManager manager) {
        engine = chooseEngine();
        searchResults = engine.collectResults();
        searchResults = EngineResultsParser.getRelevantResults(searchResults);

        manager.requestSearchResult(this);
        manager.setLastActionTime(timestamp);

        return (new SearchOutput(this)).generateOutputNode();
    }

    /**
     * Method that always returns true, because the Search Command has to work with filters
     * @return true
     */
    @Override
    public boolean hasFilters() {
        return true;
    }


}
