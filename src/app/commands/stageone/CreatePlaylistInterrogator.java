package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stageone.CreationExit;
import app.outputs.stageone.CreatePlaylistOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class CreatePlaylistInterrogator extends CommandObject {
    private String playlistName;
    @JsonIgnore private CreationExit.Status exitStatus;

    /**
     * Executes the createPlaylist command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestPlaylistCreation(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new CreatePlaylistOutput(this)).generateOutputNode();
    }
}
