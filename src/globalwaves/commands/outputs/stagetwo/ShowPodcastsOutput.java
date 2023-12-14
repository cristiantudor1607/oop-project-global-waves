package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.stagetwo.ShowPodcastsInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import globalwaves.player.entities.Podcast;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShowPodcastsOutput extends CommandOutputFormatter {
    private final List<PodcastStats> result;

    public ShowPodcastsOutput(final ShowPodcastsInterrogator executedQuery) {
        command = "showPodcasts";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        result = new ArrayList<>();

        for (Podcast podcast: executedQuery.getResults())
            result.add(new PodcastStats(podcast));

    }
}
