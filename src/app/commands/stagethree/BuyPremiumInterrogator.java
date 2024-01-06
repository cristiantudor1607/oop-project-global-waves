package app.commands.stagethree;

import app.exitstats.stagethree.ChangeSubscriptionExit;
import app.outputs.stagethree.BuyPremiumOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class BuyPremiumInterrogator extends CommandObject {
    private ChangeSubscriptionExit.Status exitStatus;
    /**
     * Executes the buyPremium command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestBuyPremium(username);
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
        return (new BuyPremiumOutput(this)).generateOutputNode();
    }
}
