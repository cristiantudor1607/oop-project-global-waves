package app.outputs.stageone;

import app.commands.stageone.StatusInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class StatusOutput extends CommandOutputFormatter {
    private PlayerStats stats;

    public StatusOutput(final StatusInterrogator executedQuery) {
        command = "status";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        stats = new PlayerStats(executedQuery.getRequestedPlayer());
    }
}
