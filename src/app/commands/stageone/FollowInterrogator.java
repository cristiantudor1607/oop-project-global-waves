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
