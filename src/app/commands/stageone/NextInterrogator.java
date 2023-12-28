package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.NextOutput;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class NextInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            message = username + StringConstants.OFFLINE_DESCRIPTOR;
            return;
        }

        message = manager.requestNext(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new NextOutput(this)).generateOutputNode();
    }
}