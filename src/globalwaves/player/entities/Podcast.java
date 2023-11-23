package globalwaves.player.entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import globalwaves.commands.enums.FollowExit;
import globalwaves.commands.enums.ShuffleExit;
import globalwaves.player.entities.properties.OwnedEntity;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class Podcast implements PlayableEntity, OwnedEntity {
    private String name;
    private String owner;
    private List<AudioFile> episodes;

    public Podcast(PodcastInput input) {
        name = input.getName();
        owner = input.getOwner();

        episodes = new ArrayList<>();
        for (EpisodeInput inputFormatEpisode: input.getEpisodes()) {
            Episode myEpisode = new Episode(inputFormatEpisode);
            episodes.add(myEpisode);
        }

    }

    public AudioFile getNextEpisode(AudioFile currentEpisode) {
        int currentIndex = episodes.indexOf(currentEpisode);

        if (currentIndex > episodes.size())
            return null;

        return episodes.get(currentIndex + 1);
    }

    public AudioFile getPrevEpisode(AudioFile currentEpisode) {
        int currentIndex = episodes.indexOf(currentEpisode);

        if (currentIndex == 0)
            return null;

        return episodes.get(currentIndex - 1);
    }

    @Override
    public String getRepeatStateName(int repeatValue) {
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
        }

        return null;
    }

    @Override
    public AudioFile getNextForPlaying(AudioFile currentFile, int repeatValue) {
        if (repeatValue == 2 || repeatValue == 1)
            return currentFile;

        return  getNextEpisode(currentFile);
    }

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
    public FollowExit.code follow(String username) {
        return FollowExit.code.NOT_A_PLAYLIST;
    }

    @Override
    public ShuffleExit.code shuffle(int seed) {
        return ShuffleExit.code.NOT_A_PLAYLIST;
    }

    @Override
    public ShuffleExit.code unshuffle() {
        return ShuffleExit.code.NOT_A_PLAYLIST;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "\nname='" + name + '\'' +
                "\nowner='" + owner + '\'' +
                "\nepisodes=" + episodes +
                '}';
    }


}
