package globalwaves.player.entities.library;

import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.User;
import globalwaves.player.entities.properties.PlayableEntity;

import java.util.ArrayList;
import java.util.List;

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

    public List<Song> getUserLikedSongs(final String username) {
        User queriedUser = getUserByUsername(username);

        return queriedUser.getLikes();
    }

}
