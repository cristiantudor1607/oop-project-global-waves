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

    /**
     * Executes the addEvent command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestAddingEvent(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new AddEventOutput(this)).generateOutputNode();
    }
}
