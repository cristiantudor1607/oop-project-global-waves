package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.SelectExit;
import app.outputs.stageone.SelectOutput;
import app.parser.commands.templates.CommandObject;
import app.pages.Page;
import app.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class SelectInterrogator extends CommandObject {
    private int itemNumber;
    @JsonIgnore private PlayableEntity selectedPlayableEntity = null;
    @JsonIgnore private Page selectedPage = null;
    @JsonIgnore private SelectExit.Status exitStatus;

    @Override
    public void execute() {
        approval = manager.requestApprovalForAction(this);
        if (!approval) {
            exitStatus = SelectExit.Status.OFFLINE;
            return;
        }

        exitStatus = manager.requestItemSelection(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new SelectOutput(this)).generateOutputNode();
    }
}
