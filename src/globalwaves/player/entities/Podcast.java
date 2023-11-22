package globalwaves.player.entities;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import globalwaves.player.entities.properties.PlayableEntity;
import globalwaves.player.entities.properties.OwnedEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Podcast implements PlayableEntity, OwnedEntity {
    private String name;
    private String owner;
    private ArrayList<Episode> episodes;
    private int duration;

    public Podcast(PodcastInput input) {
        name = input.getName();
        owner = input.getOwner();

        episodes = new ArrayList<>();
        for (EpisodeInput inputFormatEpisode: input.getEpisodes()) {
            Episode myEpisode = new Episode(inputFormatEpisode);
            episodes.add(myEpisode);
        }

        duration = calculatePodcastDuration();
    }

    public int calculatePodcastDuration() {
        int sum = 0;
        for (Episode e  : episodes)
            sum += e.getDuration();

        return sum;
    }

    @Override
    public boolean isEmptyPlayableFile() {
        return false;
    }

    @Override
    public boolean isPlaylist() {
        return false;
    }

    @Override
    public boolean isSong() {
        return false;
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
