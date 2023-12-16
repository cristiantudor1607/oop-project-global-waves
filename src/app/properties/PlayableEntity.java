package app.properties;

import app.exitstats.stageone.ShuffleExit;
import app.player.entities.Album;
import app.player.entities.AudioFile;
import app.player.entities.Playlist;

public interface PlayableEntity {
    /**
     * Returns the name of the entity
     * @return The name of the entity
     */
    String getName();

    /**
     * Returns the size of the entity
     * @return The size of the entity
     */
    int getSize();

    /**
     * Returns the duration of the entity
     * @return The duration of the entity
     */
    int getDuration();

    /**
     * Returns the creation time of the entity
     * @return The creation time, if the entity stores it, or 0 otherwise
     */
    int getCreationTime();

    /**
     * Returns the AudioFile at index specified
     * @param index The index of the file
     * @return null, if the index is out of bounds, the AudioFile otherwise
     */
    AudioFile getAudioFileAtIndex(int index);

    /**
     * Returns the index of the file in the list that stores it
     * @param file The file to be inspected.
     * @return The index of the file, or -1 if it doesn't contain the file
     */
    int getIndexOfFile(AudioFile file);

    /**
     * Gets the first file to be loaded and played
     * @return The first file of the entity
     */
    AudioFile getFirstAudioFile();

    /**
     * Returns the username of the user that is associated with the entity
     * @return The username of the associated user
     */
    String getPublicPerson();

    /**
     * Returns the repeatValue formatted as String
     * @param repeatValue The repeatValue to be converted
     * @return formatted repeatValue, null if repeatValue < 0 or repeatValue > 2
     */
    String getRepeatStateName(int repeatValue);

    /**
     * Checks if the entity is an empty file
     * @return true, if it is an empty file, false otherwise
     */
    boolean isEmptyPlayableFile();

    /**
     * Checks if the entity is a playlist
     * @return true, if it is a playlist or an album, false otherwise
     */
    boolean isPlaylist();

    /**
     * Checks if the entity contains at least one audio file owned by the user
     * @param username The username of the user
     * @return true, if it contains, false otherwise
     */
    boolean hasAudiofileFromUser(String username);

    /**
     * Checks if the entity needs to save its data to a history record
     * @return true, if it needs, false otherwise
     */
    boolean needsHistoryTrack();

    /**
     * Checks if the forward and backward commands can be applied on the
     * entity
     * @return true, if they can be applied, false otherwise
     */
    boolean cantGoForwardOrBackward();

    /**
     * If the current instance is a playlist, returns its instance
     * @return Current object instance, if it is a playlist, null otherwise
     */
    Playlist getCurrentPlaylist();

    /**
     * If the current instance is an album, returns its instance
     * @return Current object instance, if it is an album, null otherwise
     */
    Album getCurrentAlbum();
}
