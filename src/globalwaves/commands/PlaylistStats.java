package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlaylistStats {
    private final String name;
    private final String visibility;
    @JsonProperty("songs")
    private List<String> songNames;
    private final int followers;

    public PlaylistStats(Playlist playlist) {
        name = playlist.getName();
        followers = playlist.getFollowers();
        if (playlist.isVisible())
            visibility = "public";
        else
            visibility = "private";

        songNames = new ArrayList<>();
        for (Song song : playlist.getSongs())
            songNames.add(song.getName());
    }
}
