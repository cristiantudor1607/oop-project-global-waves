package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.enums.exitstats.stagetwo.RemovePodcastExit;
import globalwaves.commands.stagetwo.RemovePodcastInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class RemovePodcastOutput extends CommandOutputFormatter {
    private final String message;

    public RemovePodcastOutput(final RemovePodcastInterrogator executedQuery) {
        command = "removePodcast";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }

    public String generateMessage(final String username, final RemovePodcastExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_HOST -> username + " is not a host.";
            case DONT_HAVE -> username + " doesn't have a podcast with the given name.";
            case FAIL -> username + " can't delete this podcast.";
            case SUCCESS -> username + " deleted the podcast successfully.";
        };
    }
}
