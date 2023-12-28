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


    @Override
    public void execute() {
        userPlaylists = manager.requestUserPlaylists(username);

        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ShowPlaylistsOutput(this)).generateOutputNode();
    }
}
