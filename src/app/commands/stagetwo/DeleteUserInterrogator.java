package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.DeleteUserOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeleteUserInterrogator extends CommandObject {
    @JsonIgnore
    private String exitMessage;

    /**
     * Executes the deleteUser command.
     */
    @Override
    public void execute() {
        exitMessage = manager.requestDeletingUser(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new DeleteUserOutput(this)).generateOutputNode();
    }
}
