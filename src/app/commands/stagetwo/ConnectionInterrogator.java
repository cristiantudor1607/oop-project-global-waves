package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.SwitchConnectionExit;
import app.outputs.stagetwo.SwitchConnectionOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConnectionInterrogator extends CommandObject {
    @JsonIgnore
    private SwitchConnectionExit.Status exitStatus;

    /**
     * Executes the switchConnectionStatus command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestSwitchConnection(username);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new SwitchConnectionOutput(this)).generateOutputNode();
    }
}
