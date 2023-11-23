package globalwaves.player.entities.properties;

import globalwaves.commands.enums.FollowExit;
import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.HistoryEntry;

import java.util.Map;

public interface PlayableEntity {
    String getName();
    int getDuration();
    boolean isEmptyPlayableFile();
    AudioFile getPlayableFile();
    boolean needsHistoryTrack();
//    boolean hasNextForPlaying(AudioFile currentFile);
//    AudioFile getNextForPlaying(AudioFile currentFile);
    FollowExit.code follow(String username);
    String getRepeatStateName(int repeatValue);

    boolean hasNextForPlaying(AudioFile currentFile, int repeatValue);
    AudioFile getNextForPlaying(AudioFile currentFile, int repeatValue);
    AudioFile getPrevForPlaying(AudioFile currentFile, int repeatValue);
    boolean isPartialRepeated(int repeatValue);
}
