package app.player.entities;

import app.pages.Page;
import app.users.User;
import app.utilities.HelperTool;
import app.properties.PlayableEntity;
import app.utilities.HistoryEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
public class Player {
    public enum PlayerStatus {
        PLAYING,
        PAUSED,
        SELECTED,
        UNUSED,
    }
    private final int seconds = 90;
    private final int statesNo = 3;

    private final User user;
    private final Map<PlayableEntity, HistoryEntry> history;
    private List<Integer> playingOrder;
    private PlayableEntity selectedSource;
    private AudioFile playingFile;
    private AudioFile nextAd;
    private Player.PlayerStatus state;
    private int repeat;
    @Setter
    private boolean shuffle;
    private boolean freeze;
    private int remainedTime;
    private int currentIndex;

    public Player(final User user) {
        this.user = user;
        history = new HashMap<>();
        playingOrder = new ArrayList<>();
        state = PlayerStatus.UNUSED;
        freeze = false;
        currentIndex = -1;
    }

    /**
     * Enqueues the ad to the next list. <b>The next list does not exist for this
     * implementation of the player, it's just a logical concept.</b> It will play the ad
     * after the current song.
     * @param ad The ad to be played
     */
    public void insertAdBreak(final AudioFile ad) {
        nextAd = ad;
    }

    /**
     * Checks if there is an ad waiting to be played next.
     * @return {@code true}, if there is an ad that should be played, {@code false}
     * otherwise
     */
    public boolean hasAdBreakNext() {
        return nextAd != null;
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
        repeat = (repeat + 1) % statesNo;
    }

    /**
     * Shuffles the collection, if there is a playlist playing.
     * @param seed The seed for randomness
     * @return {@code true}, if there is a playlist playing, and it was shuffled, {@code false}
     * otherwise
     */
    public boolean shuffle(final int seed) {
        if (!selectedSource.isPlaylist()) {
            return false;
        }

        int currentElement = playingOrder.get(currentIndex);

        Collections.shuffle(playingOrder, new Random(seed));
        currentIndex = playingOrder.indexOf(currentElement);
        shuffle = true;
        return true;
    }

    /**
     * Unshuffles the collection, if there is a playlist playing.
     * @return {@code true}, if there is a playlist playing, and it was unshuffled, {@code false}
     * otherwise
     */
    public boolean unshuffle() {
        if (!selectedSource.isPlaylist()) {
            return false;
        }

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

        // LANDMARK: Maybe this works for ads
        nextAd = null;
    }

    /**
     * Stops the player and saves a record in history, if necessary
     */
    public void stopPlayer() {
        if (selectedSource == null) {
            return;
        }

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
        if (repeat == 2) {
            return currentIndex;
        }

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
        if (currentIndex + 1 >= playingOrder.size()) {
            return -1;
        }

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
            if (currentIndex - 1 < 0) {
                return currentIndex;
            }

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
        if (currentIndex - 1 < 0) {
            return -1;
        }

        currentIndex -= 1;
        return currentIndex;
    }

    /**
     * PLays the next audio file, if possible.
     * @return {@code true}, if it successfully changed to the next file, {@code false},
     * if there is no next file
     */
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

    /**
     * Plays the previous file, or it goes to the beginning of current track.
     * @param timeDiff The time difference between previous action and current action.
     *                 It matters, because if there are 2 {@code playPrev} one after another,
     *                 the second one brings the previous file to the player, otherwise,
     *                 the current file is played from the beginning.
     */
    public void playPrev(final int timeDiff) {
        remainedTime -= timeDiff;

        // If at least one second has passed, play again the current track
        if (remainedTime < playingFile.getDuration()) {
            state = PlayerStatus.PLAYING;
            remainedTime = playingFile.getDuration();
            return;
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
            return;
        }

        int realIndex = playingOrder.get(currentIndex);

        playingFile = selectedSource.getAudioFileAtIndex(realIndex);
        remainedTime = playingFile.getDuration();
        state = PlayerStatus.PLAYING;
    }

