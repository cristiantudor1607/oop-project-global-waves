package globalwaves.player.entities.properties;

import globalwaves.player.entities.AudioFile;

public interface PlayableEntity {
    String getName();
    int getDuration();
    boolean isEmptyPlayableFile();
    AudioFile getPlayableFile();

    boolean needsHistoryTrack();
    boolean hasNextForPlaying(AudioFile currentFile);
    AudioFile getNextForPlaying(AudioFile currentFile);
}
