package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.SearchExit;
import app.outputs.stageone.SearchOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class SearchInterrogator extends CommandObject {
    private String type;
    @JsonIgnore private Map<String, List<String>> filters;
    @JsonIgnore private List<String> results;
    @JsonIgnore private SearchExit.Status exitStatus = SearchExit.Status.SUCCESS;

    /**
     * Executes the search command.
     */
    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = SearchExit.Status.OFFLINE;
            results = new ArrayList<>();
            return;
        }

        results = manager.requestSearchResults(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
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

    /**
     * Sets the filters of the search.
     * @param mappedFilters The filters parsed as a Map
     */
    @Override
    public void setFiltersField(final Map<String, List<String>> mappedFilters) {
        filters = mappedFilters;
    }
}
