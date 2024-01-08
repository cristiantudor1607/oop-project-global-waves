package app.monetization;

import app.player.entities.Song;
import app.users.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonetizationUtils {
    private MonetizationUtils() { }

    /**
     * Returns the income for each song.
     * @param credit The total amount of money that will be split equally to all songs
     * @param totalPlayed The total number of played songs
     * @return The income for one song
     */
    public static Double getIncomePerSong(final int credit, final int totalPlayed) {
        return (double) credit / (double) totalPlayed;
    }


    /**
     * Map the number of playing times for each song in the history.
     * @param songs The song history as a list.
     * @return A map containing the number of playing times for each song in the history
     */
    public static Map<Song, Integer> mapSongsForIncomes(final List<Song> songs) {
        Map<Song, Integer> playingTimes = new HashMap<>();

        songs.forEach(song -> {
            if (!playingTimes.containsKey(song)) {
                playingTimes.put(song, 0);
            }

            int count = playingTimes.get(song);
            playingTimes.put(song, ++count);
        });

        return playingTimes;
    }

    /**
     * Calculates the incomes for each song from the given map.
     * @param playingTimesMap A map containing the number of playing times for each song
     * @param singleIncome The income for one song
     * @return A map containing the incomes for each song
     */
    public static Map<Song, Double>
    calculateIncomesPerSong(final Map<Song, Integer> playingTimesMap,
                            final Double singleIncome) {
        Map<Song, Double> incomes = new HashMap<>();
        playingTimesMap.forEach((song, counter) -> {
            Double songIncome = (double) counter * singleIncome;
            incomes.put(song, songIncome);
        });

        return incomes;
    }

    /**
     * Calculates the price for each song in the history and sends the money to the artists.
     * @param credit The total amount of money to be split equally per song
     * @param songHistory The song history
     */
    public static void paySongs(final int credit, final List<Song> songHistory) {
        Double singleIncome = getIncomePerSong(credit, songHistory.size());
        Map<Song, Integer> songPlayTimesMap = mapSongsForIncomes(songHistory);

        Map<Song, Double> incomes = calculateIncomesPerSong(songPlayTimesMap, singleIncome);

        // Send the money to the artist
        incomes.forEach((song, income) -> {
            User artist = song.getArtistLink();
            artist.receiveMoneyFromSong(song, income);
        });
    }
}
