package globalwaves.player.entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
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
    public boolean hasNextForPlaying(AudioFile currentFile) {
        return getNextEpisode(currentFile) != null;
    }

    @Override
    public AudioFile getNextForPlaying(AudioFile currentFile) {
        return getNextEpisode(currentFile);
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
