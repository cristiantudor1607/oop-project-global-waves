package globalwaves.player.entities;

import globalwaves.player.entities.library.HelperTool;
import globalwaves.player.entities.properties.PlayableEntity;
import globalwaves.player.entities.utilities.HistoryEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Player {
    public enum PlayerStatus {
        PLAYING,
        PAUSED,
        SELECTED,
        UNUSED,
    }
    private final static int SECONDS = 90;

    private final Map<PlayableEntity, HistoryEntry> history;
    private List<Integer> playingOrder;
    private PlayableEntity selectedSource;
    private AudioFile playingFile;
    private Player.PlayerStatus state;
    private int repeat;
    @Setter
    private boolean shuffle;
    private boolean freeze;
    private int remainedTime;
    private int currentIndex;

    public Player() {
        history = new HashMap<>();
        playingOrder = new ArrayList<>();
        state = PlayerStatus.UNUSED;
        freeze = false;
        currentIndex = -1;
    }

    /**
     * Puts the player in playing state
     */
    public void play() {
        state = PlayerStatus.PLAYING;
    }

    /**
     * Checks if the player has an audio file playing
     * @return true, if the player is playing something, false otherwise
     */
    public boolean isPlaying() {
        return state == PlayerStatus.PLAYING;
    }

    /**
     * Puts the player in pause state
     */
    public void pause() {
       state = PlayerStatus.PAUSED;
    }

    /**
     * Freezes the player when a user is sent offline
     */
    public void freeze() {
        freeze = true;
    }

    /**
     * Unfreezes the player when a user is sent back online
     */
    public void unfreeze() {
        freeze = false;
    }

    /**
     * Changes the repeat value of the player
     * It goes from 0 to 1, from 1 to 2, and form 2 to 0
     */
    public void changeRepeatState() {
        repeat = (repeat + 1) % 3;
    }

    public boolean shuffle(final int seed) {
        if (!selectedSource.isPlaylist())
            return false;

        int currentElement = playingOrder.get(currentIndex);

        Collections.shuffle(playingOrder, new Random(seed));
        currentIndex = playingOrder.indexOf(currentElement);
        shuffle = true;
        return true;
    }

    public boolean unshuffle() {
        if (!selectedSource.isPlaylist())
            return false;

        int currentElement = playingOrder.get(currentIndex);
        playingOrder = getDefaultOrder(playingOrder.size());
        currentIndex = playingOrder.indexOf(currentElement);
        shuffle = false;
        return true;
    }

    /**
     * Resets the player fields to default values
     */
    public void resetPlayer() {
        playingOrder = new ArrayList<>();
        selectedSource = null;
        playingFile = null;
        state = PlayerStatus.UNUSED;
        repeat = 0;
        shuffle = false;
        remainedTime = 0;
    }

    /**
     * Stops the player and saves a record in history, if necessary
     */
    public void stopPlayer() {
        if (selectedSource == null)
            return;

        if (selectedSource.needsHistoryTrack()) {
            history.put(selectedSource, new HistoryEntry(playingFile, remainedTime));
        }

        resetPlayer();
    }

    /**
     * Returns the index of the next file (compared with the playing one), considering the
     * shuffling array and the repeat value
     * @return An index between 0 and the number of files, or -1, if there's no next file
     */
    public int getNextAudioFileIndex() {
        // If repeat is 2, then return the same thing
        if (repeat == 2)
            return currentIndex;

        // If repeat is 1, and the source is a playlist, then check if
        // the index goes out of bounds. In this case, start over
        if (repeat == 1 && selectedSource.isPlaylist()) {
            if (currentIndex + 1 >= playingOrder.size()) {
                currentIndex = 0;
                return currentIndex;
            }

            currentIndex += 1;
            return currentIndex;
        }

        // If repeat is 1, then we give the same track one more time. After that, repeat becomes
        // 0
        if (repeat == 1) {
            repeat = 0;
            return currentIndex;
        }

        // If repeat is 0, checks if it goes out of bounds. If true, return -1
        if (currentIndex + 1 >= playingOrder.size())
            return -1;

        currentIndex += 1;
        return currentIndex;
    }

    /**
     * Gets the index of the previous files (compared to the playing one), considering the shuffling
     * array and the repeat value
     * @return An index between 0 and the number of files, or -1, if there's no previous file
     */
    public int getPrevAudioFileIndex() {
        // Here it was an if for repeat = 2, to return the same thing on repeat = 2, but the
        // checker doesn't work in this way

        // If repeat is 1, and the source is a playlist, then check if the index goes out
        // of bounds. In this case, get the ending song
        if (repeat == 1 && selectedSource.isPlaylist()) {
            if (currentIndex - 1 < 0) return currentIndex;

            currentIndex -= 1;
            return currentIndex;
        }

        // If repeat is 1, then we give the same track one more time. After that, repeat becomes
        // 0
        if (repeat == 1) {
            repeat = 0;
            return currentIndex;
        }

        // If repeat is 0, checks if it goes out of bounds. If true, return -1
        if (currentIndex - 1 < 0)
            return -1;

        currentIndex -= 1;
        return currentIndex;
    }

    public boolean playNext() {
        int nextIndex = getNextAudioFileIndex();
        if (nextIndex == -1) {
            resetPlayer();
            return false;
        }

        int realIndex = playingOrder.get(currentIndex);

        playingFile = selectedSource.getAudioFileAtIndex(realIndex);
        remainedTime = playingFile.getDuration();
        state = PlayerStatus.PLAYING;
        return true;
    }

    public boolean playPrev(final int timeDiff) {
        remainedTime -= timeDiff;

        // If at least one second has passed, play again the current track
        if (remainedTime < playingFile.getDuration()) {
            state = PlayerStatus.PLAYING;
            remainedTime = playingFile.getDuration();
            return true;
        }

        // Save the old index
        int indexSave = currentIndex;

        // Try to get the previous file. If it doesn't have one,
        // it'll return -1, and we have to play the same file again,
        // and keep the index
        int prevIndex = getPrevAudioFileIndex();
        if (prevIndex == -1) {
            state = PlayerStatus.PLAYING;
            remainedTime = playingFile.getDuration();
            currentIndex = indexSave;
            return true;
        }

        int realIndex = playingOrder.get(currentIndex);

        playingFile = selectedSource.getAudioFileAtIndex(realIndex);
        remainedTime = playingFile.getDuration();
        state = PlayerStatus.PLAYING;
        return true;
    }

    public void skip() {
        // If there are less than 90 seconds remaining, start the next episode
        if (remainedTime <= SECONDS) {
            // TODO: poate sa fie de aici ceva
            int nextIndex = getNextAudioFileIndex();
            if (nextIndex == -1) {
                resetPlayer();
                return;
            }

            int realIndex = playingOrder.get(nextIndex);
            playingFile = selectedSource.getAudioFileAtIndex(realIndex);
            remainedTime = playingFile.getDuration();
            state = PlayerStatus.PLAYING;
            return;
        }

        remainedTime -= SECONDS;
    }

    public void rewound() {
        if (playingFile.getDuration() - remainedTime < SECONDS)
            remainedTime = playingFile.getDuration();
        else
            remainedTime += SECONDS;
    }


    /**
     * Removes the entity's history record from player's history tracker
     * @param entity The entity to be removed
     */
    public void removeFromHistory(PlayableEntity entity) {
        if (hasHistoryEntry(entity))
            history.remove(entity);
    }

    /**
     * Checks if the entity is being tracked by the player history
     * @param entity The entity to be checked
     * @return true, if the entity has a history record, false otherwise
     */
    public boolean hasHistoryEntry(PlayableEntity entity) {
        return history.containsKey(entity);
    }

    /**
     * Loads the entity data from history
     * @param entity The entity to be loaded
     * @return true, if loading was successfully, false otherwise
     */
    public boolean loadFromHistory(PlayableEntity entity) {
        HistoryEntry historyEntry = history.get(entity);
        if (historyEntry == null)
            return false;

        // Mark entity as selected source
        select(entity);
        // Load the file to be played
        playingFile = historyEntry.getFile();
        remainedTime = historyEntry.getRemainedTime();
        // Get the index of the file (actually it will only be an episode, so we
        // don't need to worry about shuffling)
        currentIndex = entity.getIndexOfFile(historyEntry.getFile());
        // Set the playing order to default
        playingOrder = getDefaultOrder(entity.getSize());

        // Set the default options of loading, and play
        shuffle = false;
        repeat = 0;
        play();

        // Remove the history record of the entity
        removeFromHistory(entity);

        return true;
    }

    /**
     * Select a source
     * @param selectedEntity The entity to be selected
     */
    public void select(PlayableEntity selectedEntity) {
        this.selectedSource = selectedEntity;
        state = PlayerStatus.SELECTED;
    }

    /**
     * Loads the entity into the player and starts playing.
     *
     * @param entity The entity to be loaded
     */
    public void load(PlayableEntity entity) {
        // Try to load from history
        boolean success = loadFromHistory(entity);
        if (success)
            return;

        select(entity);

        // Load the file
        playingFile = entity.getFirstAudioFile();
        remainedTime = playingFile.getDuration();

        // Get the index of the file. It should be 0
        currentIndex = entity.getIndexOfFile(playingFile);
        // TODO: Remove this
        assert currentIndex != 0 : "The first loaded file has not index 0!";

        // Set the default order
        playingOrder = getDefaultOrder(entity.getSize());

        // Set the default loading options
        shuffle = false;
        repeat = 0;
        play();
    }

    public List<Integer> getDefaultOrder(final int size) {
        return HelperTool.getInstance().getAscendingOrder(size);
    }

    /**
     * Checks if the player has an empty source loaded
     * @return true, if there is a playlist or an album loaded, and it doesn't
     * contain any song, or false otherwise
     */
    public boolean hasEmptySource() {
        return selectedSource.isEmptyPlayableFile();
    }

    /**
     * Checks if the player has a source at least selected
     * @return true, if the player has a source selected or loaded, false otherwise
     */
    public boolean hasNoSource() {
        return selectedSource == null;
    }

    /**
     * Checks if the player has a source selected
     * @return true, if the player has a source selected, false otherwise
     */
    public boolean hasSourceSelected() {
        return state == PlayerStatus.SELECTED;
    }

    /**
     * Checks if the player has a source loaded
     * @return true, if the player has a source loaded, false otherwise
     */
    public boolean hasSourceLoaded() {
        return state == PlayerStatus.PLAYING || state == PlayerStatus.PAUSED;
    }

    private void updateTimings(final int timeDiff) {
        if (remainedTime - timeDiff > 0) {
            remainedTime -= timeDiff;
            return;
        }

        remainedTime -= timeDiff;

        // After the time difference, the player reaches the end of the file
        if (remainedTime == 0) {
            int nextIndex = getNextAudioFileIndex();
            if (nextIndex == -1) {
                resetPlayer();
                return;
            }

            int realIndex = playingOrder.get(nextIndex);
            playingFile = selectedSource.getAudioFileAtIndex(realIndex);
            remainedTime = playingFile.getDuration();
            state = PlayerStatus.PLAYING;
            return;
        }

        // If the time difference is a bigger "hole" in time
        while (remainedTime < 0) {
            int nextIndex = getNextAudioFileIndex();
            if (nextIndex == -1) {
                resetPlayer();
                return;
            }

            int realIndex = playingOrder.get(nextIndex);
            playingFile = selectedSource.getAudioFileAtIndex(realIndex);
            remainedTime += playingFile.getDuration();
        }

    }

    public void updatePlayer(final int timeDiff) {
        if (state == PlayerStatus.UNUSED || state == PlayerStatus.PAUSED ||
                state == PlayerStatus.SELECTED )
            return;

        updateTimings(timeDiff);
    }


    /**
     * Checks if the player is playing the album, or something from the album
     * @param album The album
     * @return true, if the player has loaded the album, or a song from album,
     * false otherwise
     */
    public boolean isPlayingFromAlbum(Album album) {
        // if there's nothing selected
        if (selectedSource == null)
            return false;

        // If there's an album playing
        Album possiblyPlayingAlbum = selectedSource.getCurrentAlbum();
        if (possiblyPlayingAlbum != null)
            if (possiblyPlayingAlbum.equals(album))
                return true;

        // Check if there's something playing
        AudioFile file = selectedSource.getFirstAudioFile();
        if (file == null)
            return false;

        // Check if there's a Song playing
        Song possiblyPlayingSong = file.getCurrentSong();
        if (possiblyPlayingSong != null)
            return possiblyPlayingSong.getAlbum().equals(album.getName());

        return false;
    }
}
