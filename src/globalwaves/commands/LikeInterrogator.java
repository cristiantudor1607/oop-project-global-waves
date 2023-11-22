package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.LikeExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class LikeOutput extends CommandOutputFormatter {
    private String message;

    public LikeOutput(LikeInterrogator executedQuery) {
        command = "like";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case LIKED -> message = "Like registered successfully.";
            case UNLIKED -> message = "Unlike registered successfully.";
            case NO_SOURCE -> message = "Please load a source before liking or unliking.";
            case NOT_A_SONG -> message = "Loaded source is not a song.";
        }
    }
}

@Getter
public class LikeInterrogator extends CommandObject {
    @JsonIgnore
    private LikeExit.code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Liking or unliking ...");

        exitCode = manager.requestLikeAction(this);

        System.out.println(exitCode);
        manager.setLastActionTime(timestamp);
        return (new LikeOutput(this)).generateOutputNode();
    }
}
