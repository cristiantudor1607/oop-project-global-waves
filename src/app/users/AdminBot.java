package app.users;

import app.utilities.HelperTool;
import app.player.entities.*;
import app.utilities.SortAlphabetical;
import app.utilities.SortByArtistLikes;
import app.utilities.SortByTotalLikes;

import java.util.*;

public class AdminBot extends Admin {
    private final HelperTool tool;
    public AdminBot() {
        super();
        tool = HelperTool.getInstance();
    }

    /**
     * Returns the user that have the given username
     * @param username The username of the user to be found
     * @return The user, if it exists, null otherwise
     */
    public User getUserByUsername(final String username) {
        for (User matchUser : database.getUsers()) {
            if (matchUser.getUsername().equals(username))
                return matchUser;
        }

        for (User matchArtist: database.getArtists()) {
            if (matchArtist.getUsername().equals(username))
                return matchArtist;
        }

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

    public User getHostByUsername(final String username) {
        for (User matchHost: database.getHosts()) {
            if (matchHost.getUsername().equals(username))
                return matchHost;
        }

        return null;
    }

    public List<User> getOnlineUsers() {
        List<User> onlineUsers = new ArrayList<>();

        for (User user: database.getUsers()) {
            if (user.isOnline())
                onlineUsers.add(user);
        }

        return onlineUsers;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        users.addAll(database.getUsers());
        users.addAll(database.getArtists());
        users.addAll(database.getHosts());

        return users;
    }

    /**
     * Checks if the username exists
     * @param username The username to be verified in database
     * @return true, if the username is taken, false otherwise
     */
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
     * @param user User to be inspected. It has to be previously verified if it is an artist.
     * @param albumName The name of the album.
     * @return false, if the user isn't an artist, or it doesn't have an album with the
     * specified name, or true, if it has.
     */
    public boolean checkAlbumNameForUser(final User user, final String albumName) {
        if (!user.isArtist())
            return false;

        return user.hasAlbumWithName(albumName);
    }

    /**
     * Checks if the user has a podcast with the name specified.
     * @param user User to be inspected. It has to be previously verified if it is a host.
     * @param podcastName The name of the podcast.
     * @return false, if the user isn't a host, or it doesn't have a podcast with the given
     * name, or true, if it has
     */
    public boolean checkPodcastNameForUser(final User user, final String podcastName) {
        if (!user.isHost())
            return false;

        return user.hasPodcastWithName(podcastName);
    }

    public boolean checkIfOwnerHasPlaylist(final String owner, final String playlistName) {
        List<Playlist> ownerPlaylists =  database.getPlaylists().get(owner);
        if (ownerPlaylists == null)
            return false;

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

    /**
     * Gets all the playlist owned by owner.
     * @param owner The owner of the playlists.
     * @return List of playlists, if it exists, null otherwise
     */
    public List<Playlist> getOwnerPlaylists(final String owner) {
        return database.getPlaylists().get(owner);
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
        List<Playlist> availablePlaylists = new ArrayList<>();

        List<Playlist> ownedPlaylists = database.getPlaylists().get(owner);
        if (ownedPlaylists != null)
            availablePlaylists.addAll(ownedPlaylists);

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

    /**
     * Maps all songs from the library into a Map of Songs as keys, and 0 as value,
     * that represents the number of likes before being counted.
     * @return The map of all songs from library
     */
    public Map<Song, Integer> mapSongs() {
        Map<Song, Integer> mappedSongs = new HashMap<>();

        for (Song s : database.getSongs())
            mappedSongs.put(s, 0);

        for (List<Song> songs: database.getAddedSongs().values()) {
           for (Song s: songs) {
                mappedSongs.put(s, 0);
           }
        }

        return mappedSongs;
    }

    public Map<Song, Integer> mapLikes() {
        Map<Song, Integer> mappedSongs = mapSongs();

        for (User u : database.getUsers()) {
            for (Song likedSong : u.getLikes()) {
               int likes = mappedSongs.get(likedSong);
               likes++;
               mappedSongs.put(likedSong, likes);
            }
        }

        return mappedSongs;
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

    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();

        database.getArtists().forEach((artist -> albums.addAll(artist.getAlbums())));

        return albums;
    }

    public List<String> getTopFiveSongs() {
        Map<Song, Integer> mappedLikes = mapLikes();
        List<Map.Entry<Song, Integer>> unrolledLikes =  tool.unrollLikes(mappedLikes);
        tool.sortLikes(unrolledLikes);
        tool.sortByLibrary(unrolledLikes);
        tool.truncateResults(unrolledLikes);

        List<String> results = new ArrayList<>();

        for (Map.Entry<Song, Integer> entry : unrolledLikes) {
            results.add(entry.getKey().getName());
        }

        return results;
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

    public List<String> getTopFiveAlbums() {
        List<Album> albums = getAllAlbums();
        albums.sort(new SortByTotalLikes().thenComparing(new SortAlphabetical()));
        tool.truncateResults(albums);

        List<String> names = new ArrayList<>();
        albums.forEach(album -> names.add(album.getName()));

        return names;
    }

    public List<String> getTopFiveArtists() {
        List<User> artists = new ArrayList<>(database.getArtists());

        artists.sort(new SortByArtistLikes());
        tool.truncateResults(artists);

        List<String> names = new ArrayList<>();
        artists.forEach(artist -> names.add(artist.getUsername()));

        return names;
    }

    public List<Album> getArtistAlbums(final String username) {
        User artist = getArtistByUsername(username);

        return artist.getAlbums();
    }

    public List<Podcast> getHostPodcasts(final String username) {
        User host = getHostByUsername(username);

        return host.getPodcasts();
    }

    public void addSongsToLibrary(final String artist, final List<Song> songs) {
        List<Song> artistSongs = database.getAddedSongs()
                .computeIfAbsent(artist, k -> new ArrayList<>());

        artistSongs.addAll(songs);
    }

    public void addPodcastToLibrary(final String host, final Podcast podcast) {
        List<Podcast> hostPodcasts = database.getAddedPodcasts()
                .computeIfAbsent(host, k -> new ArrayList<>());

        hostPodcasts.add(podcast);
    }

    public boolean playlistsHaveSongFromArtist(final String artistName) {
        for (List<Playlist> playlists: database.getPlaylists().values()) {
            if (tool.playlistsHaveArtist(playlists, artistName))
                return true;
        }

        return false;
    }

}
