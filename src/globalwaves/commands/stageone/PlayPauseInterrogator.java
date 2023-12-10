package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stageone.PlayPauseExit;
import globalwaves.commands.outputs.stageone.PlayPauseOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class PlayPauseInterrogator extends CommandObject {
    @JsonIgnore private PlayPauseExit.Status exitStatus;

    /**
     * The method executes the PlayPause Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestUpdateState(this);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new PlayPauseOutput(this)).generateOutputNode();
    }
}
