package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.LikeExit;
import globalwaves.commands.outputs.LikeOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class LikeInterrogator extends CommandObject {
    @JsonIgnore
    private LikeExit.Code exitCode;

    @Override
    public void execute() {
        exitCode = manager.requestLikeAction(this);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new LikeOutput(this)).generateOutputNode();
    }
}
