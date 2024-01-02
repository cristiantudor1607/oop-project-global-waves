package app.commands.stagetwo;

import app.utilities.constants.StringConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.PrintPageOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PrintPageInterrogator extends CommandObject {
    @JsonIgnore
    private String output = null;

    /**
     * Executes the printCurrentPage command.
     */
    @Override
    public void execute() {
        manager.setLastActionTime(timestamp);

        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            output = username + StringConstants.OFFLINE_DESCRIPTOR;
            return;
        }

        output = manager.requestPageContent(username);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new PrintPageOutput(this)).generateOutputNode();
    }
}
