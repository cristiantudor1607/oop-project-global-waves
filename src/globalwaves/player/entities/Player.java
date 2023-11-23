package globalwaves.player.entities;

import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class Player {
    public enum PlayerStatus {
        PLAYING,
        PAUSED,
        SELECTED,
        NOT_IN_USE,
    }

    public enum RepeatValue {
        NO_REPEAT,
        REPEAT_ONCE,
        REPEAT_INF,
        REPEAT_ALL,
        REPEAT_CURR,
    }

    private Map<PlayableEntity, HistoryEntry> history;
    private PlayableEntity selectedSource;
    private AudioFile playingFile;
    private PlayerStatus state;
    private RepeatValue repeat;
    private boolean shuffle;
    private int remainedTime;

    public Player() {
        history = new HashMap<>();
        selectedSource = null;
        playingFile = null;
        state = PlayerStatus.NOT_IN_USE;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
        remainedTime = 0;
    }

    public void resetPlayer() {
        selectedSource = null;
        playingFile = null;
        state = PlayerStatus.NOT_IN_USE;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
        remainedTime = 0;
    }

    public void select(PlayableEntity selectedEntity) {
        this.selectedSource = selectedEntity;
        state = PlayerStatus.SELECTED;
    }

    public void play() {
        state = PlayerStatus.PLAYING;
    }

    public void pause() {
        state = PlayerStatus.PAUSED;
    }

    public void stopPlayer() {
        if (selectedSource == null)
            return;

        if (selectedSource.needsHistoryTrack()) {
            history.put(selectedSource, new HistoryEntry(playingFile, remainedTime));
        }

        resetPlayer();
    }

    public void updatePlayer(final int timeDifference) {
        if (state == PlayerStatus.PAUSED || state == PlayerStatus.NOT_IN_USE ||
            state == PlayerStatus.SELECTED)
            return;

        remainedTime -= timeDifference;
        if (remainedTime <= 0 && selectedSource.hasNextForPlaying(playingFile)) {
            playingFile = selectedSource.getNextForPlaying(playingFile);
            remainedTime += playingFile.getDuration();
            return;
        }

        if (remainedTime <= 0) {
            removeFromHistory();
            resetPlayer();
        }
    }

    public void removeFromHistory() {
        if (hasHistoryEntry())
            history.remove(selectedSource);
    }

    public boolean hasHistoryEntry() {
        return history.containsKey(selectedSource);
    }

    public void setDefaultLoadOptions() {
        state = PlayerStatus.PLAYING;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
    }

    private void loadFromHistory() {
        playingFile = history.get(selectedSource).getFile();
        remainedTime = history.get(selectedSource).getRemainedTime();
        setDefaultLoadOptions();
    }

    public void load() {
        if (hasHistoryEntry()) {
            loadFromHistory();
            return;
        }

        AudioFile newAudiofile = selectedSource.getPlayableFile();
        int duration = newAudiofile.getDuration();
        if (selectedSource.needsHistoryTrack())
            history.put(selectedSource, new HistoryEntry(newAudiofile, duration));

        playingFile = newAudiofile;
        remainedTime = duration;
        setDefaultLoadOptions();
    }

    public boolean hasEmptySource() {
        return selectedSource.isEmptyPlayableFile();
    }

    public boolean hasNoSource() {
        return selectedSource == null;
    }

    public boolean hasSourceLoaded() {
        return state == PlayerStatus.PLAYING || state == PlayerStatus.PAUSED;
    }

    public boolean isPlaying() {
        return state == PlayerStatus.PLAYING;
    }

    public void printPlayer() {
        if (selectedSource == null)
            System.out.println("No AudioFile");
        else
            System.out.println(selectedSource.getName());

        System.out.println(state);
        System.out.println(repeat);
        System.out.println(shuffle);
        System.out.println(remainedTime);
    }

}
