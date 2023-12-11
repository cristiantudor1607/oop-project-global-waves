package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stageone.LikeExit;
import globalwaves.commands.outputs.stageone.LikeOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class LikeInterrogator extends CommandObject {
    @JsonIgnore
    private LikeExit.Status exitStatus;

    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = LikeExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestLikeAction(this);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new LikeOutput(this)).generateOutputNode();
    }
}
