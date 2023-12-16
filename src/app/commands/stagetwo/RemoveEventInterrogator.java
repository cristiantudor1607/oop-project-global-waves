package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemoveEventExit;
import app.outputs.stagetwo.RemoveEventOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class RemoveEventInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemoveEventExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestRemovingEvent(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new RemoveEventOutput(this)).generateOutputNode();
    }
}
