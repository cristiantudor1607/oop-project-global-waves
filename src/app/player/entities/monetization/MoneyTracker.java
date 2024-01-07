package app.player.entities.monetization;

import app.player.entities.Song;
import app.users.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MoneyTracker {
    private Song ad;
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
     * Adds a new ad to the money tracker.
     * @param nextAd The ad to be enqueued
     * @param price The price of the ad
     */
    public void insertAd(final Song nextAd, final int price) {
        ad = nextAd;
        adPrice = price;
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


}
