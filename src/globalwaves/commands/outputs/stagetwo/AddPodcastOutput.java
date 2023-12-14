package globalwaves.commands.outputs.stagetwo;

import globalwaves.commands.enums.exitstats.stagetwo.AddPodcastExit;
import globalwaves.commands.stagetwo.AddPodcastInterrogator;
import globalwaves.parser.templates.CommandOutputFormatter;
import lombok.Getter;

@Getter
public class AddPodcastOutput extends CommandOutputFormatter {
    private final String message;

    public AddPodcastOutput(final AddPodcastInterrogator executedQuery) {
        command = "addPodcast";
        user = executedQuery.getUsername();
        timestamp = executedQuery.getTimestamp();
        message = generateMessage(user, executedQuery.getExitStatus());
    }


    public String generateMessage(final String username, final AddPodcastExit.Status atExit) {
        return switch (atExit) {
            case DOESNT_EXIST -> "The username " + username + " doesn't exist.";
            case NOT_HOST -> username + " is not a host.";
            case SAME_NAME -> username + " has another podcast with the same name.";
            case DUPLICATE -> username + " has the same episode in this podcast.";
            case SUCCESS -> username + " has added new podcast successfully.";
        };
    }
}