    /**
     * Goes forward by 90 seconds, or plays the next file, if there are no 90
     * seconds remained from current one.
     */
    public void skip() {
        if (playingFile.isAd()) {
            // debugmessage: Remove this message after you are sure
            System.out.println("Ad playing!");
            return;
        }

        // If there are less than 90 seconds remaining, start the next episode
        if (remainedTime <= seconds) {
            int nextIndex = getNextAudioFileIndex();
            if (nextIndex != -1) {
                int realIndex = playingOrder.get(nextIndex);
                playingFile = selectedSource.getAudioFileAtIndex(realIndex);
                remainedTime = playingFile.getDuration();
                state = PlayerStatus.PLAYING;
            } else {
                resetPlayer();
            }

            return;
        }

        remainedTime -= seconds;
    }

    /**
     * Goes backward by 90 seconds, or plays the file from the beginning, if there
     * are no 90 seconds passed.
     */
    public void rewound() {
        if (playingFile.getDuration() - remainedTime < seconds) {
            remainedTime = playingFile.getDuration();
        } else {
            remainedTime += seconds;
        }
    }


    /**
     * Removes the entity's history record from player's history tracker.
     * @param entity The entity to be removed
     */
    public void removeFromHistory(final PlayableEntity entity) {
        if (hasHistoryEntry(entity)) {
            history.remove(entity);
        }
    }

    /**
     * Checks if the entity is being tracked by the player history.
     * @param entity The entity to be checked
     * @return true, if the entity has a history record, false otherwise
     */
    public boolean hasHistoryEntry(final PlayableEntity entity) {
        return history.containsKey(entity);
    }

    /**
     * Loads the entity data from history.
     * @param entity The entity to be loaded
     * @return {@code true}, if loading was successfully, {@code false} otherwise
     */
    public boolean loadFromHistory(final PlayableEntity entity) {
        HistoryEntry historyEntry = history.get(entity);
        if (historyEntry == null) {
            return false;
        }

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

        user.trackFile(playingFile);
        return true;
    }

    /**
     * Select a source.
     * @param selectedEntity The entity to be selected
     */
    public void select(final PlayableEntity selectedEntity) {
        this.selectedSource = selectedEntity;
        state = PlayerStatus.SELECTED;
    }

    /**
     * Loads the entity and starts playing.
     *
     * @param entity The entity to be loaded
     */
    public void load(final PlayableEntity entity) {
        // Try to load from history
        boolean success = loadFromHistory(entity);
        if (success) {
            return;
        }

        select(entity);

        // Load the file
        playingFile = entity.getFirstAudioFile();
        remainedTime = playingFile.getDuration();

        // Get the index of the file. It should be 0
        currentIndex = entity.getIndexOfFile(playingFile);

        // Set the default order
        playingOrder = getDefaultOrder(entity.getSize());

        // Set the default loading options
        shuffle = false;
        repeat = 0;
        play();

        // LANDMARK
        user.trackFile(playingFile);
        //System.out.println("New play for " + playingFile.getName() + " by " + user.getName());
    }

    /**
     * Returns the default order for playing any collection.
     * @param size The size of the collection
     * @return A list with all numbers from {@code 0} to {@code size - 1}, in ascending order
     */
    public List<Integer> getDefaultOrder(final int size) {
        return HelperTool.getInstance().getIndexesList(size);
    }

    /**
     * Checks if the player has an empty source loaded.
     * @return {@code true}, if there is a playlist or an album loaded, and it doesn't
     * contain any song, or {@code false} otherwise
     */
    public boolean hasEmptySource() {
        return selectedSource.isEmptyPlayableFile();
    }

    /**
     * Checks if the player has no source at least selected.
     * @return {@code true}, if the player has no source selected, {@code false} otherwise
     */
    public boolean hasNoSource() {
        return selectedSource == null;
    }

    /**
     * Checks if the player has a source selected.
     * @return {@code true}, if the player has a source selected, {@code false} otherwise
     */
    public boolean hasSourceSelected() {
        return state == PlayerStatus.SELECTED;
    }

