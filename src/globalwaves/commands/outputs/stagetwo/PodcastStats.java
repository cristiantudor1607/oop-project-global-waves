package globalwaves.commands.outputs.stagetwo;

import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Podcast;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PodcastStats {
    private final String name;
    private final List<String> episodes;

    public PodcastStats(final Podcast podcast) {
        name = podcast.getName();
        episodes = new ArrayList<>();

        for (AudioFile episode: podcast.getEpisodes())
            episodes.add(episode.getName());
    }
}
