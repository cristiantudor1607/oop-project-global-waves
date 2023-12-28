package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.PlayPauseExit;
import app.outputs.stageone.PlayPauseOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class PlayPauseInterrogator extends CommandObject {
    @JsonIgnore private PlayPauseExit.Status exitStatus;

    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = PlayPauseExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestPlayPause(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new PlayPauseOutput(this)).generateOutputNode();
    }
}
