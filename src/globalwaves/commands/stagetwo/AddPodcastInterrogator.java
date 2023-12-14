package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.AddPodcastExit;
import globalwaves.parser.templates.CommandObject;
import globalwaves.player.entities.Episode;
import lombok.Getter;

import java.util.List;

@Getter
public class AddPodcastInterrogator extends CommandObject {
    @JsonProperty("name")
    private String podcastName;
    private List<Episode> episodes;
    @JsonIgnore
    private AddPodcastExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestAddingPodcast(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return null;
    }
}
