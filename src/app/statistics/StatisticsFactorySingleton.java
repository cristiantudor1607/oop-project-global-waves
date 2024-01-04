package app.statistics;

import app.users.User;
import app.utilities.constants.StatisticsConstants;

import java.util.*;

public class StatisticsFactorySingleton {
    private static StatisticsFactorySingleton instance =  null;

    private StatisticsFactorySingleton() { }

    public static StatisticsFactorySingleton getInstance() {
        if (instance == null) {
            instance =  new StatisticsFactorySingleton();
        }

        return instance;
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

        int listeners = topFans.size();

        return ArtistStatistics.builder()
                .topAlbums(topAlbums)
                .topSongs(topSongs)
                .topFans(topFans)
                .listeners(listeners)
                .build();
    }

    private HostStatistics createHostStatistics(final User user) {
        return null;
    }

    public StatisticsTemplate createStatistics(final User user) {
        Map<String, List<Map.Entry<String, Integer>>> stats = user.getStatistics();
        if (user.isNormalUser()) {
            return createUserStatistics(user);
        } else if (user.isArtist()) {
            return createArtistStatistics(user);
        } else {
            return createHostStatistics(user);
        }
    }


}
