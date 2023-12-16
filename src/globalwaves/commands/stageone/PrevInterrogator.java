package globalwaves.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.outputs.stageone.PrevOutput;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class PrevInterrogator extends CommandObject {
    @JsonIgnore
    private String message;

    /**
     *
     * @param manager The ActionManager that manages the players and is able to make changes
     *                at a specific player, or communicates with the library interrogator to
     *                retrieve infos from library.
     * @return The output formatted as JsonNode.
     */
    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            message = username + StringConstants.OFFLINE_DESCRIPTOR;
            return;
        }

        if (timestamp == 6390)
            System.out.println();

        message = manager.requestPrev(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new PrevOutput(this)).generateOutputNode();
    }
}
