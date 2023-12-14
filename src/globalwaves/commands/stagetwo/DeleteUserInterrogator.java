package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.stagetwo.DeleteUserOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class DeleteUserInterrogator extends CommandObject {
    @JsonIgnore
    private String exitMessage;

    @Override
    public void execute() {
        exitMessage = manager.requestDeletingUser(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new DeleteUserOutput(this)).generateOutputNode();
    }
}
