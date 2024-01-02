package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemovePodcastExit;
import app.outputs.stagetwo.RemovePodcastOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemovePodcastInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemovePodcastExit.Status exitStatus;

    /**
     * Executes the removePodcast command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestRemovingPodcast(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new RemovePodcastOutput(this)).generateOutputNode();
    }
}
