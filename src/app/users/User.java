package app.users;

import app.player.entities.Album;
import app.player.entities.Playlist;
import app.player.entities.Podcast;
import app.player.entities.Song;
import fileio.input.UserInput;
import app.pages.features.Announcement;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.pages.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    public enum ConnectionStatus {
        ONLINE,
        OFFLINE,
    }

    private String username;
    private int age;
    private String city;
    private ConnectionStatus status;
    private List<Song> likes;
    @Setter
    private List<Playlist> following;

    public User() { }

    public User(final UserInput input) {
        username = input.getUsername();
        age = input.getAge();
        city = input.getCity();
        status = ConnectionStatus.ONLINE;
        likes = new ArrayList<>();
        following = new ArrayList<>();
    }

    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        status = ConnectionStatus.ONLINE;
        likes = new ArrayList<>();
        following = new ArrayList<>();
    }

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

}
