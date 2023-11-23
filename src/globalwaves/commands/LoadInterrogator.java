package globalwaves.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.enums.LoadExit;
import globalwaves.parser.commands.CommandObject;
import globalwaves.parser.commands.CommandOutputFormatter;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;

@Getter
class LoadOutput extends CommandOutputFormatter {
    String message;

    public LoadOutput(LoadInterrogator executedLoad) {
        command = "load";
        user = executedLoad.getUsername();
        timestamp = executedLoad.getTimestamp();
        switch (executedLoad.getExitCode()) {
            case NO_SOURCE_SELECTED ->  message =
                    "Please select a source before attempting to load.";
            case EMPTY_SOURCE -> message =
                    "You can't load an empty audio collection!";
            case LOADED -> message =
                    "Playback loaded successfully.";
        }
    }

}

@Getter
public class LoadInterrogator extends CommandObject {
    @JsonIgnore private LoadExit.code exitCode;
    @Override
    public JsonNode execute(ActionManager manager) {
        System.out.println("Loading file ...");

        exitCode = manager.requestLoading(this);
        manager.setLastActionTime(timestamp);
        manager.setLastAction(this);

        return (new LoadOutput(this)).generateOutputNode();
    }
}
