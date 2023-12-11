package globalwaves.player.entities.properties;

import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.paging.HomePage;

import java.util.List;

public class ContentVisitor implements Visitor {
    @Override
    public String visit(HomePage page) {
        List<Song> recommendedSongs = page.getRecommendedSongs();
        List<Playlist> recommendedPlaylists = page.getRecommendedPlaylist();

        StringBuilder songNames = new StringBuilder();
        for (int i = 0; i < recommendedSongs.size(); i++) {
            if (i != 0)
                songNames.append(", ");

            songNames.append(recommendedSongs.get(i).getName());
        }

        StringBuilder playlistNames = new StringBuilder();
        for (int i = 0; i < recommendedPlaylists.size(); i++) {
            if (i != 0)
                playlistNames.append(", ");

            playlistNames.append(recommendedPlaylists.get(i).getName());
        }

        return "Liked songs:\n\t[" + songNames + "]\n\nFollowed playlists:\n\t["
                + playlistNames + "]";
    }
}
