package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.AddUserExit;
import globalwaves.commands.outputs.stagetwo.AddUserOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class AddUserInterrogator extends CommandObject {
    private String type;
    private int age;
    private String city;
    @JsonIgnore
    private AddUserExit.Status exitStatus;


    @Override
    public void execute() {
        exitStatus = manager.requestAddingUser(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new AddUserOutput(this)).generateOutputNode();
    }
}
