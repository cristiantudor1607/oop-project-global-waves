package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.AddAnnouncementExit;
import globalwaves.commands.outputs.stagetwo.AddAnnouncementOutput;
import globalwaves.parser.templates.CommandObject;
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
