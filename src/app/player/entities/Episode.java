package app.player.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fileio.input.EpisodeInput;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
public class Episode extends AudioFile {
    private String description;

    @JsonCreator
    public Episode(@JsonProperty("name") final String name,
                   @JsonProperty("duration") final int duration,
                   @JsonProperty("description") final String description) {
        this.name = name;
        this.duration = duration;
        this.description = description;
    }

    public Episode(EpisodeInput input) {
        name = input.getName();
        duration = input.getDuration();
        description = input.getDescription();
    }

    /**
     * Checks if the current instance of the class can be liked
     * @return false
     */
    @Override
    public boolean isSong() {
        return false;
    }

    @Override
    public Song getCurrentSong() {
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Episode)) return false;
        Episode episode = (Episode) o;
        return name.equals(episode.name) && duration == episode.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription());
    }
}
