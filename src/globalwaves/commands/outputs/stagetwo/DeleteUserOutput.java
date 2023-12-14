package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.stagetwo.DeleteUserInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class DeleteUserOutput extends CommandOutputFormatter {
    private final String message;

    public DeleteUserOutput(final DeleteUserInterrogator executedQuery) {
        command = "deleteUser";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = executedQuery.getExitMessage();
    }

}
