package app.commands.stagethree;

import app.outputs.stagethree.UpdateRecomsOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class UpdateRecomsInterrogator extends CommandObject {
    public enum Status {
        DOESNT_EXIST,
        NO_RECOMS,
        NOT_NORMAL_USER,
        SUCCESS,
    }

    private String recommendationType;
    @JsonIgnore
    private Status exitStatus;

    /**
     * Executes the updateRecommendations command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestUpdateRecoms(this);
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
        return new UpdateRecomsOutput(this).generateOutputNode();
    }
}
