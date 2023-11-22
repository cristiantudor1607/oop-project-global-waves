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
    private PlayableEntity selectedEntity;
    private AudioFile loadedFile;
    private PlayerStatus state;
    private RepeatValue repeat;
    private boolean shuffle;
    private int remainedTime;

    public Player() {
        history = new HashMap<>();
        selectedEntity = null;
        loadedFile = null;
        state = PlayerStatus.NOT_IN_USE;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
        remainedTime = 0;
    }

    public void resetPlayer() {
        selectedEntity = null;
        loadedFile = null;
        state = PlayerStatus.NOT_IN_USE;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
        remainedTime = 0;
    }

    public void select(PlayableEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
        state = PlayerStatus.SELECTED;
    }

    public void play() {
        state = PlayerStatus.PLAYING;
    }

    public void pause() {
        state = PlayerStatus.PAUSED;
    }

    public void stopPlayer() {
        if (selectedEntity == null)
            return;

        if (selectedEntity.needsHistoryTrack()) {
            history.put(selectedEntity, new HistoryEntry(loadedFile, remainedTime));
        }

        resetPlayer();
    }

    public void updatePlayer(final int timeDifference) {
        if (state == PlayerStatus.PAUSED || state == PlayerStatus.NOT_IN_USE ||
            state == PlayerStatus.SELECTED)
            return;

        remainedTime -= timeDifference;
        if (remainedTime <= 0 && selectedEntity.hasNextForPlaying(loadedFile)) {
            loadedFile = selectedEntity.getNextForPlaying(loadedFile);
            remainedTime += loadedFile.getDuration();
            return;
        }

        if (remainedTime <= 0) {
            removeFromHistory();
            resetPlayer();
        }
    }

    public void removeFromHistory() {
        if (hasHistoryEntry())
            history.remove(selectedEntity);
    }

    public boolean hasHistoryEntry() {
        return history.containsKey(selectedEntity);
    }

    public void setDefaultLoadOptions() {
        state = PlayerStatus.PLAYING;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
    }

    private void loadFromHistory() {
        loadedFile = history.get(selectedEntity).getFile();
        remainedTime = history.get(selectedEntity).getRemainedTime();
        setDefaultLoadOptions();
    }

    public void load() {
        if (hasHistoryEntry()) {
            loadFromHistory();
            return;
        }

        AudioFile newAudiofile = selectedEntity.getPlayableFile();
        int duration = newAudiofile.getDuration();
        if (selectedEntity.needsHistoryTrack())
            history.put(selectedEntity, new HistoryEntry(newAudiofile, duration));

        loadedFile = newAudiofile;
        remainedTime = duration;
        setDefaultLoadOptions();
    }

    public boolean hasEmptySource() {
        return selectedEntity.isEmptyPlayableFile();
    }

    public boolean hasSourceSelected() {
        return !(state == PlayerStatus.NOT_IN_USE);
    }

    public boolean hasSourceLoaded() {
        return state == PlayerStatus.PLAYING || state == PlayerStatus.PAUSED;
    }

    public boolean isPlaying() {
        return state == PlayerStatus.PLAYING;
    }

    public void printPlayer() {
        if (selectedEntity == null)
            System.out.println("No AudioFile");
        else
            System.out.println(selectedEntity.getName());

        System.out.println(state);
        System.out.println(repeat);
        System.out.println(shuffle);
        System.out.println(remainedTime);
    }

}
