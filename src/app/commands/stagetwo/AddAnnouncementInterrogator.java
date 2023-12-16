package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.AddAnnouncementExit;
import app.outputs.stagetwo.AddAnnouncementOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

@Getter
public class AddAnnouncementInterrogator extends CommandObject {
    private String name;
    private String description;
    @JsonIgnore
    private AddAnnouncementExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestAddingAnnouncement(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new AddAnnouncementOutput(this)).generateOutputNode();
    }
}
