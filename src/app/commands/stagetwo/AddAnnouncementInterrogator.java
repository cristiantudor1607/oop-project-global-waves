package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.AddAnnouncementExit;
import app.outputs.stagetwo.AddAnnouncementOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddAnnouncementInterrogator extends CommandObject {
    private String name;
    private String description;
    @JsonIgnore
    private AddAnnouncementExit.Status exitStatus;

    /**
     * Executes the addAnnouncement command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestAddingAnnouncement(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new AddAnnouncementOutput(this)).generateOutputNode();
    }
}
