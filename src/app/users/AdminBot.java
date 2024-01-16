package app.users;

import app.player.entities.Album;
import app.player.entities.Playlist;
import app.player.entities.Podcast;
import app.player.entities.Song;
import app.utilities.HelperTool;
import app.utilities.SortByCreatorId;
import app.utilities.SortByUniqueId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminBot extends Admin {
    private final HelperTool tool;

    public AdminBot() {
        super();
        tool = HelperTool.getInstance();
    }

    /**
     * Returns the user that have the given username.
     * @param username The username of the user
     * @return The user, if it exists, {@code null} otherwise
     */
    public User getUserByUsername(final String username) {
        for (User matchUser : database.getUsers()) {
            if (matchUser.getUsername().equals(username)) {
                return matchUser;
            }
        }

        for (User matchArtist: database.getArtists()) {
            if (matchArtist.getUsername().equals(username)) {
                return matchArtist;
            }
        }

        for (User matchHost: database.getHosts()) {
            if (matchHost.getUsername().equals(username)) {
                return matchHost;
            }
        }

        return null;
    }

    /**
     * Returns the artist with the given username. Wrapper over the same method
     * from {@code Library} class.
     * @param username The username of the artist
     * @return The artist, if it exists, {@code null} otherwise
     */
    public User getArtistByUsername(final String username) {
        return database.getArtistByUsername(username);
    }

    /**
     * Returns the host with the given username. Wrapper over the same method
     * from {@code Library} class.
     * @param username The username of the host
     * @return The host, if it exists, {@code null} otherwise
     */
    public User getHostByUsername(final String username) {
        return database.getHostByUsername(username);
    }

    /**
     * Returns all users that are online.
     * @return A list of online users
     */
    public List<User> getOnlineUsers() {
        List<User> onlineUsers = new ArrayList<>();

        for (User user: database.getUsers()) {
            if (user.isOnline()) {
                onlineUsers.add(user);
            }
        }

        return onlineUsers;
    }

    /**
     * Returns all existing users.
     * @return A list of all existing users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        users.addAll(database.getUsers());
        users.addAll(database.getArtists());
        users.addAll(database.getHosts());

        return users;
    }

    /**
     * Returns all songs from library as a list.
     * @return A list of songs
     */
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>(database.getAds());

        for (List<Song> listOfSongs : database.getSongs().values()) {
            songs.addAll(listOfSongs);
        }

        return songs;
    }


    /**
     * Checks if the username exists. Wrapper over the {@code usernameIsTaken} method
     * from {@code Library} class.
     * @param username The username to be checked
     * @return {@code true}, if the username is taken, {@code false} otherwise
     */
    public boolean checkUsername(final String username) {
        return database.usernameIsTaken(username);
    }

    /**
     * Checks if the user has an album with the name specified.
     * @param user User to be inspected.
     * @param albumName The name of the album.
     * @return {@code false}, if the user isn't an artist, or it doesn't have an album with the
     * specified name, or {@code true}, if it has
     */
    public boolean checkAlbumNameForUser(final User user, final String albumName) {
        if (!user.isArtist()) {
            return false;
        }

        return user.getAlbum(albumName) != null;
    }

    /**
     * Checks if the user has a podcast with the name specified.
     * @param user User to be inspected.
     * @param podcastName The name of the podcast.
     * @return {@code false}, if the user isn't a host, or it doesn't have a podcast with the given
     * name, or {@code true}, if it has
     */
    public boolean checkPodcastNameForUser(final User user, final String podcastName) {
        if (!user.isHost()) {
            return false;
        }

        return user.getPodcast(podcastName) != null;
    }

    /**
     * Checks if the user has a playlist with the name specified.
     * @param owner The username of the user to be inspected.
     * @param playlistName The name of the playlist.
     * @return {@code false}, if the user isn't a normal user, or it doesn't have
     * a playlist with the specified name, or {@code true}, if it has
     */
    public boolean checkPlaylistNameForUser(final String owner, final String playlistName) {
        if (!getUserByUsername(owner).isNormalUser()) {
            return false;
        }

        List<Playlist> ownerPlaylists =  database.getPlaylists().get(owner);
        if (ownerPlaylists == null) {
            return false;
        }

        for (Playlist p : ownerPlaylists) {
            if (p.getName().equals(playlistName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the playlist with the given id.
     * @param owner The owner of the playlist
     * @param id The id of the playlist
     * @return The playlist, if {@code id} is valid, {@code null}, if the id is out of bounds
     */
    public Playlist getUserPlaylist(final String owner, final int id) {
        List<Playlist> ownerPlaylists = database.getPlaylists().get(owner);

        if (id > ownerPlaylists.size() || id < 1) {
            return null;
        }

        return ownerPlaylists.get(id - 1);
    }

    /**
     * Creates a new playlist.
     * @param owner The username of the playlist owner
     * @param playlistName The name of the playlist
     * @param time The time when playlist is created
     */
    public void createPlaylist(final String owner, final String playlistName, final int time) {
        User ownerProfile = getUserByUsername(owner);

        Playlist newOwnerPlaylist = new Playlist.Builder()
                .name(playlistName)
                .owner(owner)
                .creationTime(time)
                .ownerLink(ownerProfile)
                .build();

        database.addPlaylist(owner, newOwnerPlaylist);
    }

    /**
     * Returns all playlists owned by a user
     * @param owner The username of the playlists owner
     * @return A list of playlists, if the owner doesn't exist, or it isn't a normal user,
     * {@code null} otherwise
     */
    public List<Playlist> getUserPlaylists(final String owner) {
        User user = getUserByUsername(owner);
        if (user == null) {
            return null;
        }

        if (!user.isNormalUser()) {
            return null;
        }

        return database.getPlaylists().get(owner);
    }

    /**
     * Returns all public playlists of the given user.
     * @param username The username of the user
     * @return A list of public playlists
     */
    public List<Playlist> getUserPublicPlaylists(final String username) {
        List<Playlist> publicPlaylists = new ArrayList<>();

        for (Playlist p : database.getPlaylists().get(username)) {
            if (p.isPublic()) {
                publicPlaylists.add(p);
            }
        }

        return publicPlaylists;
    }

    /**
     * Returns all available playlists for the specified user. The available playlists
     * are retrieved by making a list with the user playlists with the addition of all
     * public playlists from all users
     * @param username The username of the user whose playlists should be returned
     * @return A list of playlists
     */
    public List<Playlist> getAvailablePlaylists(final String username) {
        List<Playlist> availablePlaylists = new ArrayList<>();

        List<Playlist> ownedPlaylists = database.getPlaylists().get(username);
        if (ownedPlaylists != null) {
            availablePlaylists.addAll(ownedPlaylists);
        }

        for (String otherUser : database.getPlaylists().keySet()) {
            if (username.equals(otherUser)) {
                continue;
            }

            List<Playlist> otherPublicPlaylists = getUserPublicPlaylists(otherUser);
            availablePlaylists.addAll(otherPublicPlaylists);
        }

        return availablePlaylists;
    }

    /**
     * Returns all public playlists.
     * @return A list of all public playlists
     */
    public List<Playlist> getPublicPlaylists() {
        List<Playlist> publicPlaylists = new ArrayList<>();

        for (String username : database.getPlaylists().keySet()) {
            List<Playlist> userPlaylists = database.getPlaylists().get(username);
            for (Playlist p : userPlaylists) {
                if (p.isPublic()) {
                    publicPlaylists.add(p);
                }
            }
        }

        return publicPlaylists;
    }

    /**
     * Returns all albums.
     * @return A list of all albums
     */
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();

        database.getArtists().forEach((artist -> albums.addAll(artist.getAlbums())));

        return albums;
    }

    /**
     * Returns all artists.
     * @return A list of all artists
     */
    public List<User> getArtists() {
        return database.getArtists();
    }

    /**
     * Returns a list of all artist albums.
     * @param username The username of the artist
     * @return A list of albums
     */
    public List<Album> getArtistAlbums(final String username) {
        User artist = getArtistByUsername(username);

        return artist.getAlbums();
    }


    /**
     * Returns a list of all host podcasts.
     * @param username The username of the host
     * @return A list of podcasts
     */
    public List<Podcast> getHostPodcasts(final String username) {
        User host = getHostByUsername(username);

        return host.getPodcasts();
    }

    /**
     * Adds a list of songs from an artist to the database.
     * @param artist The artist whose songs are added
     * @param songs The songs to be added
     */
    public void addSongsToLibrary(final String artist, final List<Song> songs) {
        List<Song> artistSongs = database.getSongs()
                .computeIfAbsent(artist, k -> new ArrayList<>());

        artistSongs.addAll(songs);
    }

    /**
     * Adds a podcast from a host to the database.
     * @param host The host whose podcast is added
     * @param podcast The podcast to be added
     */
    public void addPodcastToLibrary(final String host, final Podcast podcast) {
        List<Podcast> hostPodcasts = database.getPodcasts()
                .computeIfAbsent(host, k -> new ArrayList<>());

        hostPodcasts.add(podcast);
    }

    /**
     * Checks if the artist with the given name has at least one song that is being used
     * in a playlist. It doesn't have to be listened by someone, it has to be added in the
     * playlist.
     * @param artistName The name of the artist
     * @return {@code true}, if a song from artist was found in at least one playlist, {@code
     * false} otherwise
     */
    public boolean artistHasSongInPlaylists(final String artistName) {
        for (List<Playlist> playlists: database.getPlaylists().values()) {
            if (tool.playlistsHaveArtist(playlists, artistName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Moves default podcasts owned by host to its profile. It adds the podcasts to the
     * host's list and removes them from defaultPodcasts.
     * @param host The host whose podcasts should be moved
     */
    public void movePodcastsFromDefaultToHost(final User host) {
        String username = host.getUsername();
        database.getDefaultPodcasts().forEach(podcast -> {
            if (podcast.getOwner().equals(username)) {
                tool.setHostLinks(podcast.getEpisodes(), host);
                tool.setPodcastLink(podcast.getEpisodes(), podcast);
                host.addPodcast(podcast);
                database.getPodcasts()
                        .computeIfAbsent(username, k -> new ArrayList<>())
                        .add(podcast);
            }
        });

        database.getDefaultPodcasts()
                .removeIf(podcast -> podcast.getOwner().equals(username));

    }

    /**
     * Returns the first ad found in library.
     * @return An {@code Optional} containing the ad, if it was found,
     * an empty {@code Optional} otherwise
     */
    public Optional<Song> getFirstAd() {
        return database.getAds().stream()
                .filter(song -> song.getGenre().equalsIgnoreCase("advertisement"))
                .findFirst();
    }

    /**
     * Returns a list with all database songs that have the same genre. The list is sorted
     * by song's id.
     * @param genre The genre of the songs.
     * @return A list with all songs from same genre found in library.
     */
    public List<Song> getSongsFromSameGenre(final String genre) {
        List<Song> foundSongs = new ArrayList<>();
        database.getSongs().forEach((ignored, songList) -> {
            List<Song> currEntrySongs = songList.stream()
                    .filter(song -> song.getGenre().equalsIgnoreCase(genre))
                    .toList();
            foundSongs.addAll(currEntrySongs);
        });

        foundSongs.sort(new SortByUniqueId());

        return foundSongs;
    }

    /**
     * Returns a list with the artists that have at least one listened song.
     *
     * @return A list with the artist with listens
     */
    public List<User> getArtistWithListens() {
        return database.getArtists()
                .stream()
                .filter(User::wasListened)
                .toList();
    }

}
