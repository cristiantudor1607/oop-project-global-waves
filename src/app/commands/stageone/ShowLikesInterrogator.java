package app.commands.stageone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import app.outputs.stageone.ShowLikesOutput;
import app.parser.commands.templates.CommandObject;
import lombok.Getter;

import java.util.List;

@Getter
public class ShowLikesInterrogator extends CommandObject {
    @JsonIgnore
    private List<String> songNames;

    /**
     * Executes the showPreferredSongs command.
     */
    @Override
    public void execute() {
        songNames = manager.requestLikedSongs(username);

        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new ShowLikesOutput(this)).generateOutputNode();
    }
}
