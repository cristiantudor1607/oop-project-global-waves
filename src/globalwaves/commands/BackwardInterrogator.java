package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.ForwardBackwardOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class BackwardInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    @Override
    public void execute() {
        message = manager.requestBackward(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ForwardBackwardOutput(this)).generateOutputNode();
    }
}
