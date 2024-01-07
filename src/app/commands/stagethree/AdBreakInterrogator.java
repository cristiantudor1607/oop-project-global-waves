package app.commands.stagethree;

import app.exitstats.stagethree.AdBreakExit;
import app.outputs.stagethree.AdBreakOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class AdBreakInterrogator extends CommandObject {
    private int price;
    @JsonIgnore
    private AdBreakExit.Status exitStatus;

    /**
     * Executes the adBreak command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestAdBreak(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     *
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return new AdBreakOutput(this).generateOutputNode();
    }
}
