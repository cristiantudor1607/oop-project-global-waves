package globalwaves.player.entities.library;

import globalwaves.player.entities.*;
import globalwaves.player.entities.utilities.SortByCreationTime;
import globalwaves.player.entities.utilities.SortByFollowers;
import globalwaves.player.entities.utilities.SortByInteger;
import globalwaves.player.entities.utilities.SortByLibraryOrder;

import java.util.*;

public class AdminBot extends Admin  {
    private final HelperTool tool;
    public AdminBot() {
        super();
        tool = HelperTool.getInstance();
    }

    public boolean checkUsername(final String username) {
        for (User normalUser: database.getUsers()) {
            if (normalUser.getUsername().equals(username))
                return true;
        }

        for (User artist: database.getArtists()) {
            if (artist.getUsername().equals(username))
                return true;
        }

        for (User host: database.getHosts()) {
            if (host.getUsername().equals(username))
                return true;
        }

        return false;
    }

    /**
     * Checks if the user has an album with the name specified.
     * @param user User to be inspected.
     * @param albumName The name of the album.
     * @return false, if the user isn't an artist, or it doesn't have an album with the
     * specified name, or true, if it has.
     */
    public boolean checkAlbumNameForUser(final User user, final String albumName) {
        if (!user.isArtist())
            return false;

        return user.hasAlbumWithName(albumName);
    }

    public boolean checkIfOwnerHasPlaylist(final String owner, final String playlistName) {
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

    public void createPlaylist(final String owner, final String playlistName, final int time) {
        Playlist newOwnerPlaylist = new Playlist(owner, playlistName, time);
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

        // TODO: Consider removing this
        for (User matchArtist: database.getArtists()) {
            if (matchArtist.getUsername().equals(username))
                return matchArtist;
        }

        // TODO: And this
        for (User matchHost: database.getHosts()) {
            if (matchHost.getUsername().equals(username))
                return matchHost;
        }

        return null;
    }

    public User getArtistByUsername(final String username) {
        for (User matchArtist: database.getArtists()) {
            if (matchArtist.getUsername().equals(username))
                return matchArtist;
        }

        return null;
    }


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

    public List<String> getTopFiveSongs() {
        Map<AudioFile, Integer> mappedLikes = mapLikes();
        List<Map.Entry<AudioFile, Integer>> unrolledLikes =  tool.unrollLikes(mappedLikes);
        tool.sortLikes(unrolledLikes);
        tool.sortByLibrary(unrolledLikes);
        tool.truncateResults(unrolledLikes);

        List<String> results = new ArrayList<>();

        for (Map.Entry<AudioFile, Integer> entry : unrolledLikes) {
            results.add(entry.getKey().getName());
        }

        return results;
    }

    public List<Playlist> getPublicPlaylists() {
        List<Playlist> publicPlaylists = new ArrayList<>();

        for (String username : database.getPlaylists().keySet()) {
            List<Playlist> userPlaylists = database.getPlaylists().get(username);
            for (Playlist p : userPlaylists) {
                if (p.isPublic())
                    publicPlaylists.add(p);
            }
        }

        return publicPlaylists;
    }

    public List<User> getOnlineUsers() {
        List<User> onlineUsers = new ArrayList<>();

        for (User user: database.getUsers()) {
            if (user.isOnline())
                onlineUsers.add(user);
        }

        return onlineUsers;
    }


    public List<String> getTopFivePlaylists() {
        List<Playlist> playlists = getPublicPlaylists();
        tool.sortPlaylists(playlists);
        tool.sortPlaylistsByTime(playlists);
        tool.truncateResults(playlists);

        List<String> names = new ArrayList<>();
        for (Playlist p : playlists)
            names.add(p.getName());

        return names;
    }

    public List<Album> getArtistAlbums(final String username) {
        User artist = getArtistByUsername(username);

        return artist.getAlbums();
    }
}
