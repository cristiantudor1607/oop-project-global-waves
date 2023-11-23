package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.ShuffleExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class ShuffleOutput extends CommandOutputFormatter {
    private String message;

    public ShuffleOutput(ShuffleInterrogator executedQuery) {
        command = "shuffle";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case ACTIVATED -> message = "Shuffle function activated successfully.";
            case DEACTIVATED -> message = "Shuffle function deactivated successfully.";
            case NOT_A_PLAYLIST -> message = "The loaded source is not a playlist.";
            case NO_SOURCE_LOADED -> message = "Please load a source before using the " +
                    "shuffle function.";
        }
    }
}

@Getter
public class ShuffleInterrogator extends CommandObject {
    private int seed;
    @JsonIgnore
    private ShuffleExit.code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Shuffling ...");

        exitCode = manager.requestShuffling(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new ShuffleOutput(this)).generateOutputNode();
    }
}
