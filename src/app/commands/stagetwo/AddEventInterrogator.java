package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.AddEventExit;
import app.outputs.stagetwo.AddEventOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddEventInterrogator extends CommandObject {
    private String name;
    private String description;
    private String date;

    @JsonIgnore
    private AddEventExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestAddingEvent(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new AddEventOutput(this)).generateOutputNode();
    }
}
