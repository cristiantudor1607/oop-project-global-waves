package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.RepeatOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class RepeatInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    /**
     * Execute the repeat command.
     */
    @Override
    public void execute() {
        message = manager.requestRepeatAction(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new RepeatOutput(this)).generateOutputNode();
    }
}
