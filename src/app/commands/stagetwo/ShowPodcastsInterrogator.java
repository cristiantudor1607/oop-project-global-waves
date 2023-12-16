package app.commands.stagetwo;

import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.ShowPodcastsOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Podcast;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowPodcastsInterrogator extends CommandObject {
    private List<Podcast> results;

    @Override
    public void execute() {
        results = manager.requestUserPodcasts(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new ShowPodcastsOutput(this)).generateOutputNode();
    }
}
