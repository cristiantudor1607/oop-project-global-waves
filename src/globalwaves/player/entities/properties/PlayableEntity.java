package globalwaves.player.entities.properties;

import globalwaves.commands.enums.FollowExit;
import globalwaves.commands.enums.ShuffleExit;
import globalwaves.player.entities.AudioFile;

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
    AudioFile getNextForPlaying(AudioFile currentFile, int repeatValue);
    AudioFile getPrevForPlaying(AudioFile currentFile, int repeatValue);
    ShuffleExit.code shuffle(int seed);
    ShuffleExit.code unshuffle();
}
