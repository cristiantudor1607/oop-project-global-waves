package app.outputs.stageone;

import app.commands.stageone.ShowPlaylistsInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import app.player.entities.Playlist;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShowPlaylistsOutput extends CommandOutputFormatter {
    private final List<PlaylistStats> result;

    public ShowPlaylistsOutput(final ShowPlaylistsInterrogator executedQuery) {
        command = "showPlaylists";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = new ArrayList<>();
        for (Playlist p : executedQuery.getUserPlaylists()) {
            result.add(new PlaylistStats(p));
        }
    }
}
