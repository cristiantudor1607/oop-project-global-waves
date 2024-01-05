package app.properties;

import app.player.entities.Album;
import app.player.entities.AudioFile;
import app.player.entities.Playlist;

public interface PlayableEntity extends NamedObject {

    /**
     * Returns the size of the entity. <b>Size of an entity is defined as number of audio files
     * encapsulated by the entity.</b>
     * @return The size of the entity
     */
    int getSize();

    /**
     * Returns the duration of the entity
     * @return The duration of the entity
     */
    int getDuration();

    /**
     * Returns the identification number of the entity. It is usually an id
     * associated to the entity at creation.
     * @return An identification number bigger than {@code 0}, if the entity has
     * one, {@code 0} otherwise
     */
    int getIdentificationNumber();

    /**
     * Returns the identification number of the user that added the entity, if the
     * entity needs to be sorted by the time when user registered.
     * @return An identification number bigger than {@code 0}, if the entities needs to be
     * sorted by this criterion, {@code 0} otherwise
     */
    int getCreatorIdForSorting();

    /**
     * Returns the AudioFile at the specified index
     * @param index The index of the file
     * @return {@code null}, if the index is out of bounds, the {@code AudioFile} otherwise
     */
    AudioFile getAudioFileAtIndex(int index);

    /**
     * Returns the index of the file in the collection / entity.
     * @param file The file to be inspected.
     * @return The index of the file, or {@code -1 } if it doesn't contain the file
     */
    int getIndexOfFile(AudioFile file);

    /**
     * Returns the first file in collection
     * @return The first file of the entity
     */
    AudioFile getFirstAudioFile();

    /**
     * Returns the username of the user that is associated with the entity
     * @return The username of the associated user
     */
    String getPublicIdentity();

    /**
     * Returns the repeatValue formatted as String value
     * @param repeatValue The repeatValue to be converted
     * @return The corespondent string, or {@code null} if {@code repeatValue < 0} or
     * {@code repeatValue > 2}
     */
    String getRepeatStateName(int repeatValue);

    /**
     * Checks if the entity is an empty file. Only a playlist can be empty,
     * if it doesn't have any song.
     * @return {@code true}, if it is an empty file, {@code false} otherwise
     */
    boolean isEmptyPlayableFile();

    /**
     * Checks if the entity is a playlist
     * @return {@code true}, if it is a playlist or an album, {@code false} otherwise
     */
    boolean isPlaylist();

    /**
     * Checks if the entity is an album
     * @return {@code true}, if it is an album, {@code false} otherwise
     */
    boolean isAlbum();

    /**
     * Checks if the entity contains at least one audio file owned by the user
     * @param username The username of the user
     * @return {@code true}, if it contains, {@code false} otherwise
     */
    boolean hasAudiofileFromUser(String username);

    /**
     * Checks if the entity needs to save its data to a history record
     * @return {@code true}, if it needs, {@code false} otherwise
     */
    boolean needsHistoryTrack();

    /**
     * Checks if the forward and backward commands can be applied on the
     * entity
     * @return {@code true}, if they can be applied, {@code false} otherwise
     */
    boolean cantGoForwardOrBackward();

    /**
     * If {@code this} is a playlist, returns its instance.
     * @return {@code this}, if it is a playlist, {@code null} otherwise
     */
    Playlist getCurrentPlaylist();

    /**
     * If {@code this} is an album, returns its instance.
     * @return {@code this}, if it is an album, {@code null} otherwise
     */
    Album getCurrentAlbum();
}
