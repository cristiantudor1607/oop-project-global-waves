package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.ChangePageOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class ChangePageInterrogator extends CommandObject {
    private String nextPage;
    @JsonIgnore
    private String output;

    @Override
    public void execute() {
        manager.setLastActionTime(timestamp);

        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            output = null;
            return;
        }

        output = manager.requestChangePage(this);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ChangePageOutput(this)).generateOutputNode();
    }
}
