package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.ShuffleExit;
import app.outputs.stageone.ShuffleOutput;
import app.parser.commands.templates.CommandObject;
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
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = ShuffleExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestShuffling(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ShuffleOutput(this)).generateOutputNode();
    }
}
