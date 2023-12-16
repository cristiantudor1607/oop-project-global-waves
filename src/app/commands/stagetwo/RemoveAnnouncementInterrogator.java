package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemoveAnnouncementExit;
import app.outputs.stagetwo.RemoveAnnouncementOutput;
import app.parser.commands.templates.CommandObject;
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
