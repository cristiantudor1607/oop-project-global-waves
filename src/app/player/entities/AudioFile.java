package app.player.entities;

import app.properties.NamedObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AudioFile {
    protected String name;
    protected int duration;

    /**
     * Checks if {@code this} is a Song
     * @return {@code true}, if it is a song, {@code false} otherwise
     */
    public abstract boolean isSong();

    /**
     * If {@code this} is a song, return its instance.
     * @return {@code this}, if it is a song, {@code null} otherwise
     */
    public abstract Song getCurrentSong();

    /**
     * If {@code this} is an episode, return its instance.
     * @return {@code this}, if it is an episode, {@code null} otherwise
     */
    public abstract Episode getCurrentEpisode();
}
