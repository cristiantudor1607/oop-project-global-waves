package globalwaves.commands.outputs;

import globalwaves.commands.ShuffleInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class ShuffleOutput extends CommandOutputFormatter {
    private String message;

    public ShuffleOutput(final ShuffleInterrogator executedQuery) {
        command = "shuffle";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case ACTIVATED -> message = "Shuffle function activated successfully.";
            case DEACTIVATED -> message = "Shuffle function deactivated successfully.";
            case NOT_A_PLAYLIST -> message = "The loaded source is not a playlist.";
            case NO_SOURCE_LOADED -> message = "Please load a source before using the "
                    + "shuffle function.";
            default -> {

            }
        }
    }
}
