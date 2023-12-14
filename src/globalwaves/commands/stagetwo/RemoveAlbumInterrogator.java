package globalwaves.commands.stagetwo;

import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.parser.templates.CommandObject;
import lombok.Getter;

@Getter
public class RemoveAlbumInterrogator extends CommandObject {
    private String name;

    @Override
    public void execute() {

    }

    @Override
    public JsonNode formatOutput() {
        return null;
    }
}
