package app.utilities.constants;

public class StatisticsConstants {
    // TODO: Inlocuieste si in celelalte parti cu asta
    public static final String TOP_ARTISTS = "topArtists";
    public static final String TOP_GENRES = "topGenres";
    public static final String TOP_SONGS = "topSongs";
    public static final String TOP_ALBUMS = "topAlbums";
    public static final String TOP_EPISODES = "topEpisodes";
    public static final String TOP_FANS = "topFans";
    public static final String LISTENERS = "listeners";

    /**
     * Generate the "No data to show" message for the given username.
     * @param username The username
     * @return The "No data to show" message
     */
    public static String NoDataMessage(final String username) {
        return "No data to show for user " + username + ".";
    }
}
