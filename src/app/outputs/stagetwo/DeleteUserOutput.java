package app.outputs.stagetwo;

import app.commands.stagetwo.DeleteUserInterrogator;
import app.parser.commands.templates.CommandOutputFormatter;
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
