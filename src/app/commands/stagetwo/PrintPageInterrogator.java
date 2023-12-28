package app.commands.stagetwo;

import app.utilities.constants.StringConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.PrintPageOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class PrintPageInterrogator extends CommandObject {
    @JsonIgnore
    private String output = null;

    @Override
    public void execute() {
        // Because the timestamp doesn't have an effect on requestPageContent
        // we can set the timestamp of the action manager before requesting
        // the approval
        manager.setLastActionTime(timestamp);

        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            output = username + StringConstants.OFFLINE_DESCRIPTOR;
            return;
        }

        output = manager.requestPageContent(username);
    }


    @Override
    public JsonNode formatOutput() {
        return (new PrintPageOutput(this)).generateOutputNode();
    }
}
