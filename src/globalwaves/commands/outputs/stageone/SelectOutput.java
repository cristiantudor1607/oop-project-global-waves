package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.SelectExit;
import globalwaves.commands.stageone.SelectInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;

@Getter
public class SelectOutput extends CommandOutputFormatter {
    private final String message;

    public SelectOutput(SelectInterrogator executedSelect) {
        command = "select";
        user = executedSelect.getUsername();
        timestamp = executedSelect.getTimestamp();
        message = generateMessage(executedSelect.getSelectedAudio(),
                executedSelect.getUsername(), executedSelect.getExitStatus());
    }

    public String generateMessage(final PlayableEntity namedEntity, final String username,
                                  final SelectExit.Status atExit) {
        return switch (atExit) {
            case NO_LIST -> "Please conduct a search before making a selection.";
            case OUT_OF_BOUNDS -> "The selected ID is too high.";
            case SELECTED -> "Successfully selected " + namedEntity.getName() + ".";
            case OFFLINE -> username + StringConstants.OFFLINE_DESCRIPTOR;
        };
    }
}
