package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.AddMerchExit;
import app.outputs.stagetwo.AddMerchOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddMerchInterrogator extends CommandObject {
    private String name;
    private String description;
    private int price;
    @JsonIgnore
    private AddMerchExit.Status exitStatus;

    /**
     * Executes the addMerch command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestAddingMerch(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new AddMerchOutput(this)).generateOutputNode();
    }
}
