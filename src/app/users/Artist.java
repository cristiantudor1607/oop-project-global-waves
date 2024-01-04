package app.users;

import app.player.entities.Album;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.pages.ArtistPage;
import app.pages.Page;
import app.player.entities.Song;
import app.statistics.StatisticsUtils;
import app.utilities.SortByIntegerValue;
import app.utilities.constants.StatisticsConstants;
import lombok.Getter;

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
        albums = new ArrayList<>();
        selfPage = new ArtistPage(this);
    }

    public Artist(final String username) {
        super(username);
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

        List<Map.Entry<String, Integer>> albums = StatisticsUtils.parseHistory(albumHistory,
                new SortByIntegerValue<>());
        statistics.put(StatisticsConstants.TOP_ALBUMS, albums);

        List<Map.Entry<String, Integer>> songs = StatisticsUtils.parseHistory(songHistory,
                new SortByIntegerValue<Song>().thenComparing((o1, o2) -> {
                    String o1name = o1.getKey().getName();
                    String o2name = o2.getKey().getName();
                    return o1name.compareTo(o2name);
                }));
        statistics.put(StatisticsConstants.TOP_SONGS, songs);

        List<Map.Entry<String, Integer>> fans = StatisticsUtils.parseHistory(peopleHistory,
                new SortByIntegerValue<>());
        statistics.put(StatisticsConstants.TOP_FANS, fans);

        return statistics;
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
