package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class ShowPlaylistsOutput extends CommandOutputFormatter {
    List<PlaylistStats> result;

    public ShowPlaylistsOutput(ShowPlaylistsInterrogator executedQuery) {
        command = "showPlaylists";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = new ArrayList<>();
        for (Playlist p : executedQuery.getUserPlaylists())
            result.add(new PlaylistStats(p));
    }
}

@Getter
public class ShowPlaylistsInterrogator extends CommandObject {
    @JsonIgnore
    private List<Playlist> userPlaylists;

    @Override
    public JsonNode execute(ActionManager manager) {
        userPlaylists = manager.requestOwnerPlaylists(username);

        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);
        return (new ShowPlaylistsOutput(this)).generateOutputNode();
    }
}
