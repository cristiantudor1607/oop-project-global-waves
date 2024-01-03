package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.AddUserExit;
import app.outputs.stagetwo.AddUserOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddUserInterrogator extends CommandObject {
    private String type;
    private int age;
    private String city;
    @JsonIgnore
    private AddUserExit.Status exitStatus;

    /**
     * Executes the addUser command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestAddingUser(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new AddUserOutput(this)).generateOutputNode();
    }
}
