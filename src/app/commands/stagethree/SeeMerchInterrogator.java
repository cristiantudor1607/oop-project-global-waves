package app.commands.stagethree;

import app.outputs.stagethree.SeeMerchOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class SeeMerchInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> items;

    /**
     * Executes the seeMerch command.
     */
    @Override
    public void execute() {
        items = manager.requestMerchandisingItems(username);
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
        return new SeeMerchOutput(this).generateOutputNode();
    }
}
