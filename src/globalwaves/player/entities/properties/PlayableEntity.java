package globalwaves.player.entities.properties;

import globalwaves.commands.enums.FollowExit;
import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Player;

public interface PlayableEntity {
    String getName();
    int getDuration();
    boolean isEmptyPlayableFile();
    AudioFile getPlayableFile();
    boolean needsHistoryTrack();
    boolean hasNextForPlaying(AudioFile currentFile);
    AudioFile getNextForPlaying(AudioFile currentFile);
    FollowExit.code follow(String username);
}
