package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.AddPodcastExit;
import app.outputs.stagetwo.AddPodcastOutput;
import app.parser.commands.templates.CommandObject;
import app.player.entities.Episode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AddPodcastInterrogator extends CommandObject {
    @JsonProperty("name")
    private String podcastName;
    private List<Episode> episodes;
    @JsonIgnore
    private AddPodcastExit.Status exitStatus;

    /**
     * Executes the addPodcast command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestAddingPodcast(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new AddPodcastOutput(this)).generateOutputNode();
    }
}
