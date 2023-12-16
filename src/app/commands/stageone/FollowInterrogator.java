package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.FollowExit;
import app.outputs.stageone.FollowOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class FollowInterrogator extends CommandObject {
    @JsonIgnore
    private FollowExit.Status exitStatus;

    /**
     * Method that executes the Follow Command and generates it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = FollowExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestFollowAction(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new FollowOutput(this)).generateOutputNode();
    }
}
