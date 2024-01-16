package app.statistics;

import app.enums.UserType;
import app.users.User;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class StatisticsFactorySingleton {
    private static StatisticsFactorySingleton instance =  null;

    public static final String TOP_ARTISTS = "topArtists";
    public static final String TOP_GENRES = "topGenres";
    public static final String TOP_SONGS = "topSongs";
    public static final String TOP_ALBUMS = "topAlbums";
    public static final String TOP_EPISODES = "topEpisodes";
    public static final String TOP_FANS = "topFans";
    public static final String LISTENERS = "listeners";

    private StatisticsFactorySingleton() { }

    /**
     * Returns the unique factory instance. It creates one when {@code getInstance} is called
     * first time.
     * @return The only instance of the class
     */
    public static StatisticsFactorySingleton getInstance() {
        if (instance == null) {
            instance =  new StatisticsFactorySingleton();
        }

        return instance;
    }

    /**
     * Generates the "No data to show" message for the given user.
     *
     * @param username The username
     * @param type The type of the user. The types accepted are {@code USER}, {@code ARTIST},
     *             {@code HOST}.
     * @return The message, if the type is accepted, {@code null} otherwise
     */
    public static String NoDataMessage(final String username, final UserType.Type type) {
        return switch (type) {
            case USER -> "No data to show for user " + username + ".";
            case HOST -> "No data to show for host " + username + ".";
            case ARTIST -> "No data to show for artist " + username + ".";
            default -> null;
        };
    }

    private NoDataStatistics createNoDataStatistics(final User user) {
        String username = user.getUsername();
        String userTypeMessage;
        if (user.isNormalUser()) {
            userTypeMessage = NoDataMessage(username, UserType.Type.USER);
        } else if (user.isArtist()) {
            userTypeMessage = NoDataMessage(username, UserType.Type.ARTIST);
        } else {
            userTypeMessage = NoDataMessage(username, UserType.Type.HOST);
        }

        return NoDataStatistics.builder()
                .message(userTypeMessage)
                .build();
    }

    private UserStatistics createUserStatistics(final User user) {
        Map<String, List<Map.Entry<String, Integer>>> stats = user.getStatistics();

        Map<String, Integer> topArtists = new LinkedHashMap<>();
        stats.get(TOP_ARTISTS)
                .forEach(pair -> topArtists.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topGenres = new LinkedHashMap<>();
        stats.get(TOP_GENRES)
                .forEach(pair -> topGenres.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topSongs = new LinkedHashMap<>();
        stats.get(TOP_SONGS)
                .forEach(pair -> topSongs.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topAlbums = new LinkedHashMap<>();
        stats.get(TOP_ALBUMS)
                .forEach(pair -> topAlbums.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topEpisodes = new LinkedHashMap<>();
        stats.get(TOP_EPISODES)
                .forEach(pair -> topEpisodes.put(pair.getKey(), pair.getValue()));

        return UserStatistics.builder()
                .topArtists(topArtists)
                .topGenres(topGenres)
                .topSongs(topSongs)
                .topAlbums(topAlbums)
                .topEpisodes(topEpisodes)
                .build();
    }

    private ArtistStatistics createArtistStatistics(final User user) {
        Map<String, List<Map.Entry<String, Integer>>> stats = user.getStatistics();

        Map<String, Integer> topAlbums = new LinkedHashMap<>();
        stats.get(TOP_ALBUMS)
                .forEach(pair -> topAlbums.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topSongs = new LinkedHashMap<>();
        stats.get(TOP_SONGS)
                .forEach(pair -> topSongs.put(pair.getKey(), pair.getValue()));

        List<String> topFans = new LinkedList<>();
        stats.get(TOP_FANS)
                .forEach(pair -> topFans.add(pair.getKey()));

        int listeners = stats.get(LISTENERS).get(0).getValue();

        return ArtistStatistics.builder()
                .topAlbums(topAlbums)
                .topSongs(topSongs)
                .topFans(topFans)
                .listeners(listeners)
                .build();
    }

    private HostStatistics createHostStatistics(final User user) {
        Map<String, List<Map.Entry<String, Integer>>> stats = user.getStatistics();

        Map<String, Integer> topEpisodes = new LinkedHashMap<>();
        stats.get(TOP_EPISODES)
                .forEach(pair -> topEpisodes.put(pair.getKey(), pair.getValue()));

        int listeners = stats.get(LISTENERS).get(0).getValue();

        return HostStatistics.builder()
                .topEpisodes(topEpisodes)
                .listeners(listeners)
                .build();
    }

    /**
     * Creates a user-specific statistics encapsulated in a {@code StatisticsTemplate} class.
     * @param user The user that is analysed
     * @return The statistics encapsulated in a class that extends
     * the abstract {@code StatisticsTemplate}.
     */
    public StatisticsTemplate createStatistics(final User user) {
        if (!user.hasHistoryData()) {
            return createNoDataStatistics(user);
        }

        if (user.isNormalUser()) {
            return createUserStatistics(user);
        } else if (user.isArtist()) {
            return createArtistStatistics(user);
        } else {
            return createHostStatistics(user);
        }
    }


}
