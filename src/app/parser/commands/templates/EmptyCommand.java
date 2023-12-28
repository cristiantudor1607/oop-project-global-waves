package app.parser.commands.templates;

import com.fasterxml.jackson.databind.JsonNode;

public class EmptyCommand extends CommandObject {

    /**
     * Empty command is a class designed for temporary usage, so it prints a
     * message and does nothing.
     */
    @Override
    public void execute() {
        System.out.println("Unimplemented!");
    }

    /**
     * Empty command is a class designed for temporary usage, so it returns
     * null because it has no output.
     * @return null
     */
    @Override
    public JsonNode formatOutput() {
        return null;
    }
}
