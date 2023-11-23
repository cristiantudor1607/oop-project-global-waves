package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.SearchType;
import globalwaves.commands.search.utils.*;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
class SearchOutput extends CommandOutputFormatter {
    private final String message;
    private final List<String> results;

    public SearchOutput(SearchInterrogator executingCommand) {
        command = "search";
        user = executingCommand.getUsername();
        timestamp = executingCommand.getTimestamp();
        message = generateMessage(executingCommand);
        results = EngineResultsParser.getNamesFromList(executingCommand.getSearchResults());
    }

    String generateMessage(SearchInterrogator executingCommand) {
        int n = executingCommand.getSearchResults().size();
        return "Search returned " + n + " results";
    }
}

@Getter @Setter
public class SearchInterrogator extends CommandObject {
    private String type;
    @JsonIgnore private Map<String, List<String>> filters;
    @JsonIgnore private SearchEngine<? extends PlayableEntity> engine;
    @JsonIgnore private List<? extends PlayableEntity> searchResults;

    public void chooseEngine() {
        switch (Objects.requireNonNull(SearchType.ParseString(type))) {
            case SONG -> engine = new SongEngine(filters);
            case PODCAST -> engine = new PodcastEngine(filters);
            case PLAYLIST -> engine = new PlaylistEngine(filters, username);
        }
    }


    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Searching ...");
        chooseEngine();
        searchResults = engine.collectResults();
        searchResults = EngineResultsParser.getRelevantResults(searchResults);

        manager.requestSearchResult(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new SearchOutput(this)).generateOutputNode();
    }

    @Override
    public boolean hasFilters() {
        return true;
    }


}
