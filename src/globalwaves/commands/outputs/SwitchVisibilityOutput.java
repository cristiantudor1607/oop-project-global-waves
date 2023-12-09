package globalwaves.commands.outputs;

import globalwaves.commands.SwitchVisibilityInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class SwitchVisibilityOutput extends CommandOutputFormatter {
    private String message;

    public SwitchVisibilityOutput(SwitchVisibilityInterrogator executedQuery) {
        command = "switchVisibility";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case TOO_HIGH -> message = "The specified playlist ID is too high.";
            case MADE_PRIVATE -> message = "Visibility status updated successfully to " +
                    "private.";
            case MADE_PUBLIC -> message = "Visibility status updated successfully to " +
                    "public.";
        }
    }
}
