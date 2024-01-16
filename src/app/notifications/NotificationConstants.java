package app.notifications;

public class NotificationConstants {
    public static final String NEW_ALBUM = "New Album";
    public static final String NEW_PODCAST = "New Podcast";
    public static final String NEW_MERCH = "New Merchandise";
    public static final String NEW_EVENT = "New Event";
    public static final String NEW_ANNOUNCEMENT = "New Announcement";

    private NotificationConstants() { }

    /**
     * Generates a specific notification message. <br>
     * For example, if a new album is added for Pink FLoyd, if generates the message
     * <b>"New Album from Pink Floyd."</b>, if called with {@code NEW_ALBUM} as {@code constant}.
     * @param username The username of the artist, host, or the owner of the playlist
     * @param constant The type of message generated. It can be one of the following:
     *                 <ul>
     *                 <li>{@code NEW_FOLLOWER}, for generating messages when a user starts
     *                 following a playlist</li>
     *                 <li>{@code NEW_ALBUM}, for generating messages when an artist adds a
     *                 new album</li>
     *                 <li>{@code NEW_PODCAST}, for generating messages when a host adds a new
     *                 podcast</li>
     *                 <li>{@code NEW_EVENT}, for generating messages when an artist adds a new
     *                 event</li>
     *                 <li>{@code NEW_ANNOUNCEMENT}, for generating messages when a host adds
     *                 a new announcement</li>
     *                 <li>{@code NEW_MERCH}, for generating messages when an artist adds a new
     *                 merchandise item</li>
     *                 </ul>
     * @return The notification message
     */
    public static String generateMessage(final String username, final String constant) {
        return constant + " from " + username + ".";
    }
}
