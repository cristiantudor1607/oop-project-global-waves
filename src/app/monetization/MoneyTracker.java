package app.monetization;

import app.player.entities.Song;
import app.users.User;
import lombok.Getter;

import java.util.*;

@Getter
public class MoneyTracker {
    private int adPrice;

    private User.SubscriptionType subscription;
    private final int premiumCredit = 1_000_000;

    private final List<Song> freeSongs;
    private final List<Song> premiumSongs;

    public MoneyTracker() {
        subscription = User.SubscriptionType.FREE;
        freeSongs = new ArrayList<>();
        premiumSongs = new ArrayList<>();
    }

    /**
     * Inserts a new price when an adBreak is added to the queue.
     * @param price The price of the ad
     */
    public void insertPrice(final int price) {
        adPrice = price;
    }

    /**
     * Calculates the price for each song and sends the money to the artists.
     * When finished, it rests the free songs history.
     */
    public void adBreak() {
        Double singleIncome = MonetizationUtils.getIncomePerSong(adPrice, freeSongs.size());
        Map<Song, Integer> playingTimes = MonetizationUtils.mapSongsForIncomes(freeSongs);
        Map<Song, Double> incomes = MonetizationUtils
                .calculateIncomesPerSong(playingTimes, singleIncome);

        // Send the money to the artist
        incomes.forEach((song, income) -> {
            User artist = song.getArtistLink();
            artist.receiveMoneyFromSong(song, income);
        });

        resetFreeSongsHistory();
    }

    /**
     * Enqueues song to one of the queues, depending on current value of
     * {@code subscription}.
     * @param song The song to be enqueued
     */
    public void enqueueSong(final Song song) {
        if (subscription == User.SubscriptionType.FREE) {
            enqueueFreeSong(song);
        } else {
            enqueuePremiumSong(song);
        }
    }

    /**
     * Updates the current subscription type to {@code FREE}.
     */
    public void makeFree() {
        subscription = User.SubscriptionType.FREE;
    }

    /**
     * Updates the current subscription type to {@code PREMIUM}.
     */
    public void makePremium() {
        subscription = User.SubscriptionType.PREMIUM;
    }

    /**
     * Enqueues a new song to the free song history.
     * @param song The song to be enqueued
     */
    public void enqueueFreeSong(final Song song) {
        freeSongs.add(song);
    }

    /**
     * Resets the short-term free song history.
     */
    public void resetFreeSongsHistory() {
        freeSongs.clear();
    }

    /**
     * Enqueues a new song to the free song history.
     * @param song The song to be enqueued
     */
    public void enqueuePremiumSong(final Song song) {
        premiumSongs.add(song);
    }

    /**
     * Resets the short-term premium song history.
     */
    public void resetPremiumSongsHistory() {
        premiumSongs.clear();
    }

    /**
     * Maps the songs to calculate the incomes for each song. They're mapped by the number of
     * songs listened from an artist. If there are multiple songs from an artist, the entry will
     * have the same value, but different keys. <br>
     * For example, if the user listened to:
     * <ul>
     *     <li>Hey Love from Stevie Wonder - <b>2 times</b></li>
     *     <li>Let's Go Crazy [Explicit] from Prince - <b>3 times</b></li>
     *     <li>Come One from The Rolling Stones - <b>2 times</b></li>
     *     <li>It's All Over Now from The Rolling Stones - <b>2 times</b></li>
     * </ul>
     * Then the resulting map will look like:
     * <ul>
     *     <li>Hey Love from Stevie Wonder - <b>2</b></li>
     *     <li>Let's Go Crazy [Explicit] from Prince - <b>3</b></li>
     *     <li>Come One from The Rolling Stones - <b>4</b></li>
     *     <li>It's All Over Now from The Rolling Stones - <b>4</b></li>
     * </ul>
     * Because the user played <b>4</b> songs added by The Rolling Stones, each song will be mapped
     * with <b>4</b> as value.
     * @param songs The song history as a list.
     * @return A Map containing the songs mapped as in the example above
     */
    public Map<Song, Integer> mapSongsForIncomes(final List<Song> songs) {
        return null;
    }
}
