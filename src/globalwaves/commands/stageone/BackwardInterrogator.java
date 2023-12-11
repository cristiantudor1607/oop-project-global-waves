package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.stageone.ForwardBackwardOutput;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class BackwardInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            message = username + StringConstants.OFFLINE_DESCRIPTOR;
            return;
        }

        message = manager.requestBackward(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ForwardBackwardOutput(this)).generateOutputNode();
    }
}
