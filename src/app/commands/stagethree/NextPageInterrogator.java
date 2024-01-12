package app.commands.stagethree;

import app.outputs.stagethree.PageNavigationOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class NextPageInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    /**
     * Executes the nextPage command.
     */
    @Override
    public void execute() {
        message = manager.requestNextPage(username);
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
        return new PageNavigationOutput(this).generateOutputNode();
    }
}
