package globalwaves.commands;

import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.commands.CommandObject;
import globalwaves.player.entities.library.ActionManager;

public class RepeatInterrogator extends CommandObject {

    @Override
    public JsonNode execute(ActionManager manager) {
        return super.execute(manager);
    }
}
