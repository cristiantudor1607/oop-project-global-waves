package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.PlayPauseExit;
import globalwaves.commands.outputs.PlayPauseOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class PlayPauseInterrogator extends CommandObject {
    @JsonIgnore private PlayPauseExit.Code exitCode;

    /**
     * The method executes the PlayPause Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public JsonNode execute(final ActionManager manager) {
        exitCode = manager.requestUpdateState(this);

        manager.setLastActionTime(timestamp);

        return (new PlayPauseOutput(this)).generateOutputNode();
    }
}
