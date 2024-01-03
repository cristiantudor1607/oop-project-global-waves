package app.commands.stagetwo;

import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.ShowPodcastsOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Podcast;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ShowPodcastsInterrogator extends CommandObject {
    private List<Podcast> results;

    /**
     * Executes the showPodcasts command.
     */
    @Override
    public void execute() {
        results = manager.requestUserPodcasts(username);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new ShowPodcastsOutput(this)).generateOutputNode();
    }
}
