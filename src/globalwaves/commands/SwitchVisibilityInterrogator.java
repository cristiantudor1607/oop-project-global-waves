package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.SwitchVisibilityExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;
import lombok.Setter;

@Getter
class SwitchVisibilityOutput extends CommandOutputFormatter {
    private String message;

    public SwitchVisibilityOutput(SwitchVisibilityInterrogator executedQuery) {
        command = "switchVisibility";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        switch (executedQuery.getExitCode()) {
            case TOO_HIGH -> message = "The specified playlist ID is too high.";
            case MADE_PRIVATE -> message = "Visibility status updated successfully to " +
                    "false.";
            case MADE_PUBLIC -> message = "Visibility status updated successfully to " +
                    "true.";
        }
    }
}

@Getter @Setter
public class SwitchVisibilityInterrogator extends CommandObject {
    private int playlistId;
    @JsonIgnore
    private SwitchVisibilityExit.code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Switching Visibility ...");

        exitCode = manager.requestSwitch(this);
        manager.setLastActionTime(timestamp);
        return (new SwitchVisibilityOutput(this)).generateOutputNode();
    }
}
