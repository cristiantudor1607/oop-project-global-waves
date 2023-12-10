package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import globalwaves.commands.outputs.StatisticsOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

import java.util.List;

@Getter
public class TopFivePlaylistsInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> result;

    /**
     * Method that executes the getTop5Playlists command, and returns it;s output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        result = manager.requestTopFivePlaylists();

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        // Method to remove a field found on stack overflow
        JsonNode output =  (new StatisticsOutput(this)).generateOutputNode();
        ObjectNode object = (ObjectNode) output;
        object.remove("user");

        return output;
    }
}
