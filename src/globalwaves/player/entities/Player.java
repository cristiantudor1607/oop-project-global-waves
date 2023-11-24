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

    private Map<PlayableEntity, HistoryEntry> history;
    private PlayableEntity selectedSource;
    private AudioFile prevFile;
    private AudioFile playingFile;
    private AudioFile nextFile;
    private PlayerStatus state;
    private int repeat;
    private boolean shuffle;
    private int remainedTime;

    public Player() {
        history = new HashMap<>();
        state = PlayerStatus.NOT_IN_USE;
    }

    public void resetPlayer() {
        selectedSource.unshuffle();
        selectedSource = null;
        prevFile = null;
        playingFile = null;
        nextFile = null;
        state = PlayerStatus.NOT_IN_USE;
        repeat = 0;
        shuffle = false;
        remainedTime = 0;
    }

    public boolean playNext() {
        if  (nextFile == null)
            return false;

        if (playingFile == nextFile && repeat == 1)
            repeat = 0;

        prevFile = playingFile;
        playingFile = nextFile;
        nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
        state = PlayerStatus.PLAYING;
        remainedTime = playingFile.getDuration();
        return true;
    }

    public void playPrev(int timeDiff) {
        remainedTime -= timeDiff;

        if (remainedTime == playingFile.getDuration()) {
            // s-ar putea sa fie nevoie sa resetez repeat-ul aici

            if (prevFile == null) {
                remainedTime = playingFile.getDuration();
                state = PlayerStatus.PLAYING;
                return;
            }

            nextFile = playingFile;
            playingFile = prevFile;
            prevFile = selectedSource.getPrevForPlaying(playingFile, repeat);
            state = PlayerStatus.PLAYING;
            remainedTime = playingFile.getDuration();
            return;
        }

        // If there is more than one second passed
        if (remainedTime < playingFile.getDuration()) {
            if (playingFile == nextFile && repeat == 1) {
                repeat = 0;
                nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
            }

            state = PlayerStatus.PLAYING;
            remainedTime = playingFile.getDuration();
        }
    }

    public void skipForward() {
        // If there are less than 90 seconds remaining, start the next episode
        if (remainedTime <= 90 ) {
            prevFile = playingFile;
            if (playingFile == nextFile && repeat == 1)
                repeat = 0;
            else
                playingFile = nextFile;

            nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
            state = PlayerStatus.PLAYING;
            remainedTime = playingFile.getDuration();
            return;
        }

        remainedTime -= 90;
    }

    public void rewoundBackward() {
        if (playingFile.getDuration() - remainedTime < 90)
            remainedTime = playingFile.getDuration();
        else
            remainedTime += 90;
    }


    public void setDefaultLoadOptions() {
        state = PlayerStatus.PLAYING;
        repeat =  0;
        shuffle = false;
    }

    public void select(PlayableEntity selectedEntity) {
        this.selectedSource = selectedEntity;
        state = PlayerStatus.SELECTED;
    }

    public void changeRepeatState() {
        repeat = (repeat + 1) % 3;
        nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
    }

    public void changeOrderAfterShuffle() {
        prevFile = selectedSource.getPrevForPlaying(playingFile, repeat);
        nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
    }

    public void updateAfterTimeskip(int timeDiff) {
        if (remainedTime - timeDiff > 0) {
            remainedTime -= timeDiff;
            return;
        }

        remainedTime -= timeDiff;
        if (remainedTime == 0) {
            // for songs and podcasts repeat once is temporary
            if (playingFile == nextFile && repeat == 1)
                repeat = 0;

            prevFile = playingFile;
            playingFile = nextFile;
            if (playingFile == null) {
                resetPlayer();
                return;
            }
            nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
            remainedTime = playingFile.getDuration();
            return;
        }

        if (remainedTime < 0) {
            if (playingFile == nextFile && repeat == 1)
                repeat = 0;

            while (remainedTime < 0) {
                prevFile = playingFile;
                playingFile = nextFile;
                if (playingFile == null) {
                    resetPlayer();
                    return;
                }
                nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
                remainedTime += playingFile.getDuration();
            }
        }
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

        updateAfterTimeskip(timeDifference);
    }

    public void removeFromHistory() {
        if (hasHistoryEntry())
            history.remove(selectedSource);
    }

    public boolean hasHistoryEntry() {
        return history.containsKey(selectedSource);
    }

    private void loadFromHistory() {
        playingFile = history.get(selectedSource).getFile();
        prevFile = selectedSource.getPrevForPlaying(playingFile, 0);
        nextFile = selectedSource.getNextForPlaying(playingFile, 0);
        remainedTime = history.get(selectedSource).getRemainedTime();
        removeFromHistory();
        setDefaultLoadOptions();
    }

    public void load() {
        if (hasHistoryEntry()) {
            loadFromHistory();
            return;
        }

        AudioFile newAudiofile = selectedSource.getPlayableFile();
        int duration = newAudiofile.getDuration();

        playingFile = newAudiofile;
        nextFile = selectedSource.getNextForPlaying(playingFile, 0);
        remainedTime = duration;
        setDefaultLoadOptions();
    }

    public boolean hasEmptySource() {
        return selectedSource.isEmptyPlayableFile();
    }

    public boolean hasNoSource() {
        return selectedSource == null;
    }

    public boolean hasSourceSelected() {
        return state == PlayerStatus.SELECTED;
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
