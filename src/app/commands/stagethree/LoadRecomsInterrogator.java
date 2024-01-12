package app.commands.stagethree;

import app.outputs.stagethree.LoadRecomsOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class LoadRecomsInterrogator extends CommandObject {
    public enum Status {
        DOESNT_EXIST,
        NO_RECOMS,
        OFFLINE,
        SUCCESS
    }

    @JsonIgnore
    private Status exitStatus;
    /**
     * Executes the loadRecommendations command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestLoadRecoms(this);
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
        return new LoadRecomsOutput(this).generateOutputNode();
    }
}
