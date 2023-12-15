package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.stagetwo.PrintPageOutput;
import globalwaves.parser.templates.CommandObject;
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
        if (!approval)
            return;

        System.out.println(username + " at timestamp " + timestamp);
        output = manager.requestPageContent(this);
    }


    @Override
    public JsonNode formatOutput() {
        return (new PrintPageOutput(this)).generateOutputNode();
    }
}
