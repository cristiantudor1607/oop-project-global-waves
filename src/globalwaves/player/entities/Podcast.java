package globalwaves.player.entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import globalwaves.commands.enums.exitcodes.FollowExit;
import globalwaves.commands.enums.exitcodes.ShuffleExit;
import globalwaves.player.entities.properties.OwnedEntity;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Podcast implements PlayableEntity, OwnedEntity {
    private String name;
    private String owner;
    private List<AudioFile> episodes;

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
     * Returns the episode that has to be played after the specified one
     * @param currentEpisode The episode currently playing
     * @return The next episode if it exists, null otherwise
     */
    public AudioFile getNextEpisode(final AudioFile currentEpisode) {
        int currentIndex = episodes.indexOf(currentEpisode);

        if (currentIndex > episodes.size())
            return null;

        return episodes.get(currentIndex + 1);
    }

    /**
     * Returns the episode that has to be played before the specified one
     * @param currentEpisode The episode currently playing
     * @return The previous episode, if it exists, null, otherwise
     */
    public AudioFile getPrevEpisode(final AudioFile currentEpisode) {
        int currentIndex = episodes.indexOf(currentEpisode);

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

    /**
     * Returns the next episode for playing, based on the repeatValue. The repeatValue 1 acts as
     * 2 because it will be modified by the player to 0, after playing the second time
     * @param currentFile The playing file
     * @param repeatValue The current repeat state
     * @return The next episode to be played in the player
     */
    @Override
    public AudioFile getNextForPlaying(final AudioFile currentFile, final int repeatValue) {
        if (repeatValue == 2 || repeatValue == 1)
            return currentFile;

        return  getNextEpisode(currentFile);
    }

    /**
     * Returns the previous episode for playing, based on the repeatValue. The repeatValue 1 acts
     * as 2 because it will be modified by the player to 0, after playing the second time
     * @param currentFile The playing file
     * @param repeatValue The current repeat state
     * @return The previous episode to be played in the player
     */
    @Override
    public AudioFile getPrevForPlaying(AudioFile currentFile, int repeatValue) {
        if (repeatValue == 2 || repeatValue == 1)
            return currentFile;

        return getPrevEpisode(currentFile);
    }

    @Override
    public boolean isEmptyPlayableFile() {
        return false;
    }

    @Override
    public AudioFile getPlayableFile() {
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
    public FollowExit.Code follow(String username) {
        return FollowExit.Code.NOT_A_PLAYLIST;
    }

    @Override
    public ShuffleExit.Code shuffle(int seed) {
        return ShuffleExit.Code.NOT_A_PLAYLIST;
    }

    @Override
    public ShuffleExit.Code unshuffle() {
        return ShuffleExit.Code.NOT_A_PLAYLIST;
    }

    @Override
    public boolean cantGoForwardOrBackward() {
        return false;
    }

}
