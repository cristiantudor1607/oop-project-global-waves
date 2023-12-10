package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import globalwaves.commands.outputs.stageone.StatisticsOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

import java.util.List;

@Getter
public class TopFiveSongsInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> result;

    /**
     * The method executes the getTop5Songs Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        result = manager.requestTopFiveSongs();

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
