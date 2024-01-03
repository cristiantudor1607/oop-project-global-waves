package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemoveEventExit;
import app.outputs.stagetwo.RemoveEventOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemoveEventInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemoveEventExit.Status exitStatus;

    /**
     * Executes the removeEvent command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestRemovingEvent(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new RemoveEventOutput(this)).generateOutputNode();
    }
}
