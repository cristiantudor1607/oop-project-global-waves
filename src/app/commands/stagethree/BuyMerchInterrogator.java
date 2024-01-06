package app.commands.stagethree;

import app.exitstats.stagethree.BuyMerchExit;
import app.outputs.stagethree.BuyMerchOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class BuyMerchInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private BuyMerchExit.Status exitStatus;

    /**
     * Executes the buyMerch command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestBuyMerch(this);
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
        return new BuyMerchOutput(this).generateOutputNode();
    }
}
