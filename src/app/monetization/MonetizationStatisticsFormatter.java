package app.monetization;

import app.management.Library;
import app.users.User;
import app.utilities.SortAlphabeticallyByKey;
import app.utilities.SortByRevenue;

import java.util.*;

public class MonetizationStatisticsFormatter {
    private final Library database;

    public MonetizationStatisticsFormatter() {
        database = Library.getInstance();
    }

    /**
     * Pays the artists for the songs listened by premium users by
     * the end of the program.
     */
    public void makeFinalPayment() {
        database.getUsers().forEach(user -> {
            user.getMoneyTracker().payByPremiumAccount();
        });
    }

    /**
     * Creates a monetization summary entry for the given user.
     * @param artist The user whose summary is created
     * @return {@code null}, if the user isn't an artist, or it doesn't have at least one
     * song played by someone or a merch bought by someone, the summary entry otherwise
     */
    public Map.Entry<String, MonetizationStat> createMonetizationEntry(final User artist) {
        if (!artist.hasMonetizationData()) {
            return null;
        }

        MonetizationStat artistStat = new MonetizationStat(artist);
        String artistName = artist.getUsername();

        return new AbstractMap.SimpleEntry<>(artistName, artistStat);
    }

    /**
     * Retrieves the monetization statistics for all artist. An overview for
     * the artist income is generated only if at least one of their songs was
     * played by someone, or one of their merch was bought by someone.
     * It sorts the stats by total revenue, and for equal revenues, by artist username.
     * @return A map with the monetization stats for all artists.
     */
    private List<Map.Entry<String, MonetizationStat>> getAndSortMonetization() {
        List<User> artists = database.getArtists();

        List<Map.Entry<String, MonetizationStat>> stats = new ArrayList<>();
        for (User artist : artists) {
            Map.Entry<String, MonetizationStat> entry = createMonetizationEntry(artist);
            if (entry != null) {
                stats.add(entry);
            }
        }

        stats.sort(new SortByRevenue<String>()
                .reversed()
                .thenComparing(new SortAlphabeticallyByKey<>()));

        for (int i = 0; i < stats.size(); i++) {
            stats.get(i).getValue().setRanking(i + 1);
        }

        return stats;
    }

    /**
     * Retrieves the monetization statistics for all artist. An overview for
     * the artist income is generated only if at least one of their songs was
     * played by someone, or one of their merch was bought by someone.
     * @return A map with the monetization stats for all artists.
     */
    public Map<String, MonetizationStat> getMonetizationStatistics() {
        makeFinalPayment();
        List<Map.Entry<String, MonetizationStat>> statsList = getAndSortMonetization();

        Map<String, MonetizationStat> result = new HashMap<>();
        statsList.forEach(entry -> {
            result.put(entry.getKey(), entry.getValue());
        });

        return result;
    }
}
