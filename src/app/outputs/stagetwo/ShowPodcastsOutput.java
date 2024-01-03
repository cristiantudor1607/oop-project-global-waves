package app.outputs.stagetwo;

import app.commands.stagetwo.ShowPodcastsInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import app.player.entities.Podcast;
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

        for (Podcast podcast: executedQuery.getResults()) {
            result.add(new PodcastStats(podcast));
        }

    }
}
