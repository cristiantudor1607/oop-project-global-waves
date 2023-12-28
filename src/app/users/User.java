package app.users;

import fileio.input.UserInput;
import app.pages.features.Announcement;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.player.entities.*;
import app.pages.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public User() {}

    public User(UserInput input) {
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

    public Page getPage() {
        return null;
    }

    public boolean isLikingSong(Song song) {
        return likes.contains(song);
    }

    public void like(Song song) {
        likes.add(song);
    }

    public void unlike(Song song) {
        likes.remove(song);
    }

    public boolean isFollowing(Playlist playlist) {
        return following.contains(playlist);
    }

    public void follow(Playlist playlist) {
        following.add(playlist);
    }

    public void unfollow(Playlist playlist) {
        following.remove(playlist);
    }

    public boolean isOnline() {
        return status == ConnectionStatus.ONLINE;
    }

    public boolean isOffline() {
        return status == ConnectionStatus.OFFLINE;
    }

    public void bringOnline() {
        status = ConnectionStatus.ONLINE;
    }

    public void sendOffline() {
        status = ConnectionStatus.OFFLINE;
    }


    public void switchStatus() {
        if (isOnline())
            sendOffline();
        else
            bringOnline();
    }


    public boolean hasAlbumWithName(final String albumName) {
        return false;
    }
    public List<Album> getAlbums() {
        return null;
    }
    public Album getAlbumByName(final String albumName) {
        return null;
    }
    public boolean addAlbum(final Album newAlbum) {
        return false;
    }
    public void removeAlbum(final Album oldAlbum) { }
    public int getNumberOfLikes() {
        return 0;
    }

    public boolean hasEvent(final String eventName) {
        return false;
    }
    public Event getEvent(final String eventName) {
        return null;
    }
    public void addEvent(final Event newEvent) { }
    public boolean removeEvent(final Event event) {
        return false;
    }

    public boolean hasMerch(final String merchName) {
        return false;
    }
    public void addMerch(final Merch newMerch) { }

    public boolean hasPodcastWithName(final String podcastName) {
        return false;
    }
    public Podcast getPodcastByName(final String podcastName) {
        return null;
    }
    public List<Podcast> getPodcasts() {
        return null;
    }
    public boolean addPodcast(final Podcast newPodcast) {
        return false;
    }
    public void removePodcast(final Podcast oldPodcast) { }

    public boolean hasAnnouncement(final String announceName) {
        return false;
    }
    public Announcement getAnnouncement(final String announceName) {
        return null;
    }
    public boolean addAnnouncement(final Announcement newAnnouncement) {
        return false;
    }
    public boolean removeAnnouncement(final Announcement oldAnnouncement) {
        return false;
    }

    public boolean isNormalUser() {
        return true;
    }
    public boolean isArtist() {
        return false;
    }
    public boolean isHost() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public String toString() {
        return "User{" +
                "\nusername='" + username + '\'' +
                "\nage=" + age +
                "\ncity='" + city + '\'' +
                '}';
    }
}
