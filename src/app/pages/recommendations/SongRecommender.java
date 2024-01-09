package app.pages.recommendations;

import app.player.entities.AudioFile;
import app.player.entities.Player;
import app.player.entities.Song;
import app.properties.PlayableEntity;
import app.users.AdminBot;
import app.utilities.HelperTool;

import java.util.List;

public class SongRecommender extends Recommender {
    private final AdminBot adminBot;
    private final HelperTool tool;
    private final AudioFile playingFile;
    private final int passedTime;

    public SongRecommender(final Player player) {
        adminBot = new AdminBot();
        tool = HelperTool.getInstance();

        playingFile = player.getPlayingFile();
        passedTime = playingFile.getDuration() - player.getRemainedTime();
    }

    /**
     * Returns the found recommended song.
     *
     * @return The song recommended, if it can be found, {@code null otherwise}
     */
    @Override
    public PlayableEntity getRecommendation() {
        Song playingSong = playingFile.getCurrentSong();
        if (playingSong == null || passedTime < 30) {
            return null;
        }

        String playingGenre = playingSong.getGenre();
        List<Song> sameGenreSongs = adminBot.getSongsFromSameGenre(playingGenre);

        int index = tool.generateRandomNumberInRange(0, sameGenreSongs.size(), passedTime);
        return sameGenreSongs.get(index);
    }
}
