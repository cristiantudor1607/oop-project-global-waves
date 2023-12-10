package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.stagetwo.SwitchConnectionExit;
import globalwaves.commands.outputs.stagetwo.SwitchConnectionOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class ConnectionInterrogator extends CommandObject {
    @JsonIgnore
    private SwitchConnectionExit.Code exitCode;

    @Override
    public void execute() {
        exitCode = manager.requestSwitchConnection(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SwitchConnectionOutput(this)).generateOutputNode();
    }
}
