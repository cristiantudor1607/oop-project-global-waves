package globalwaves.commands.outputs;

import globalwaves.commands.StatusInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
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
