package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemovePodcastExit;
import app.outputs.stagetwo.RemovePodcastOutput;
import app.parser.commands.templates.CommandObject;
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
