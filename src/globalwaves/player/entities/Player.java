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
    private Map<Podcast, Integer> podcastHistory;
    // TODO : selectedAudio cu selectedFile trebuie inversate ca nume + schimba numele
    // in selectedEntity
    private PlayableEntity selectedAudio;
    private AudioFile selectedFile;
    private PlayerStatus state;
    private RepeatValue repeat;
    private boolean shuffle;
    private int remainedTime;

    public Player() {
        podcastHistory = new HashMap<>();
        selectedAudio = null;
        state = PlayerStatus.NOT_IN_USE;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
        remainedTime = 0;
    }

    public void resetPlayer() {
        selectedAudio = null;
        state = PlayerStatus.NOT_IN_USE;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
        remainedTime = 0;
    }


    public void updatePlayer(final int timeDifference) {
        if (state == PlayerStatus.PAUSED || state == PlayerStatus.NOT_IN_USE ||
            state == PlayerStatus.SELECTED || remainedTime == 0)
            return;

        remainedTime -= timeDifference;
        if (remainedTime <= 0) {
            resetPlayer();
        }
    }

    public void selectPlayableEntity(PlayableEntity newLoad) {
        selectedAudio = newLoad;
        state = PlayerStatus.SELECTED;
        repeat = RepeatValue.NO_REPEAT;
        shuffle = false;
    }

    public boolean hasEmptySource() {
        return selectedAudio.isEmptyPlayableFile();
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
        if (selectedAudio == null)
            System.out.println("No AudioFile");
        else
            System.out.println(selectedAudio.getName());

        System.out.println(state);
        System.out.println(repeat);
        System.out.println(shuffle);
        System.out.println(remainedTime);
    }

}
