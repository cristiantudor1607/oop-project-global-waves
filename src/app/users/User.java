package app.users;

import app.management.IDContainer;
import app.notifications.Inbox;
import app.notifications.Notification;
import app.notifications.Notifier;
import app.pages.PageHistory;
import app.player.entities.Album;
import app.player.entities.AudioFile;
import app.player.entities.Episode;
import app.player.entities.Playlist;
import app.player.entities.Podcast;
import app.player.entities.Song;
import app.monetization.MoneyTracker;
import app.properties.NamePossessor;
import app.properties.UniqueIdPossessor;
import app.statistics.Genre;
import app.statistics.StatisticsUtils;
import app.utilities.SortAlphabeticallyByKey;
import app.utilities.SortByIntegerValue;
import app.utilities.SortByKeyName;
import app.utilities.SortByNumberOfLikes;
import app.utilities.constants.StatisticsConstants;
import fileio.input.UserInput;
import app.pages.features.Announcement;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.pages.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class User implements NamePossessor, UniqueIdPossessor {
    public enum ConnectionStatus {
        ONLINE,
        OFFLINE,
    }

    // TODO: Add a special subscription type for admins
    public enum SubscriptionType {
        FREE,
        PREMIUM,
        PROVIDER
    }

    // Essential info
    private int id;
    private String username;
    private int age;
    private String city;
    private ConnectionStatus status;
    @Setter
    private SubscriptionType subscription;
    private List<Song> likes;
    @Setter
    private List<Playlist> following;
    private List<Merch> boughtItems;

    // Histories for monetization
    private MoneyTracker moneyTracker;

    // Monetization info for artists
    protected Double merchRevenue;
    protected Double songRevenue;
    protected Map<Song, Double> songsIncome;

    // Statistics info
    private Map<User, Integer> artistHistory;
    protected Map<Song, Integer> songHistory;
    private Map<Genre, Integer> genreHistory;
    protected Map<Album, Integer> albumHistory;
    protected Map<Episode, Integer> episodeHistory;
    protected Map<User, Integer> peopleHistory;

    // Inbox and Notifier for notification management
    private Inbox inbox;
    protected Notifier notifier;

    // Extra info, in case age or city weren't set, and they are used
    private boolean noAge;
    private boolean noCity;

    public User() { }

    public User(final UserInput input) {
        IDContainer idContainer = IDContainer.getInstance();
        id = idContainer.useUserId();

        username = input.getUsername();
        age = input.getAge();
        noAge = false;
        city = input.getCity();
        noCity = false;
        status = ConnectionStatus.ONLINE;
        subscription = SubscriptionType.FREE;
        likes = new ArrayList<>();
        following = new ArrayList<>();
        boughtItems = new LinkedList<>();
        moneyTracker = new MoneyTracker();

        merchRevenue = 0.0;
        songRevenue = 0.0;
        songsIncome = new HashMap<>();

        artistHistory = new HashMap<>();
        songHistory = new HashMap<>();
        genreHistory = new HashMap<>();
        albumHistory = new HashMap<>();
        episodeHistory = new HashMap<>();
        peopleHistory = new HashMap<>();

        inbox = new Inbox();
        notifier = new Notifier();
    }

    public User(final String username, final int age, final String city) {
        IDContainer idContainer = IDContainer.getInstance();
        id = idContainer.useUserId();

        this.username = username;
        this.age = age;
        noAge = false;
        this.city = city;
        noCity = false;
        status = ConnectionStatus.ONLINE;
        subscription = SubscriptionType.FREE;
        likes = new ArrayList<>();
        following = new ArrayList<>();
        boughtItems = new LinkedList<>();
        moneyTracker = new MoneyTracker();

        merchRevenue = 0.0;
        songRevenue = 0.0;
        songsIncome = new HashMap<>();

        artistHistory = new HashMap<>();
        songHistory = new HashMap<>();
        genreHistory = new HashMap<>();
        albumHistory = new HashMap<>();
        episodeHistory = new HashMap<>();
        peopleHistory = new HashMap<>();

        inbox = new Inbox();
        notifier = new Notifier();
    }

    public User(final String username) {
        this(username, 0, null);
        noAge = true;
        noCity = true;
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
     *     <li>topFans: <b>for this criterion, the list will contains tuples with irrelevant
     *     Integer values</b></li>
     *     <li>listeners: <b>for this criterion, the list will contains only 1 tuple,
     *     with irrelevant String value. It will be set by default to {@code "listeners"}</b></li>
     * </ul>
     * For hosts, the criteria are:
     * <ul>
     *     <li>topEpisodes</li>
     *     <<li>listeners: <b>for this criterion, the list will contains only 1 tuple,
     *     with irrelevant String value. It will be set by default to {@code "listeners"}</b></li>
     * </ul>
     *
     */
    public Map<String, List<Map.Entry<String, Integer>>> getStatistics() {
        Map<String, List<Map.Entry<String, Integer>>> statistics  = new HashMap<>();

        List<Map.Entry<String, Integer>> artists = StatisticsUtils.parseHistory(artistHistory,
                new SortByIntegerValue<User>().thenComparing(new SortByKeyName<>()));
        statistics.put(StatisticsConstants.TOP_ARTISTS, artists);

        List<Map.Entry<String, Integer>> genres = StatisticsUtils.parseHistory(genreHistory,
                new SortByIntegerValue<Genre>().thenComparing(new SortByKeyName<>()));
        statistics.put(StatisticsConstants.TOP_GENRES, genres);

        List<Map.Entry<String, Integer>> songs = StatisticsUtils.parseHistory(songHistory,
                new SortByIntegerValue<Song>().thenComparing(new SortByKeyName<>()));
        statistics.put(StatisticsConstants.TOP_SONGS, songs);

        List<Map.Entry<String, Integer>> albums = StatisticsUtils
                .combineAndParseAlbumHistory(albumHistory,
                new SortByIntegerValue<String>().thenComparing(Map.Entry::getKey));
        statistics.put(StatisticsConstants.TOP_ALBUMS, albums);

        List<Map.Entry<String, Integer>> episodes = StatisticsUtils.parseHistory(episodeHistory,
                new SortByIntegerValue<>());
        statistics.put(StatisticsConstants.TOP_EPISODES, episodes);

        return statistics;
    }

    /**
     * Returns a list with the artist's top 5 fans.
     * The fans ar sorted by the number of
     * listens, and then after their arrival time on the platform.
     *
     * @return A list containing five users or fewer, with the fans sorted if the user is
     * an artist, or {@code null} otherwise
     */
    public List<User> getTop5Fans() {
        return null;
    }

    /**
     * Returns a list with all the songs user listened to, sorted by their number of
     * likes.
     *
     * @return A list containing all the songs user listened to, sorted by their number of
     * likes
     */
    public List<Song> getTopSongs() {
        return likes.stream()
                .sorted(new SortByNumberOfLikes())
                .toList();
    }

    /**
     * Returns the notifications of the user and mark them as read.
     * @return A list of notifications
     */
    public List<Notification> readAndGetNotifications() {
        return inbox.readNotifications();
    }

    /**
     * Subscribes to the given user.
     * @param user The user to get subscribed to
     */
    public void subscribe(final User user) {
        user.getNotifier().attach(inbox);
    }

    /**
     * Unsubscribes from the given user.
     * @param user The user to be unsubscribed from
     */
    public void unsubscribe(final User user) {
        // TODO: Clear inbox
        user.getNotifier().detach(inbox);
    }

    /**
     * Checks if {@code this} user is a subscriber of the given user.
     * @param user The user to be checked
     * @return {@code true}, if the {@code this} user is a subscriber of the given
     * user, {@code false} otherwise
     */
    public boolean isSubscriber(final User user) {
        return user.getNotifier().hasSubscriber(this);
    }

    /**
     * Checks if {@code this} has monetization data. A user has monetization data only if
     * it is an artist, and it has at least one song that was played.
     * @return {@code true}, if the artist monetization data, {@code false} otherwise
     */
    public boolean hasMonetizationData() {
        return false;
    }

    /**
     * Returns the most profitable song of the artist.
     * @return {@code null}, if there aren't songs from artist that were played at least
     * one time, or if {@code this} isn't an artist, the song that brought the biggest income,
     * otherwise
     */
    public Song getMostProfitableSong() {
        return null;
    }

    /**
     * Adds a new amount to the songRevenue of the artist and tracks the money obtained
     * from song. For normal users and hosts, it does nothing.
     * @param song The song
     * @param amount The money obtained from song
     */
    public void receiveMoneyFromSong(final Song song, final Double amount) { }

    /**
     * Checks if user has something in history.
     * @return {@code true}, if {@code this} user has history, {@code false} otherwise
     */
    public boolean hasHistoryData() {
        return !artistHistory.isEmpty() || !songHistory.isEmpty() || !albumHistory.isEmpty()
                || !genreHistory.isEmpty() || !episodeHistory.isEmpty();
    }

    /**
     * Checks if the artist have at least one listened song.
     * @return {@code true}, if the artist was listened, {@code false} otherwise. If the user
     * isn't an artist, it returns {@code false}
     */
    public boolean wasListened() {
        return false;
    }

    /**
     * Adds a new amount to the merchRevenue of the artist. For normal users and hosts,
     * it has no effect.
     * @param amount The amount paid
     */
    public void receiveMoneyFromMerch(final Double amount) { }

    /**
     * Adds a new merch to the {@code boughtItems} list.
     * @param merch The merch to be added
     */
    public void buyMerch(final Merch merch) {
        boughtItems.add(merch);
    }

    /**
     * Tracks the data for a new playing file.
     * @param file The file to be tracked
     */
    public void trackFile(final AudioFile file) {
        if (file.isSong()) {
            // Track activity for user
            Song song = file.getCurrentSong();
            trackSong(song);
            trackGenre(new Genre(song.getGenre().toLowerCase()));
            trackArtist(song.getArtistLink());
            trackAlbum(song.getAlbumLink());

            // Track activity for monetization
            moneyTracker.enqueueSong(song);

            // Track activity for artist
            User artist = song.getArtistLink();
            artist.trackAlbum(song.getAlbumLink());
            artist.trackSong(song);
            artist.trackFan(this);
        } else {
            // Track activity for user
            Episode episode = file.getCurrentEpisode();
            trackEpisode(episode);

            // Track activity for host
            User host = episode.getHostLink();
            if (host != null) {
                host.trackEpisode(episode);
                host.trackFan(this);
            }
        }
    }

    /**
     * Tracks the number of listens for the specified song.
     * @param song The song to be tracked
     */
    public void trackSong(final Song song) {
        if (!songHistory.containsKey(song)) {
            songHistory.put(song, 0);
        }

        int listens = songHistory.get(song);
        listens++;

        songHistory.put(song, listens);
    }

    /**
     * Tracks the number of listens for the specified episode.
     * @param episode The episode to be tracked
     */
    public void trackEpisode(final Episode episode) {
        if (!episodeHistory.containsKey(episode)) {
            episodeHistory.put(episode, 0);
        }

        int listens = episodeHistory.get(episode);
        listens++;

        episodeHistory.put(episode, listens);
    }

    /**
     * Tracks the number of listens for specified genre
     * @param genre The genre to be tracked
     */
    public void trackGenre(final Genre genre) {
        if (!genreHistory.containsKey(genre)) {
            genreHistory.put(genre, 0);
        }

        int listens = genreHistory.get(genre);
        genreHistory.put(genre, ++listens);
    }

    /**
     * Tracks the number of listens for specified artist.
     * @param artist The artist to be tracked
     */
    public void trackArtist(final User artist) {
        if (!artistHistory.containsKey(artist)) {
            artistHistory.put(artist, 0);
        }

        int listens = artistHistory.get(artist);
        artistHistory.put(artist, ++listens);
    }

    /**
     * Tracks the number of listens for specified album.
     * @param album The album to be tracked
     */
    public void trackAlbum(final Album album) {
        if (!albumHistory.containsKey(album)) {
            albumHistory.put(album, 0);
        }

        int listens = albumHistory.get(album);
        albumHistory.put(album, ++listens);
    }

    /**
     * Tracks the specified user number of listeners from artist pov. If {@code this}
     * isn't an artist, it does nothing.
     * @param user The user to be tracked.
     */
    public void trackFan(final User user) { }

    /**
     * Returns the page of the user.
     * @return The page, for artists and hosts, {@code null}, for normal users
     */
    public Page getPage() {
        return null;
    }

    /**
     * Returns the number of likes that the user received.
     * @return {@code 0}, if the user isn't an artist, the total number of likes,
     * otherwise (it calculates the sum of all songs number of likes)
     */
    public int getNumberOfLikes() {
        return 0;
    }

    /**
     * Checks if the user is liking the song
     * @param song The song to be searched in the user's likes
     * @return {@code true}, if the user is liking the song, {@code false}, otherwise
     */
    public boolean isLikingSong(final Song song) {
        return likes.contains(song);
    }

    /**
     * Adds the song to user's likes list.
     * @param song The song to be added
     */
    public void like(final Song song) {
        likes.add(song);
    }

    /**
     * Removes the song from user's likes list.
     * @param song The song to be removed
     */
    public void unlike(final Song song) {
        likes.remove(song);
    }

    /**
     * Adds the playlist to the user's following list.
     * @param playlist The playlist to be added
     */
    public void follow(final Playlist playlist) {
        following.add(playlist);
    }

    /**
     * Removes the playlist from user's following list.
     * @param playlist The playlist to be removed
     */
    public void unfollow(final Playlist playlist) {
        following.remove(playlist);
    }

    /**
     * Checks if the user is online.
     * @return {@code true}, if the user is online, {@code false}, otherwise
     */
    public boolean isOnline() {
        return status == ConnectionStatus.ONLINE;
    }

    /**
     * Checks if the user is offline.
     * @return {@code true}, if the user is offline, {@code false}, otherwise
     */
    public boolean isOffline() {
        return status == ConnectionStatus.OFFLINE;
    }

    /**
     * Put the user in online state.
     */
    public void bringOnline() {
        status = ConnectionStatus.ONLINE;
    }

    /**
     * Put the user in offline state.
     */
    public void sendOffline() {
        status = ConnectionStatus.OFFLINE;
    }

    /**
     * Changes the user connection status.
     */
    public void switchStatus() {
        if (isOnline()) {
            sendOffline();
        } else {
            bringOnline();
        }
    }

    /**
     * Checks if the user is a premium user.
     * @return {@code true}, if the user have a premium account, {@code false} otherwise
     */
    public boolean isPremium() {
        return subscription == SubscriptionType.PREMIUM;
    }

    // TODO: Overwrite method for artist and host
    /**
     * Makes the user a premium user.
     */
    public void makePremium() {
        subscription = SubscriptionType.PREMIUM;
        moneyTracker.makePremium();
    }

    // TODO: Overwrite method for artist and host
    /**
     * Makes the user a free user.
     */
    public void cancelPremium() {
        subscription = SubscriptionType.FREE;
        moneyTracker.payByPremiumAccount();
        moneyTracker.makeFree();
    }

    /**
     * Returns the user's albums.
     * @return The list of albums, if the user is an artist, {@code null}, otherwise. If the
     * artist doesn't have albums, it returns an empty list
     */
    public List<Album> getAlbums() {
        return null;
    }

    /**
     * Adds the album to the artist album list.
     * @param newAlbum The new album to be added
     * @return {@code true}, if the user is an artist, {@code false}, otherwise
     */
    public boolean addAlbum(final Album newAlbum) {
        return false;
    }

    /**
     * Removes the album. For artists, it is removed from their list of albums, and
     * for admins, it is removed from library.
     * @param oldAlbum The album to be removed. It doesn't check if the album already exists.
     */
    public void removeAlbum(final Album oldAlbum) { }


    /**
     * Returns the album with the given name.
     * @param albumName The name of the album
     * @return The album, if the user has one with the given name, {@code null}, otherwise. If the
     * user isn't an artist, it still returns {@code null}
     */
    public Album getAlbum(final String albumName) {
        return null;
    }

    /**
     * Adds a new event.
     * @param newEvent The new event to be added. It doesn't check if the event
     *                 already exists on user's page.
     */
    public void addEvent(final Event newEvent) { }

    /**
     * Remove the event.
     * @param event The event to be removed. It doesn't check if the event
     *              exists on the user's page, so this should be done before
     *              calling the method.
     */
    public void removeEvent(final Event event) { }

    /**
     * Returns the event with the given name.
     * @param eventName The name of the event to be searched.
     * @return The event, if it exists, {@code null}, if the artist doesn't have an event
     * with the given name, or if the user isn't an artist
     */
    public Event getEvent(final String eventName) {
        return null;
    }

    /**
     * Adds the new merch.
     * @param newMerch The new merch to be added. It doesn't check if the merch already
     *                 exists on artist page
     */
    public void addMerch(final Merch newMerch) { }

    /**
     * Checks if the user has a merch with the given name.
     * @param merchName The name of the merch to be searched
     * @return {@code true}, if the artist has a merch with the given name,
     * {@code false}, if the user isn't an artist, or it doesn't have a merch
     * with the given name
     */
    public boolean hasMerch(final String merchName) {
        return false;
    }

    /**
     * Returns the user's podcasts.
     * @return A list of podcasts, if the user is a host, {@code null}, otherwise. If the host
     * doesn't have podcasts, it returns an empty list
     */
    public List<Podcast> getPodcasts() {
        return null;
    }

    /**
     * Adds a podcast to the host podcast list.
     * @param newPodcast The new podcast to be added
     * @return {@code true}, if the user is a host, {@code false} otherwise
     */
    public boolean addPodcast(final Podcast newPodcast) {
        return false;
    }

    /**
     * Removes the podcast. For hosts, it is removed from their list, and for admins,
     * it is removed from library.
     * @param oldPodcast The podcast to be removed. It has to exist in the host list
     */
    public void removePodcast(final Podcast oldPodcast) { }

    /**
     * Returns the podcast with the given name.
     * @param podcastName The name of the podcast
     * @return The podcast, if it exists, {@code null}, if the host doesn't have a podcast
     * with the given name, or the user isn't a host
     */
    public Podcast getPodcast(final String podcastName) {
        return null;
    }

    /**
     * Adds a new announcement.
     * @param newAnnouncement The new announcement to be added
     * @return {@code true}, if the user is a host, {@code false} otherwise
     */
    public boolean addAnnouncement(final Announcement newAnnouncement) {
        return false;
    }

    /**
     * Removes the announcement.
     * @param oldAnnouncement The announcement to be removed
     */
    public void removeAnnouncement(final Announcement oldAnnouncement) { }

    /**
     * Returns the announcement with the given name
     * @param announceName The name of the announcement
     * @return The announcement, if it exists, {@code null}, if the host doesn't have an
     * announcement with the given name, or the user isn't a host
     */
    public Announcement getAnnouncement(final String announceName) {
        return null;
    }

    /**
     * Checks if the user is a normal user.
     * @return {@code true}, if the user is a normal user, {@code false}, otherwise
     */
    public boolean isNormalUser() {
        return true;
    }

    /**
     * Checks if the user is an artist.
     * @return {@code true}, if the user is an artist, {@code false}, otherwise
     */
    public boolean isArtist() {
        return false;
    }

    /**
     * Checks if the user is a host.
     * @return {@code true}, if the user is a host, {@code false}, otherwise
     */
    public boolean isHost() {
        return false;
    }

    /**
     * Returns the username
     * @return The username
     */
    @Override
    public String getName() {
        return username;
    }

    /**
     * Returns the id associated with the user at first login on platform.
     * @return An identification number bigger than {@code 0}
     */
    @Override
    public int getIdentificationNumber() {
        return id;
    }
}
