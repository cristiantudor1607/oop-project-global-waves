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

    /**
     * Resets the player - set all the fields to default
     */
    public void resetPlayer() {
        // unshuffle to reload the numerical order
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

    /**
     * Sets the player fields to default options on load
     */
    public void setDefaultLoadOptions() {
        state = PlayerStatus.PLAYING;
        repeat =  0;
        shuffle = false;
    }

    /**
     * Plays next file
     * @return true, if it succeeds, false if it has no next
     */
    public boolean playNext() {
        // If next file is null, then there is no next, so it should
        // stop
        if  (nextFile == null) {
            resetPlayer();
            return false;
        }

        // If the next file to be played is the same as the current file,
        // then there are 2 possibilities : the player is either in repeat state
        // 1 or 2. If it is in 2, it has to remain in 2, but if it is in 1,
        // it should go to 0 (if there were a Playlist with state 1, the files
        // would have been different)
        if (playingFile == nextFile && repeat == 1)
            repeat = 0;

        prevFile = playingFile;
        playingFile = nextFile;
        nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
        state = PlayerStatus.PLAYING;
        remainedTime = playingFile.getDuration();
        return true;
    }

    /**
     * Plays previous file
     * @param timeDiff The time passed from last command / action
     */
    public void playPrev(int timeDiff) {
        remainedTime -= timeDiff;

        // if it get prev at moment 0, and it has to go to the previous
        // track
        if (remainedTime == playingFile.getDuration()) {

            // If it has no prev
            if (prevFile == null) {
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
            // Repeat Once
            if (playingFile == nextFile && repeat == 1) {
                repeat = 0;
                // Basically the playingFile becomes nextFile, but they're equal
                nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
            }

            state = PlayerStatus.PLAYING;
            remainedTime = playingFile.getDuration();
        }
    }

    /**
     * Skips 90 seconds, or starts the next file, if it has less than 90
     * minutes remaining
     */
    public void skipForward() {
        // If there are less than 90 seconds remaining, start the next episode
        if (remainedTime <= 90 ) {
            prevFile = playingFile;
            // If Repeat Once is activated, it remains on the same file, but
            // repeat is set to No Repeat
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

    /**
     * Go back by 90 seconds, or starts the file from the beginning if there
     * are no 90 minutes passed
     */
    public void rewoundBackward() {
        if (playingFile.getDuration() - remainedTime < 90)
            remainedTime = playingFile.getDuration();
        else
            remainedTime += 90;
    }

    /**
     * Select a source
     * @param selectedEntity The entity to be selected
     */
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

    /**
     * Updates the player when going from one command to another
     * @param timeDiff The time passed between the two templates
     */
    public void updateAfterTimeskip(int timeDiff) {
        // If a short time has passed
        if (remainedTime - timeDiff > 0) {
            remainedTime -= timeDiff;
            return;
        }

        remainedTime -= timeDiff;

        // If the time passed is exactly the ending of the song
        if (remainedTime == 0) {
            // Change the Repeat Once to No Repeat if necessary
            if (playingFile == nextFile && repeat == 1)
                repeat = 0;

            // Go to the next file
            prevFile = playingFile;
            playingFile = nextFile;

            // Check if there was a file waiting to be played
            if (playingFile == null) {
                resetPlayer();
                return;
            }

            // Calculate the new next and set the remainedTime
            nextFile = selectedSource.getNextForPlaying(playingFile, repeat);
            remainedTime = playingFile.getDuration();
            return;
        }

        if (remainedTime < 0) {
            // Change Repeat Once to No Repeat if necessary
            if (playingFile == nextFile && repeat == 1)
                repeat = 0;

            // Go one by one, until the remainedTime becomes positive
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

}
