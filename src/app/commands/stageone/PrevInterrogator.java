package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.PrevOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class PrevInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    /**
     * Executes the prev command.
     */
    @Override
    public void execute() {
        message = manager.requestPrev(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new PrevOutput(this)).generateOutputNode();
    }
}
