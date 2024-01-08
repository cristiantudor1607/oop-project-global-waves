package app.notifications;

import app.pages.features.Merch;
import app.player.entities.Album;
import app.player.entities.Podcast;
import app.utilities.constants.NotificationConstants;
import lombok.Getter;

@Getter
public class Notification {
    private final String name;
    private final String description;

    public Notification(final Album album) {
        this.name = NotificationConstants.NEW_ALBUM;
        this.description = NotificationConstants.generateMessage(album.getArtist(), name);
    }

    public Notification(final Podcast podcast) {
        this.name = NotificationConstants.NEW_PODCAST;
        this.description = NotificationConstants.generateMessage(podcast.getOwner(), name);
    }

    public Notification(final String username, final String type) {
        this.name = type;
        this.description = NotificationConstants.generateMessage(username, name);
    }
}
