package app.users;

import app.notifications.Notification;
import app.player.entities.Album;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.pages.ArtistPage;
import app.pages.Page;
import app.player.entities.Song;
import app.statistics.StatisticsUtils;
import app.utilities.SortByIntegerValue;
import app.utilities.constants.NotificationConstants;
import app.utilities.constants.StatisticsConstants;
import lombok.Getter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Artist extends User {
    private final List<Album> albums;
    private final ArtistPage selfPage;


    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        setSubscription(SubscriptionType.PROVIDER);
        albums = new ArrayList<>();
        selfPage = new ArtistPage(this);
    }

    public Artist(final String username) {
        super(username);
        setSubscription(SubscriptionType.PROVIDER);
        albums = new ArrayList<>();
        selfPage = new ArtistPage(this);
    }

    /**
     * Returns the user statistics.
     * @return A map which stores the criteria as the key, and a list of tuples of object name
     * and listen count as value, if there isn't specified otherwise. <br>
     * For users, the criteria are:
     * <ul>
     *     <li>topArtist</li>
     *     <li>topGenres</li>
     *     <li>topSongs</li>
     *     <li>topAlbums</li>
     *     <li>topEpisodes</li>
     * </ul>
     * For artists, the criteria are:
     * <ul>
     *     <li>topAlbum</li>
     *     <li>topSongs</li>
     *     <li>topFans: <b>for this criteria, the list will contains tuples with irrelevant
     *     integer values</b></li>
     *     <li>listeners: <b>for this criteria, the list will contain only 1 tuple, with
     *     irrelevant string value</b></li>
     * </ul>
     * For hosts, the criteria are:
     * <ul>
     *     <li>topEpisodes</li>
     *     <li>listeners:  <b>for this criteria, the list will contain only 1 tuple, with
     *     irrelevant string value</b></li>
     * </ul>
     *
     */
    @Override
    public Map<String, List<Map.Entry<String, Integer>>> getStatistics() {
        Map<String, List<Map.Entry<String, Integer>>> statistics  = new HashMap<>();

        List<Map.Entry<String, Integer>> albums =
                StatisticsUtils.combineAndParseAlbumHistory(albumHistory, new SortByIntegerValue<>());
        statistics.put(StatisticsConstants.TOP_ALBUMS, albums);

        List<Map.Entry<String, Integer>> songs = StatisticsUtils.parseHistory(songHistory,
                new SortByIntegerValue<Song>().thenComparing((o1, o2) -> {
                    String o1name = o1.getKey().getName();
                    String o2name = o2.getKey().getName();
                    return o1name.compareTo(o2name);
                }));
        statistics.put(StatisticsConstants.TOP_SONGS, songs);

        int listenersNumber = peopleHistory.size();
        List<Map.Entry<String, Integer>> listenersMapList = new ArrayList<>();
        listenersMapList.add(new AbstractMap.SimpleEntry<>("listeners", listenersNumber));
        statistics.put(StatisticsConstants.LISTENERS, listenersMapList);

        List<Map.Entry<String, Integer>> fans = StatisticsUtils.parseHistory(peopleHistory,
                new SortByIntegerValue<User>()
                        .thenComparing((o1, o2) -> {
                            int id1 = o1.getKey().getIdentificationNumber();
                            int id2 = o2.getKey().getIdentificationNumber();
                            return id1 - id2;
                        }));
        statistics.put(StatisticsConstants.TOP_FANS, fans);

        return statistics;
    }

    /**
     * Checks if {@code this} has monetization data. A user has monetization data only if
     * it is an artist, and it has at least one song that was played.
     * @return {@code true}, if the artist monetization data, {@code false} otherwise
     */
    @Override
    public boolean hasMonetizationData() {
        return !songsIncome.isEmpty() || merchRevenue != 0;
    }

    /**
     * Adds a new amount to the songRevenue of the artist and tracks the money obtained
     * from song.
     * @param song The song
     * @param amount The money obtained from song
     */
    @Override
    public void receiveMoneyFromSong(final Song song, final Double amount) {
        songRevenue += amount;
        if (!songsIncome.containsKey(song)) {
            songsIncome.put(song, 0.0);
        }

        Double income = songsIncome.get(song);
        songsIncome.put(song, income + amount);
    }

    /**
     * Adds a new amount to the merchRevenue of the artist.
     * @param amount The amount paid
     */
    public void receiveMoneyFromMerch(final Double amount) {
        merchRevenue += amount;
    }

    /**
     * Returns the most profitable song of the artist.
     * @return {@code null}, if there aren't songs from artist that were played at least
     * one time, or the song that brought the biggest income otherwise
     */
    public Song getMostProfitableSong() {
        if (songsIncome.isEmpty()) {
            return null;
        }

        double income = 0;
        Song foundSong = null;
        for (Map.Entry<Song, Double> entry : songsIncome.entrySet()) {
            double songIncome = entry.getValue();
            // If the incomes are equal, take the one lexicographically less
            if (songIncome == income && foundSong != null) {
                String currentSongName = entry.getKey().getName();
                String foundSongName = foundSong.getName();
                if (currentSongName.compareTo(foundSongName) < 0) {
                    foundSong = entry.getKey();
                }
            }

            if (songIncome > income) {
                income = songIncome;
                foundSong = entry.getKey();
            }
        }

        return income > 0 ? foundSong : null;
    }

    /**
     * Checks if user has something in history.
     * @return {@code true}, if {@code this} user has history, {@code false} otherwise
     */
    @Override
    public boolean hasHistoryData() {
        return !albumHistory.isEmpty() || !songHistory.isEmpty() || !peopleHistory.isEmpty();
    }

    /**
     * Tracks the number of listens for the specified song.
     * @param song The song to be tracked
     */
    @Override
    public void trackSong(Song song) {
        super.trackSong(song);

        if (!songsIncome.containsKey(song)) {
            songsIncome.put(song, 0.0);
        }
    }

    @Override
    public void trackFan(final User user) {
        if (!peopleHistory.containsKey(user)) {
            peopleHistory.put(user, 0);
        }

        int listens = peopleHistory.get(user);
        peopleHistory.put(user, ++listens);
    }

    /**
     * Returns the page of the artist.
     * @return The page of the artist
     */
    @Override
    public Page getPage() {
        return selfPage;
    }

    /**
     * Returns the number of likes that the user received.
     * @return {@code 0}, if the user isn't an artist, the total number of likes,
     * otherwise (it calculates the sum of all songs number of likes)
     */
    @Override
    public int getNumberOfLikes() {
        int sumOfLikes = 0;
        for (Album album: albums) {
            sumOfLikes += album.getTotalLikesNumber();
        }

        return sumOfLikes;
    }

    /**
     * Adds the album to the artist album list.
     * @param newAlbum The new album to be added
     * @return {@code true}, if the user is an artist, {@code false}, otherwise
     */
    @Override
    public boolean addAlbum(final Album newAlbum) {
        notifier.insertInfo(new Notification(newAlbum));
        return albums.add(newAlbum);
    }

    /**
     * Removes the album from artist list of albums
     * @param oldAlbum The album to be removed. It doesn't check if the album already exists.
     */
    @Override
    public void removeAlbum(final Album oldAlbum) {
        albums.remove(oldAlbum);
    }

    /**
     * Returns the album with the given name.
     * @param albumName The name of the album
     * @return The album, if the user has one with the given name, {@code null}, otherwise. If the
     * user isn't an artist, it still returns {@code null}
     */
    @Override
    public Album getAlbum(final String albumName) {
        for (Album album: albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }

        return null;
    }

    /**
     * Adds a new event.
     * @param newEvent The new event to be added. It doesn't check if the event
     *                 already exists on user's page.
     */
    @Override
    public void addEvent(final Event newEvent) {
        notifier.insertInfo(new Notification(getUsername(), NotificationConstants.NEW_EVENT));
        selfPage.getEvents().add(newEvent);
    }

    /**
     * Remove the event.
     * @param event The event to be removed. It doesn't check if the event
     *              exists on the user's page, so this should be done before
     *              calling the method.
     */
    @Override
    public void removeEvent(final Event event) {
        selfPage.getEvents().remove(event);
    }

    /**
     * Returns the event with the given name.
     * @param eventName The name of the event to be searched.
     * @return The event, if it exists, {@code null}, if the artist doesn't have an event
     * with the given name, or if the user isn't an artist
     */
    @Override
    public Event getEvent(final String eventName) {
        for (Event e : selfPage.getEvents()) {
            if (e.name().equals(eventName)) {
                return e;
            }
        }

        return null;
    }

    /**
     * Adds the new merch.
     * @param newMerch The new merch to be added. It doesn't check if the merch already
     *                 exists on artist page
     */
    @Override
    public void addMerch(final Merch newMerch) {
        notifier.insertInfo(new Notification(getUsername(), NotificationConstants.NEW_MERCH));
        selfPage.getMerchandising().add(newMerch);
    }

    /**
     * Checks if the user has a merch with the given name.
     * @param merchName The name of the merch to be searched
     * @return {@code true}, if the artist has a merch with the given name,
     * {@code false}, if the user isn't an artist, or it doesn't have a merch
     * with the given name
     */
    @Override
    public boolean hasMerch(final String merchName) {
        for (Merch m: selfPage.getMerchandising()) {
            if (m.name().equals(merchName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the user is a normal user.
     * @return {@code true}, if the user is a normal user, {@code false}, otherwise
     */
    @Override
    public boolean isNormalUser() {
        return false;
    }

    /**
     * Checks if the user is an artist.
     * @return {@code true}, if the user is an artist, {@code false}, otherwise
     */
    @Override
    public boolean isArtist() {
        return true;
    }
}
