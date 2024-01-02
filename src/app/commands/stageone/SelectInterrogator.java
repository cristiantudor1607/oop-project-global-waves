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

    /**
     * Executes the select command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestItemSelection(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new SelectOutput(this)).generateOutputNode();
    }
}
