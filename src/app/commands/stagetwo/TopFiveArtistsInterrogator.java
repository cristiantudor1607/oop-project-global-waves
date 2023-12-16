package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import app.outputs.stageone.StatisticsOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

import java.util.List;

@Getter
public class TopFiveArtistsInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> results;

    @Override
    public void execute() {
        results = manager.requestTopFiveArtists();
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        // Link https://www.baeldung.com/java-jackson-remove-json-elements
        JsonNode output =  (new StatisticsOutput(this)).generateOutputNode();
        ObjectNode object = (ObjectNode) output;
        object.remove("user");

        return output;
    }
}