    /**
     * Checks if the player has a source loaded.
     * @return {@code true}, if the player has a source loaded, {@code false} otherwise
     */
    public boolean hasSourceLoaded() {
        return state == PlayerStatus.PLAYING || state == PlayerStatus.PAUSED;
    }

    /**
     * Updates the remained playing time after a time skip.
     * @param timeDiff The time passed
     */
    private void updateTimings(final int timeDiff) {
        if (remainedTime - timeDiff > 0) {
            remainedTime -= timeDiff;
            return;
        }

        remainedTime -= timeDiff;

        // TODO: Ar merge stearsa partea asta
        // After the time difference, the player reaches the end of the file
        if (remainedTime == 0) {
            // The currentIndex remains set if getNextAudioFileIndex or
            // getPrevAudioFileIndex aren't called
            if (hasAdBreakNext()) {
                // LANDMARK: AD
                user.getMoneyTracker().adBreak();
                playingFile = nextAd;
                remainedTime = nextAd.getDuration();
                nextAd = null;
                return;
            }

            int nextIndex = getNextAudioFileIndex();
            if (nextIndex == -1) {
                resetPlayer();
                return;
            }

            int realIndex = playingOrder.get(nextIndex);
            playingFile = selectedSource.getAudioFileAtIndex(realIndex);
            remainedTime = playingFile.getDuration();
            state = PlayerStatus.PLAYING;

            // LANDMARK
            user.trackFile(playingFile);
            //System.out.println("New play for " + playingFile.getName() + " by " + user.getName());
            return;
        }

        // If the time difference is a bigger "hole" in time
        while (remainedTime <= 0) {
            if (hasAdBreakNext()) {
                // LANDMARK: AD
                user.getMoneyTracker().adBreak();
                playingFile = nextAd;
                remainedTime += nextAd.getDuration();
                nextAd = null;
                continue;
            }

            // Enqueue the song for monetization
            int nextIndex = getNextAudioFileIndex();
            if (nextIndex == -1) {
                resetPlayer();
                return;
            }

            int realIndex = playingOrder.get(nextIndex);
            playingFile = selectedSource.getAudioFileAtIndex(realIndex);
            remainedTime += playingFile.getDuration();

            // LANDMARK
            user.trackFile(playingFile);
            //System.out.println("New play for " + playingFile.getName() + " by " + user.getName());
        }

    }

    /**
     * Updates the player after a time skip, if it isn't stopped.
     * @param timeDiff The time passed
     */
    public void updatePlayer(final int timeDiff) {
        if (state == PlayerStatus.UNUSED || state == PlayerStatus.PAUSED
                || state == PlayerStatus.SELECTED) {
            return;
        }

        updateTimings(timeDiff);
    }


    /**
     * Checks if the player is playing the album, or something from the album.
     * @param album The album to have something playing
     * @return {@code true}, if the player has loaded the album, or a song from album,
     * {@code false} otherwise
     */
    public boolean isPlayingFromAlbum(final Album album) {
        // if there's nothing selected
        if (selectedSource == null) {
            return false;
        }

        // If there's an album playing
        Album possiblyPlayingAlbum = selectedSource.getCurrentAlbum();
        if (possiblyPlayingAlbum != null) {
            if (possiblyPlayingAlbum.equals(album)) {
                return true;
            }
        }

        // Check if there's something playing
        AudioFile file = selectedSource.getFirstAudioFile();
        if (file == null) {
            return false;
        }

        // Check if there's a Song playing
        Song possiblyPlayingSong = file.getCurrentSong();
        if (possiblyPlayingSong != null) {
            return possiblyPlayingSong.getAlbumName().equals(album.getName());
        }

        return false;
    }

    /**
     * Returns the page of the artist / host of the playing song / episode.
     *
     * @return The page of the artist / host, if there is something playing, {@code null}
     * otherwise
     */
    public Page getCurrentPage() {
        return playingFile == null ? null : playingFile.getCreatorPage();
    }
}
