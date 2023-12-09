package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.AddRemoveExit;
import globalwaves.commands.outputs.AddRemoveOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class AddRemoveInterrogator extends CommandObject {
    private int playlistId;
    @JsonIgnore
    private AddRemoveExit.Code exitCode;

    /**
     * The method executes the AddRemove Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public JsonNode execute(final ActionManager manager) {
        exitCode = manager.requestAddRemove(this);
        manager.setLastActionTime(timestamp);

        return (new AddRemoveOutput(this)).generateOutputNode();
    }
}
