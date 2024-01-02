package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.ShowPlaylistsOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Playlist;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowPlaylistsInterrogator extends CommandObject {
    @JsonIgnore
    private List<Playlist> userPlaylists;

    /**
     * Executes the showPlaylists command.
     */
    @Override
    public void execute() {
        userPlaylists = manager.requestUserPlaylists(username);

        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new ShowPlaylistsOutput(this)).generateOutputNode();
    }
}
