package app.outputs.stagetwo;

import app.player.entities.Album;
import app.player.entities.AudioFile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AlbumStats {
    private final String name;
    private final List<String> songs;

    public AlbumStats(final Album album) {
        name = album.getName();
        songs = new ArrayList<>();

        for (AudioFile song: album.getSongs())
            songs.add(song.getName());
    }
}
