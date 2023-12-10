package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitcodes.stageone.ShuffleExit;
import globalwaves.commands.outputs.stageone.ShuffleOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class ShuffleInterrogator extends CommandObject {
    private int seed;
    @JsonIgnore
    private ShuffleExit.Status exitStatus;

    /**
     * The method executes the Shuffle Command and returns it's output.
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestShuffling(this);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ShuffleOutput(this)).generateOutputNode();
    }
}
