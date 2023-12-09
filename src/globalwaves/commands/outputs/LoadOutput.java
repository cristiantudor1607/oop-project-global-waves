package globalwaves.commands.outputs;

import globalwaves.commands.LoadInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class LoadOutput extends CommandOutputFormatter {
    private String message;

    public LoadOutput(final LoadInterrogator executedLoad) {
        command = "load";
        user = executedLoad.getUsername();
        timestamp = executedLoad.getTimestamp();
        switch (executedLoad.getExitCode()) {
            case NO_SOURCE_SELECTED -> message =
                    "Please select a source before attempting to load.";
            case EMPTY_SOURCE -> message =
                    "You can't load an empty audio collection!";
            case LOADED -> message =
                    "Playback loaded successfully.";
            default -> {

            }
        }
    }

}
