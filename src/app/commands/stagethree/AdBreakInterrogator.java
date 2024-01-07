package app.commands.stagethree;

import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class AdBreakInterrogator extends CommandObject {
    private int price;

    /**
     * Executes the adBreak command.
     */
    @Override
    public void execute() {

    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     *
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return null;
    }
}
