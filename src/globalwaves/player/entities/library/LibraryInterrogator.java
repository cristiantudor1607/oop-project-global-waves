package globalwaves.player.entities.library;

import globalwaves.player.entities.AudioFile;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.User;
import globalwaves.player.entities.properties.PlayableEntity;
import globalwaves.player.entities.utilities.SortByInteger;

import java.util.*;

public class LibraryInterrogator {

    private final Library database;
    public LibraryInterrogator() {
        database = Library.getInstance();
    }

    public boolean ownerHasPlaylist(final String owner, final String playlistName) {
        List<Playlist> ownerPlaylists =  database.getPlaylists().get(owner);
        for (Playlist p : ownerPlaylists) {
            if (p.getName().equals(playlistName))
                return true;
        }

        return false;
    }

    public Playlist getOwnerPlaylistById(final String owner, final int id) {
        List<Playlist> ownerPlaylists = database.getPlaylists().get(owner);

        if (id > ownerPlaylists.size())
            return null;

        return ownerPlaylists.get(id - 1);
    }

    public void createPlaylist(final String owner, final String playlistName) {
        Playlist newOwnerPlaylist = new Playlist(owner, playlistName);
        database.addPlaylist(owner, newOwnerPlaylist);
    }

    public List<Playlist> getOwnerPlaylists(final String owner) {
        return database.getPlaylists().get(owner);
    }

    public User getUserByUsername(final String username) {
        for (User matchUser : database.getUsers()) {
            if (matchUser.getUsername().equals(username))
                return matchUser;
        }

        return null;
    }

    // it will return a shallow copy
    public List<Playlist> getUserPublicPlaylists(final String username) {
        List<Playlist> publicPlaylists = new ArrayList<>();

        for (Playlist p : database.getPlaylists().get(username)) {
            if (p.isPublic())
                publicPlaylists.add(p);
        }

        return publicPlaylists;
    }

    public List<Playlist> getAvailablePlaylists(final String owner) {
        List<Playlist> ownedPlaylists = database.getPlaylists().get(owner);

        // Make a shallow copy
        List<Playlist> availablePlaylists = new ArrayList<>(ownedPlaylists);

        for (String username : database.getPlaylists().keySet()) {
            if (username.equals(owner))
                continue;
            List<Playlist> otherPublicPlaylists = getUserPublicPlaylists(username);
            availablePlaylists.addAll(otherPublicPlaylists);
        }

        return availablePlaylists;
    }

    public List<AudioFile> getUserLikedSongs(final String username) {
        User queriedUser = getUserByUsername(username);

        return queriedUser.getLikes();
    }

    public Map<AudioFile, Integer> mapSongs() {
        Map<AudioFile, Integer> mappedSongs = new HashMap<>();

        for (Song s : database.getSongs())
            mappedSongs.put(s, 0);

        return mappedSongs;
    }

    public Map<AudioFile, Integer> mapLikes() {
        Map<AudioFile, Integer> mappedSongs = mapSongs();

        for (User u : database.getUsers()) {
            for (AudioFile likedSong : u.getLikes()) {
               int likes = mappedSongs.get(likedSong);
               likes++;
               mappedSongs.put(likedSong, likes);
            }
        }

        return mappedSongs;
    }

    public List<Map.Entry<AudioFile, Integer>> unrollLikes(Map<AudioFile, Integer> mappedLikes) {
        List<Map.Entry<AudioFile, Integer>> unrolledLikes = new ArrayList<>();

        for (AudioFile key : mappedLikes.keySet()) {
            Integer value = mappedLikes.get(key);
            Map.Entry<AudioFile, Integer> newTuple = new AbstractMap.SimpleEntry<>(key, value);
            unrolledLikes.add(newTuple);
        }

        return unrolledLikes;
    }

    public void sortLikes(List<Map.Entry<AudioFile, Integer>> unrolledLikes) {
        unrolledLikes.sort(new SortByInteger());
    }

    public void truncateLikes(List<Map.Entry<AudioFile, Integer>> likes) {
        likes.subList(5, likes.size()).clear();
    }

    public List<String> getTopFiveSongs() {
        Map<AudioFile, Integer> mappedLikes = mapLikes();
        List<Map.Entry<AudioFile, Integer>> unrolledLikes = unrollLikes(mappedLikes);
        sortLikes(unrolledLikes);
        truncateLikes(unrolledLikes);

        List<String> results = new ArrayList<>();

        for (Map.Entry<AudioFile, Integer> entry : unrolledLikes) {
            results.add(entry.getKey().getName());
        }

        return results;
    }

}
