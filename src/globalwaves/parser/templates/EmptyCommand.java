package globalwaves.parser.templates;

import com.fasterxml.jackson.databind.JsonNode;

public class EmptyCommand extends CommandObject{

    @Override
    public void execute() {
        System.out.println("Unimplemented!");
    }

    @Override
    public JsonNode formatOutput() {
        return null;
    }
}
