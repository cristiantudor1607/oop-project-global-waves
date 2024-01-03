package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.exitstats.stagetwo.RemoveAnnouncementExit;
import app.outputs.stagetwo.RemoveAnnouncementOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RemoveAnnouncementInterrogator extends CommandObject {
    private String name;
    @JsonIgnore
    private RemoveAnnouncementExit.Status exitStatus;

    /**
     * Executes the removeAnnouncement command.
     */
    @Override
    public void execute() {
        exitStatus = manager.requestRemovingAnnouncement(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new RemoveAnnouncementOutput(this)).generateOutputNode();
    }
}
