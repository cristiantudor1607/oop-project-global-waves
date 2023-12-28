package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.SwitchConnectionExit;
import app.outputs.stagetwo.SwitchConnectionOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class ConnectionInterrogator extends CommandObject {
    @JsonIgnore
    private SwitchConnectionExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestSwitchConnection(username);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SwitchConnectionOutput(this)).generateOutputNode();
    }
}
