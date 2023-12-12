package globalwaves.player.entities;

import fileio.input.UserInput;
import globalwaves.player.entities.paging.Page;
import lombok.Getter;

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

    public boolean isNormalUser() {
        return true;
    }

    public boolean isArtist() {
        return false;
    }

    public boolean isHost() {
        return false;
    }

    public boolean hasAlbumWithName(final String albumName) {
        return false;
    }

    public boolean addAlbum(final Album newAlbum) {
        return false;
    }

    public List<Album> getAlbums() {
        return null;
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
