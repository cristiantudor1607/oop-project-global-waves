package app.commands.stagetwo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stagetwo.ChangePageOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangePageInterrogator extends CommandObject {
    private String nextPage;
    @JsonIgnore
    private String output;

    /**
     * Executes the changePage command.
     */
    @Override
    public void execute() {
        output = manager.requestChangePage(this);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new ChangePageOutput(this)).generateOutputNode();
    }
}
