package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.SelectExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
class SelectOutput extends CommandOutputFormatter {
    private String message;

    public SelectOutput(SelectInterrogator executedSelect) {
        command = "select";
        user = executedSelect.getUsername();
        timestamp = executedSelect.getTimestamp();
        switch (executedSelect.getExitCode()) {
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

@Getter @Setter
public class SelectInterrogator extends CommandObject {
    private int itemNumber;
    @JsonIgnore private PlayableEntity selectedAudio = null;
    @JsonIgnore private SelectExit.code exitCode;

    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Selecting...");

        exitCode = manager.requestItemSelection(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new SelectOutput(this)).generateOutputNode();
    }

    @Override
    public boolean isSelectAction() {
        return exitCode == SelectExit.code.SELECTED;
    }
}
