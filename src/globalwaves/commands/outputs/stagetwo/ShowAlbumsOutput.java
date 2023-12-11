package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.stagetwo.ShowAlbumsInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import globalwaves.player.entities.Album;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShowAlbumsOutput extends CommandOutputFormatter {
    private final List<AlbumStats> result;

    public ShowAlbumsOutput(final ShowAlbumsInterrogator executedQuery) {
        command = "showAlbums";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = new ArrayList<>();

        for (Album album: executedQuery.getResults())
            result.add(new AlbumStats(album));
    }
}
