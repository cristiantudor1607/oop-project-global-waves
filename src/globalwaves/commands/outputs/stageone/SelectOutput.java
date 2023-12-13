package globalwaves.commands.outputs.stageone;

import globalwaves.commands.enums.exitstats.stageone.SelectExit;
import globalwaves.commands.stageone.SelectInterrogator;
import globalwaves.constants.StringConstants;
import globalwaves.parser.templates.CommandOutputFormatter;
import globalwaves.player.entities.paging.Page;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;

@Getter
public class SelectOutput extends CommandOutputFormatter {
    private final String message;

    public SelectOutput(SelectInterrogator executedSelect) {
        command = "select";
        user = executedSelect.getUsername();
        timestamp = executedSelect.getTimestamp();
        message = generateMessage(executedSelect);
    }

    public String generateMessage(final SelectInterrogator executedSelect) {
        SelectExit.Status atExit = executedSelect.getExitStatus();

        return switch (atExit) {
            case OFFLINE -> {
                String username = executedSelect.getUsername();
                yield username + StringConstants.OFFLINE_DESCRIPTOR;
            }
            case NO_LIST -> "Please conduct a search before making a selection.";
            case OUT_OF_BOUNDS -> "The selected ID is too high.";
            case SELECTED_PLAYABLE_ENTITY -> {
                PlayableEntity playableEntity = executedSelect.getSelectedPlayableEntity();
                String nameOfEntity = playableEntity.getName();

                yield "Successfully selected " + nameOfEntity + ".";
            }
            case SELECTED_PAGE -> {
                Page page = executedSelect.getSelectedPage();
                String publicUserName = page.getUsername();

                yield "Successfully selected " + publicUserName + "'s page.";
            }
        };
    }
}
