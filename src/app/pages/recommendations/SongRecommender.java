package app.pages.recommendations;

import app.player.entities.AudioFile;
import app.player.entities.Player;
import app.player.entities.Song;
import app.properties.PlayableEntity;
import app.users.Accessor;
import app.utilities.HelperTool;

import java.util.List;

public class SongRecommender extends Recommender {
    private final Accessor accessor;
    private final HelperTool tool;
    private final AudioFile playingFile;
    private final int passedTime;

    private static final int MIN_TIME = 30;

    public SongRecommender(final Player player) {
        accessor = new Accessor();
        tool = HelperTool.getInstance();

        playingFile = player.getPlayingFile();
        passedTime = playingFile.getDuration() - player.getRemainedTime();
    }

    /**
     * Returns the found recommended song.
     * It takes the genre of the current song, and makes a list of all songs from the same genre.
     * It generates a random index and takes a random song.
     *
     * @return The song recommended if it can be found, {@code null otherwise}
     */
    @Override
    public PlayableEntity getRecommendation() {
        Song playingSong = playingFile.getCurrentSong();
        if (playingSong == null || passedTime < MIN_TIME) {
            return null;
        }

        String playingGenre = playingSong.getGenre();
        List<Song> sameGenreSongs = accessor.getSongsFromSameGenre(playingGenre);

        int index = tool.generateRandomNumberInRange(0, sameGenreSongs.size(), passedTime);
        return sameGenreSongs.get(index);
    }
}
