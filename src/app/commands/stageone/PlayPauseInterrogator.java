package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.PlayPauseExit;
import app.outputs.stageone.PlayPauseOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class PlayPauseInterrogator extends CommandObject {
    @JsonIgnore private PlayPauseExit.Status exitStatus;

    /**
     * Executes the playPause command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestPlayPause(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new PlayPauseOutput(this)).generateOutputNode();
    }
}
