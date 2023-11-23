package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.FollowExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class FollowOutput extends CommandOutputFormatter {
    private String message;

    public FollowOutput(FollowInterrogator executedQuery) {
        command = "follow";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case FOLLOWED -> message = "Playlist followed successfully.";
            case UNFOLLOWED -> message = "Playlist unfollowed successfully.";
            case NOT_A_PLAYLIST -> message = "The selected source is not a playlist.";
            case NO_SOURCE -> message = "Please select a source before following or unfollowing.";
            case OWNER -> message = "You cannot follow or unfollow your own playlist.";
        }
    }
}

@Getter
public class FollowInterrogator extends CommandObject {
    @JsonIgnore
    FollowExit.code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Following or unfollowing ...");

        exitCode = manager.requestFollowAction(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new FollowOutput(this)).generateOutputNode();
    }
}
