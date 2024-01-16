package app.player.entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import app.properties.OwnedEntity;
import app.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter @Setter
public class Podcast implements PlayableEntity, OwnedEntity {
    private String name;
    private String owner;
    private List<Episode> episodes;

    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
    }

    public Podcast(final PodcastInput input) {
        name = input.getName();
        owner = input.getOwner();

        episodes = new ArrayList<>();
        for (EpisodeInput inputFormatEpisode: input.getEpisodes()) {
            Episode myEpisode = new Episode(inputFormatEpisode);
            episodes.add(myEpisode);
        }
    }

    /**
     * Returns the size of the entity. <b>Size of an entity is defined as number of audio files
     * encapsulated by the entity.</b>
     * @return The size of the entity
     */
    @Override
    public int getSize() {
        return episodes.size();
    }

    /**
     * Returns the duration of the entity
     * @return The duration of the entity
     */
    @Override
    public int getDuration() {
        int sum = 0;

        for (Episode e: episodes) {
            sum += e.getDuration();
        }

        return sum;
    }

    /**
     * Returns the identification number of the entity. It is usually an id
     * associated to the entity at creation.
     * @return An identification number bigger than {@code 0}, if the entity has
     * one, {@code 0} otherwise
     */
    @Override
    public int getIdentificationNumber() {
        return 0;
    }

    /**
     * Returns the identification number of the user that added the entity, if the
     * entity needs to be sorted by the time when user registered.
     * @return An identification number bigger than {@code 0}, if the entities needs to be
     * sorted by this criterion, {@code 0} otherwise. <b>For podcasts, it returns 0.</b>
     */
    @Override
    public int getCreatorIdForSorting() {
        return 0;
    }

    /**
     * Returns the AudioFile at the specified index
     * @param index The index of the file
     * @return {@code null}, if the index is out of bounds, the {@code AudioFile} otherwise
     */
    @Override
    public AudioFile getAudioFileAtIndex(final int index) {
        if (index >= episodes.size()) {
            return null;
        }

        return episodes.get(index);
    }

    /**
     * Returns the index of the file in the collection / entity.
     * @param file The file to be inspected.
     * @return The index of the file, or {@code -1 } if it doesn't contain the file
     */
    @Override
    public int getIndexOfFile(final AudioFile file) {
        return episodes.indexOf((Episode) file);
    }

    /**
     * Returns the first file in collection
     * @return The first file of the entity
     */
    @Override
    public AudioFile getFirstAudioFile() {
        return episodes.get(0);
    }

    /**
     * Returns the owner of the podcast.
     * @return The owner of the podcast
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
            case 1 -> "Repeat Once";
            case 2 -> "Repeat Infinite";
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
        return false;
    }

    /**
     * Checks if the entity is a playlist.
     * @return {@code true}, if it is a playlist or an album, {@code false} otherwise
     */
    @Override
    public boolean isPlaylist() {
        return false;
    }

    /**
     * Checks if the entity is an album
     * @return {@code true}, if it is an album, {@code false} otherwise
     */
    @Override
    public boolean isAlbum() {
        return false;
    }

    /**
     * Checks if the entity contains at least one audio file owned by the user
     * @param username The username of the user
     * @return {@code true}, if it contains, {@code false} otherwise
     */
    @Override
    public boolean hasAudiofileFromUser(final String username) {
        return owner.equals(username);
    }

    /**
     * Checks if the entity needs to save its data to a history record
     * @return {@code true}, if it needs, {@code false} otherwise
     */
    @Override
    public boolean needsHistoryTrack() {
        return true;
    }

    /**
     * Checks if the forward and backward commands can be applied on the
     * entity
     * @return {@code true}, if they can be applied, {@code false} otherwise
     */
    @Override
    public boolean cantGoForwardOrBackward() {
        return false;
    }

    /**
     * If {@code this} is a playlist, returns its instance.
     * @return {@code this}, if it is a playlist, {@code null} otherwise
     */
    @Override
    public Playlist getCurrentPlaylist() {
        return null;
    }

    /**
     * If {@code this} is an album, returns its instance.
     * @return {@code this}, if it is an album, {@code null} otherwise
     */
    @Override
    public Album getCurrentAlbum() {
        return null;
    }

    /**
     * Compares this podcast with the specified object. The result is true if and only if
     * the argument is not null and is a Podcast object that represents the same podcast
     * as this object.
     *
     * @param o The object to compare this podcast against
     * @return {@code true}, if the given object represents the same podcast as this
     * podcast, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Podcast)) {
            return false;
        }
        Podcast podcast = (Podcast) o;
        return name.equals(podcast.name) && owner.equals(podcast.owner);
    }

    /**
     * Returns a hashcode value for this podcast. If two objects are equal according to the
     * equals method, then calling the hashCode method on each of the two objects must produce the
     * same integer result.
     *
     * @return A hashcode value for this podcast
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getOwner());
    }
}
