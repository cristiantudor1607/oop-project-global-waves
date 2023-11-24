package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

import java.util.List;

@Getter
public class TopFivePlaylistsInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> result;

    @Override
    public JsonNode execute(ActionManager manager) {
        result = manager.requestTopFivePlaylists();

        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        // Link https://www.baeldung.com/java-jackson-remove-json-elements
        JsonNode output =  (new StatisticsOutput(this)).generateOutputNode();
        ObjectNode object = (ObjectNode) output;
        object.remove("user");

        return output;
    }
}
