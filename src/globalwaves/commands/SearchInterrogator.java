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
    @JsonIgnore private List<String> results;


    @Override
    public void execute() {
        results = manager.requestSearch(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SearchOutput(this)).generateOutputNode();
    }

    /**
     * Method that always returns true, because the Search Command has to work with filters
     * @return true
     */
    @Override
    public boolean hasFiltersField() {
        return true;
    }

    @Override
    public void setFiltersField(Map<String, List<String>> mappedFilters) {
        filters = mappedFilters;
    }
}
