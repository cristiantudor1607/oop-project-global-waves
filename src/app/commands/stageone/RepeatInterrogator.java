package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.RepeatOutput;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class RepeatInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    /**
     * The method executes the Repeat Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            message = username + StringConstants.OFFLINE_DESCRIPTOR;
            return;
        }

        message = manager.requestRepeatAction(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new RepeatOutput(this)).generateOutputNode();
    }
}
