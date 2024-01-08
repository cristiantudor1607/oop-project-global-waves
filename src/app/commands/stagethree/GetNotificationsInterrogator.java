package app.commands.stagethree;

import app.notifications.Notification;
import app.outputs.stagethree.GetNotificationsOutput;
import app.parser.commands.templates.CommandObject;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class GetNotificationsInterrogator extends CommandObject {
    private List<Notification> notifications;
    /**
     * Executes the getNotifications command.
     */
    @Override
    public void execute() {
        notifications = manager.requestNotifications(username);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     *
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return new GetNotificationsOutput(this).generateOutputNode();
    }
}
