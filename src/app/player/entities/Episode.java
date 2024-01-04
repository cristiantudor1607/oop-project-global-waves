package app.player.entities;

import app.properties.NamedObject;
import app.users.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fileio.input.EpisodeInput;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class Episode extends AudioFile implements NamedObject {
    @JsonIgnore
    private User hostLink;
    @JsonIgnore
    private Podcast podcastLink;
    private String description;

    @JsonCreator
    public Episode(@JsonProperty("name") final String name,
                   @JsonProperty("duration") final int duration,
                   @JsonProperty("description") final String description) {
        this.name = name;
        this.duration = duration;
        this.description = description;
    }

    public Episode(final EpisodeInput input) {
        name = input.getName();
        duration = input.getDuration();
        description = input.getDescription();
    }

    /**
     * Checks if {@code this} is a Song
     * @return {@code true}, if it is a song, {@code false} otherwise
     */
    @Override
    public boolean isSong() {
        return false;
    }

    /**
     * If {@code this} is a song, return its instance.
     * @return {@code this}, if it is a song, {@code null} otherwise
     */
    @Override
    public Song getCurrentSong() {
        return null;
    }

    /**
     * If {@code this} is an episode, return its instance.
     * @return {@code this}, if it is an episode, {@code null} otherwise
     */
    @Override
    public Episode getCurrentEpisode() {
        return this;
    }

    /**
     * Compares this episode with the specified object. The result is true if and only if
     * the argument is not null and is an Episode object that represents the same episode
     * as this object.
     *
     * @param o The object to compare this episode against
     * @return {@code true}, if the given object represents the same episode as this
     * episode, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Episode)) {
            return false;
        }
        Episode episode = (Episode) o;
        return name.equals(episode.name) && duration == episode.duration
                && description.equals(episode.description);
    }

    /**
     * Returns a hashcode value for this episode. If two objects are equal according to the
     * equals method, then calling the hashCode method on each of the two objects must produce the
     * same integer result.
     *
     * @return A hashcode value for this episode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDuration(), getDescription());
    }
}
