package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.ShuffleExit;
import globalwaves.commands.outputs.ShuffleOutput;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
public class ShuffleInterrogator extends CommandObject {
    private int seed;
    @JsonIgnore
    private ShuffleExit.Code exitCode;

    /**
     * The method executes the Shuffle Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public JsonNode execute(final ActionManager manager) {
        exitCode = manager.requestShuffling(this);

        manager.setLastActionTime(timestamp);
        return (new ShuffleOutput(this)).generateOutputNode();
    }
}
