package app.player.entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import app.exitstats.stageone.ShuffleExit;
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

    @Override
    public int getIndexOfFile(AudioFile file) {
        return episodes.indexOf((Episode) file);
    }

    @Override
    public AudioFile getAudioFileAtIndex(int index) {
        if (index >= episodes.size())
            return null;

        return episodes.get(index);
    }

    @Override
    public int getSize() {
        return episodes.size();
    }

    /**
     * Returns the episode that has to be played after the specified one
     * @param currentEpisode The episode currently playing
     * @return The next episode if it exists, null otherwise
     */
    public AudioFile getNextEpisode(final AudioFile currentEpisode) {
        int currentIndex = episodes.indexOf((Episode) currentEpisode);

        if (currentIndex >= episodes.size() - 1)
            return null;

        return episodes.get(currentIndex + 1);
    }

    /**
     * Returns the episode that has to be played before the specified one
     * @param currentEpisode The episode currently playing
     * @return The previous episode, if it exists, null, otherwise
     */
    public AudioFile getPrevEpisode(final AudioFile currentEpisode) {
        int currentIndex = episodes.indexOf((Episode) currentEpisode);

        if (currentIndex == 0)
            return null;

        return episodes.get(currentIndex - 1);
    }

    /**
     * Returns a String that specifies the repeat state, based on the repeatValue
     * @param repeatValue The current repeat state
     * @return a String, if 0 <= repeatValue < 3, null, otherwise
     */
    @Override
    public String getRepeatStateName(final int repeatValue) {
        switch (repeatValue) {
            case 0 -> {
                return "No Repeat";
            }
            case 1 -> {
                return "Repeat Once";
            }
            case 2 -> {
                return "Repeat Infinite";
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public boolean isEmptyPlayableFile() {
        return false;
    }

    @Override
    public AudioFile getFirstAudioFile() {
        return episodes.get(0);
    }

    @Override
    public int getDuration() {
        return episodes.get(0).getDuration();
    }

    @Override
    public boolean needsHistoryTrack() {
        return true;
    }

    @Override
    public boolean cantGoForwardOrBackward() {
        return false;
    }

    @Override
    public Playlist getCurrentPlaylist() {
        return null;
    }

    @Override
    public String getPublicIdentity() {
        return owner;
    }

    @Override
    public boolean hasAudiofileFromUser(String username) {
        return owner.equals(username);
    }

    @Override
    public Album getCurrentAlbum() {
        return null;
    }

    @Override
    public int getCreationTime() {
        return 0;
    }

    @Override
    public boolean isPlaylist() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Podcast)) return false;
        Podcast podcast = (Podcast) o;
        return name.equals(podcast.name) && owner.equals(podcast.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getOwner());
    }
}