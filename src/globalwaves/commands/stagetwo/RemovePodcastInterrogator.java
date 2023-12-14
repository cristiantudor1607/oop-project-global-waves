package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.RemovePodcastExit;
import globalwaves.commands.outputs.stagetwo.RemovePodcastOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class RemovePodcastInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemovePodcastExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestRemovingPodcast(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new RemovePodcastOutput(this)).generateOutputNode();
    }
}
