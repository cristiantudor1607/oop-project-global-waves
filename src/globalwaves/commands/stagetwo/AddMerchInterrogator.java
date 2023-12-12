package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.AddMerchExit;
import globalwaves.commands.outputs.stagetwo.AddMerchOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddMerchInterrogator extends CommandObject {
    private String name;
    private String description;
    private int price;
    @JsonIgnore
    private AddMerchExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestAddingMerch(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new AddMerchOutput(this)).generateOutputNode();
    }
}
