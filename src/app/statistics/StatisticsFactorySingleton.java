package app.statistics;

import app.users.User;
import app.utilities.constants.StatisticsConstants;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class StatisticsFactorySingleton {
    private static StatisticsFactorySingleton instance =  null;

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

    private NoDataStatistics createNoDataStatistics(final User user) {
        String username = user.getUsername();
        String userTypeMessage;
        if (user.isNormalUser()) {
            userTypeMessage = StatisticsConstants.NoUserDataMessage(username);
        } else if (user.isArtist()) {
            userTypeMessage = StatisticsConstants.NoArtistDataMessage(username);
        } else {
            userTypeMessage = StatisticsConstants.NoHostDataMessage(username);
        }

        return NoDataStatistics.builder()
                .message(userTypeMessage)
                .build();
    }

    private UserStatistics createUserStatistics(final User user) {
        Map<String, List<Map.Entry<String, Integer>>> stats = user.getStatistics();

        Map<String, Integer> topArtists = new LinkedHashMap<>();
        stats.get(StatisticsConstants.TOP_ARTISTS)
                .forEach(pair -> topArtists.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topGenres = new LinkedHashMap<>();
        stats.get(StatisticsConstants.TOP_GENRES)
                .forEach(pair -> topGenres.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topSongs = new LinkedHashMap<>();
        stats.get(StatisticsConstants.TOP_SONGS)
                .forEach(pair -> topSongs.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topAlbums = new LinkedHashMap<>();
        stats.get(StatisticsConstants.TOP_ALBUMS)
                .forEach(pair -> topAlbums.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topEpisodes = new LinkedHashMap<>();
        stats.get(StatisticsConstants.TOP_EPISODES)
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
        stats.get(StatisticsConstants.TOP_ALBUMS)
                .forEach(pair -> topAlbums.put(pair.getKey(), pair.getValue()));

        Map<String, Integer> topSongs = new LinkedHashMap<>();
        stats.get(StatisticsConstants.TOP_SONGS)
                .forEach(pair -> topSongs.put(pair.getKey(), pair.getValue()));

        List<String> topFans = new LinkedList<>();
        stats.get(StatisticsConstants.TOP_FANS)
                .forEach(pair -> topFans.add(pair.getKey()));

        int listeners = stats.get(StatisticsConstants.LISTENERS).get(0).getValue();

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
        stats.get(StatisticsConstants.TOP_EPISODES)
                .forEach(pair -> topEpisodes.put(pair.getKey(), pair.getValue()));

        int listeners = stats.get(StatisticsConstants.LISTENERS).get(0).getValue();

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
