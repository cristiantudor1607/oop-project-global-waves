package app.outputs.stageone;

import app.exitstats.stageone.SelectExit;
import app.commands.stageone.SelectInterrogator;
import app.utilities.constants.StringConstants;
import app.parser.commands.templates.CommandOutputFormatter;
import app.pages.Page;
import app.properties.PlayableEntity;
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
