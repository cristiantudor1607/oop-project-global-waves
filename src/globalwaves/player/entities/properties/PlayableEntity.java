package globalwaves.player.entities.properties;

import globalwaves.commands.enums.exitstats.stageone.FollowExit;
import globalwaves.commands.enums.exitstats.stageone.ShuffleExit;
import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.User;

import java.util.List;

public interface PlayableEntity {
    /**
     * Checks if the entity is an empty file
     * @return true, if it is an empty file, false otherwise
     */
    boolean isEmptyPlayableFile();

    /**
     * Gets the first file to be loaded and played
     * @return The first file of the entity
     */
    AudioFile getPlayableFile();

    /**
     * Checks if the entity needs to save its data to the history
     * @return true, if it needs, false otherwise
     */
    boolean needsHistoryTrack();

    /**
     * Returns the repeatValue formatted as String
     * @param repeatValue The repeatValue to be converted
     * @return formatted repeatValue, null if repeatValue < 0 or repeatValue > 2
     */
    String getRepeatStateName(int repeatValue);

    /**
     * Returns the next AudioFile for playing, based on the repeatValue. The repeatValue 1 acts as
     * 2 because it will be modified by the player to 0, after playing the second time
     * @param currentFile The playing file
     * @param repeatValue The current repeat state
     * @return The next AudioFile to be played in the player
     */
    AudioFile getNextForPlaying(AudioFile currentFile, int repeatValue);

    /**
     * Returns the previous AudioFile for playing, based on the repeatValue. The repeatValue 1 acts
     * as 2 because it will be modified by the player to 0, after playing the second time
     * @param currentFile The playing file
     * @param repeatValue The current repeat state
     * @return The previous AudioFile to be played in the player
     */
    AudioFile getPrevForPlaying(AudioFile currentFile, int repeatValue);
    ShuffleExit.Status shuffle(int seed);
    ShuffleExit.Status unshuffle();
    boolean cantGoForwardOrBackward();
    String getName();

    Playlist getWorkingOnPlaylist();
    /**
     * Returns the duration of the entity
     * @return The duration of the entity
     */
    int getDuration();
}
