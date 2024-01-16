package app.monetization;

import app.player.entities.Song;
import app.users.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
     * Calculates the price for each song listened while being a free user
     * and sends the money to the artists.
     * When finished, it resets the free song history.
     */
    public void adBreak() {
        if (subscription != User.SubscriptionType.FREE) {
            return;
        }

        MonetizationUtils.paySongs(adPrice, freeSongs);

        // Reset the song history
        resetFreeSongsHistory();
    }

    /**
     * Calculates the price for each song listened while being a premium user and
     * sends the money to the artists.
     * When finished, it resets the premium song history.
     */
    public void payByPremiumAccount() {
        if (subscription != User.SubscriptionType.PREMIUM) {
            return;
        }

        MonetizationUtils.paySongs(premiumCredit, premiumSongs);

        // Reset the song history
        resetPremiumSongsHistory();
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
     * Enqueues a new song to the premium song history.
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
}
