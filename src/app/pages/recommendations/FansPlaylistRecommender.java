package app.pages.recommendations;

import app.player.entities.AudioFile;
import app.player.entities.Player;
import app.player.entities.Playlist;
import app.player.entities.Song;
import app.properties.PlayableEntity;
import app.users.User;

import java.util.ArrayList;
import java.util.List;

public class FansPlaylistRecommender extends Recommender{
    private final User user;
    private final User artist;

    public FansPlaylistRecommender(final Player player) {
        user = player.getUser();

        AudioFile playingFile = player.getPlayingFile();
        if (playingFile.getCurrentSong() == null) {
            artist = null;
        } else {
            artist = playingFile.getCurrentSong().getArtistLink();
        }
    }


    /**
     * Returns the playlist recommendation found.
     *
     * @return The playlist recommended, it the recommendation can be found,
     * {@code null otherwise}
     */
    @Override
    public PlayableEntity getRecommendation() {
        if (artist == null) {
            return null;
        }

        List<User> fans = artist.getTop5Fans();
        if (fans == null) {
            return null;
        }

        List<Song> playlistSongs = new ArrayList<>();
        fans.forEach(fan -> {
            List<Song> fanSongs = fan.getTopSongs();

            fanSongs = fanSongs.stream()
                    .filter(song -> !playlistSongs.contains(song))
                    .limit(5)
                    .toList();
            playlistSongs.addAll(fanSongs);
        });

        // FIXME: Daca playlisturile au problema, trebuie sortata iar lista mare dupa numarul
        // de likes

        String playlistName = artist.getUsername() + " Fan Club recommendations";

        return new Playlist.Builder()
                .name(playlistName)
                .owner(user.getUsername())
                .ownerLink(user)
                .songs(playlistSongs)
                .build();
    }
}
