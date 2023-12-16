package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.LoadExit;
import app.outputs.stageone.LoadOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class LoadInterrogator extends CommandObject {
    @JsonIgnore private LoadExit.Status exitStatus;

    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = LoadExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestLoading(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new LoadOutput(this)).generateOutputNode();
    }
}
