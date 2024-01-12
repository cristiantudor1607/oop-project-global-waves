package app.player.entities;

import app.pages.Page;
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

    /**
     * Returns the public page of the entity's creator.
     *
     * @return The artist page, if {@code this} is a song or the host page, if
     * {@code this} is an episode
     */
    public abstract Page getCreatorPage();

    /**
     * Checks if {@code this is an ad}. An <b>ad</b> is defined as a song
     * with the <b>advertisement</b> genre.
     * @return {@code true}, if {@code this} is an ad, {@code false} otherwise
     */
    public boolean isAd() {
        return false;
    }

}
