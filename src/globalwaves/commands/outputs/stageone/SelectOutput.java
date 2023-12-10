package globalwaves.commands.outputs.stageone;

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
        switch (executedSelect.getExitStatus()) {
            case NO_LIST -> message =
                    "Please conduct a search before making a selection.";
            case OUT_OF_BOUNDS -> message =
                    "The selected ID is too high.";
            case SELECTED -> message =
                    "Successfully selected " +
                            executedSelect.getSelectedAudio().getName() + ".";
        }
    }
}
