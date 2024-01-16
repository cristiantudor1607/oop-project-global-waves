package app.commands.stagethree;

import app.outputs.stagethree.WrappedOutput;
import app.parser.commands.templates.CommandObject;
import app.statistics.StatisticsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class WrappedInterrogator extends CommandObject {
    @JsonIgnore
    private StatisticsTemplate statistics;

    /**
     * Executes the wrapped command.
     */
    @Override
    public void execute() {
        statistics = manager.requestWrap(username);
        manager.setLastActionTime(timestamp);
    }

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     *
     * @return A JsonNode containing the output data
     */
    @Override
    public JsonNode formatOutput() {
        return (new WrappedOutput(this)).generateOutputNode();
    }
}
