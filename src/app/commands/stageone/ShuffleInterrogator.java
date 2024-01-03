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
     * Executes the shuffle command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestShuffling(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new ShuffleOutput(this)).generateOutputNode();
    }
}
