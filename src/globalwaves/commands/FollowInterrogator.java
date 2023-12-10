package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.FollowExit;
import globalwaves.commands.outputs.FollowOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class FollowInterrogator extends CommandObject {
    @JsonIgnore
    private FollowExit.Code exitCode;

    /**
     * Method that executes the Follow Command and generates it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        exitCode = manager.requestFollowAction(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new FollowOutput(this)).generateOutputNode();
    }
}
