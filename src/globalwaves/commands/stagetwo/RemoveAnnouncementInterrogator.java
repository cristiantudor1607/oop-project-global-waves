package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.exitstats.stagetwo.RemoveAnnouncementExit;
import globalwaves.commands.outputs.stagetwo.RemoveAnnouncementOutput;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class RemoveAnnouncementInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemoveAnnouncementExit.Status exitStatus;

    @Override
    public void execute() {
        exitStatus = manager.requestRemovingAnnouncement(this);
        manager.setLastActionTime(timestamp);
    }

    @Override
    public JsonNode formatOutput() {
        return (new RemoveAnnouncementOutput(this)).generateOutputNode();
    }
}
