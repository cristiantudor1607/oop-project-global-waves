package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.SelectExit;
import globalwaves.commands.stageone.SelectInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class SelectOutput extends CommandOutputFormatter {
    private String message;

    public SelectOutput(SelectInterrogator executedSelect) {
        command = "select";
        user = executedSelect.getUsername();
        timestamp = executedSelect.getTimestamp();
        message = generateMessage(executedSelect.getSelectedAudio().getName(),
                executedSelect.getUsername(), executedSelect.getExitStatus());
    }

    public String generateMessage(final String filename, final String username,
                                  final SelectExit.Status exitStatus) {
        return switch (exitStatus) {
            case NO_LIST -> "Please conduct a search before making a selection.";
            case OUT_OF_BOUNDS -> "The selected ID is too high.";
            case SELECTED -> "Successfully selected " + filename + ".";
            case OFFLINE -> username + " is offline.";
        };
    }
}
