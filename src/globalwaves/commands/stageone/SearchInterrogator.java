package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.stageone.SearchExit;
import globalwaves.commands.outputs.stageone.SearchOutput;
import globalwaves.parser.templates.CommandObject;
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
    @JsonIgnore private SearchExit.Status exitStatus;


    @Override
    public void execute() {
        exitStatus = manager.requestApprovalForSearch(this);
        if (exitStatus == SearchExit.Status.OFFLINE) {
            results = new ArrayList<>();
            return;
        }

        results = manager.requestSearchResults(this);
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
