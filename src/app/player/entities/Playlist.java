package app.player.entities;

import app.properties.OwnedEntity;
import app.properties.PlayableEntity;
import app.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter @Setter
public class Playlist implements PlayableEntity, OwnedEntity {
    private final String name;
    private final String owner;
    private final int creationTime;
    private boolean visible;
    private List<Song> songs;
    private List<User> followers;


    public Playlist(final String owner, final String name, final int creationTime) {
        this.name = name;
        this.owner = owner;
        this.creationTime = creationTime;
        visible = true;
        songs = new ArrayList<>();
        followers = new ArrayList<>();
    }

    /**
     * Calculates the playlist total number of likes. <b>The number of likes of a playlist
     * is the sum of each song likes number.</b>
     * @return The number of likes
     */
    public int getTotalLikesNumber() {
        int sum = 0;
        for (Song s: songs) {
            sum += s.getLikesNumber();
        }

        return sum;
    }

    /**
     * Adds a song to the playlist.
     * @param newSong The song to be added
     */
    public void addSong(final Song newSong) {
        songs.add(newSong);
    }

    /**
     * Removes a song from playlist.
     * @param oldSong The song to be removed
     */
    public void removeSong(final Song oldSong) {
        songs.remove(oldSong);
    }

    /**
     * Checks if playlist contains the song.
     * @param song The song to be contained
     * @return {@code true}, if playlist contains the song, {@code false} otherwise
     */
    public boolean hasSong(final Song song) {
        for (Song s: songs) {
            if (s == song) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the playlist is public.
     * @return {@code true}, if it is public, {@code false} otherwise
     */
    public boolean isPublic() {
        return visible;
    }

    /**
     * Makes the playlist private.
     */
    public void makePrivate() {
        visible = false;
    }

    /**
     * Makes the playlist public.
     */
    public void makePublic() {
        visible = true;
    }

    /**
     * Checks if playlist is followed by the specified user.
     * @param user The user to be verified
     * @return {@code true}, if the user is following {@code this} playlist, {@code false}
     * otherwise
     */
    public boolean isFollowedByUser(final User user) {
        for (User u: followers) {
            if (u.getUsername().equals(user.getUsername())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if playlist is owned by the specified user.
     * @param user The user to be verified
     * @return {@code true}, if the owner of {@code this} playlist is {@code user}, {@code false}
     * otherwise
     */
    public boolean isOwnedByUser(final User user) {
        return owner.equals(user.getUsername());
    }

    /**
     * Adds the user to the followers list.
     * @param user The user to be added
     */
    public void getFollowedBy(final User user) {
        followers.add(user);
    }

    /**
     * Removes the user from followers list.
     * @param user The user to be removed
     */
    public void getUnfollowedBy(final User user) {
        followers.remove(user);
    }

    /**
     * Returns the followers number of this playlist.
     * @return A primitive integer, the number of followers
     */
    public int getFollowersNumber() {
        return followers.size();
    }

    /**
     * Returns the size of the entity. <b>Size of an entity is defined as number of audio files
     * encapsulated by the entity.</b>
     * @return The size of the entity
     */
    @Override
    public int getSize() {
        return songs.size();
    }

    /**
     * Returns the duration of the entity
     * @return The duration of the entity
     */
    @Override
    public int getDuration() {
        if (songs.isEmpty()) {
            return -1;
        }

        AtomicInteger sum = new AtomicInteger();
        songs.forEach(song -> sum.addAndGet(song.getDuration()));

        return sum.get();
    }

    /**
     * Returns the AudioFile at the specified index
     * @param index The index of the file
     * @return {@code null}, if the index is out of bounds, the {@code AudioFile} otherwise
     */
    @Override
    public AudioFile getAudioFileAtIndex(final int index) {
        return index >= songs.size() ? null : songs.get(index);

    }

    /**
     * Returns the index of the file in the collection / entity.
     * @param file The file to be inspected.
     * @return The index of the file, or {@code -1 } if it doesn't contain the file
     */
    @Override
    public int getIndexOfFile(final AudioFile file) {
        return songs.indexOf((Song) file);
    }

    /**
     * Returns the first file in collection
     * @return The first file of the entity
     */
    @Override
    public AudioFile getFirstAudioFile() {
        return songs.isEmpty() ? null : songs.get(0);

    }

    /**
     * Returns the owner username
     * @return The owner username
     */
    @Override
    public String getPublicIdentity() {
        return owner;
    }

    /**
     * Returns the repeatValue formatted as String value
     * @param repeatValue The repeatValue to be converted
     * @return The corespondent string, or {@code null} if {@code repeatValue < 0} or
     * {@code repeatValue > 2}
     */
    @Override
    public String getRepeatStateName(final int repeatValue) {
        return switch (repeatValue) {
            case 0 -> "No Repeat";
            case 1 -> "Repeat All";
            case 2 -> "Repeat Current Song";
            default -> null;
        };
    }

    /**
     * Checks if the entity is an empty file. Only a playlist can be empty,
     * if it doesn't have any song.
     * @return {@code true}, if it is an empty file, {@code false} otherwise
     */
    @Override
    public boolean isEmptyPlayableFile() {
        return songs.isEmpty();
    }

    /**
     * Checks if the entity is a playlist
     * @return {@code true}, if it is a playlist or an album, {@code false} otherwise
     */
    @Override
    public boolean isPlaylist() {
        return true;
    }

    /**
     * Checks if the entity contains at least one audio file owned by the user.
     * @param username The username of the user
     * @return {@code true}, if it contains, {@code false} otherwise
     */
    @Override
    public boolean hasAudiofileFromUser(final String username) {
        for (Song s : songs) {
            if (s.hasAudiofileFromUser(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the entity needs to save its data to a history record
     * @return {@code true}, if it needs, {@code false} otherwise
     */
    @Override
    public boolean needsHistoryTrack() {
        return false;
    }

    /**
     * Checks if the forward and backward commands can be applied on the
     * entity
     * @return {@code true}, if they can be applied, {@code false} otherwise
     */
    @Override
    public boolean cantGoForwardOrBackward() {
        return true;
    }

    /**
     * If {@code this} is a playlist, returns its instance.
     * @return {@code this}, if it is a playlist, {@code null} otherwise
     */
    @Override
    public Playlist getCurrentPlaylist() {
        return this;
    }

    /**
     * If {@code this} is an album, returns its instance.
     * @return {@code this}, if it is an album, {@code null} otherwise
     */
    @Override
    public Album getCurrentAlbum() {
        return null;
    }
}
