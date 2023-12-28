package app.management;

import app.player.entities.Album;
import app.player.entities.Playlist;
import app.player.entities.Podcast;
import app.player.entities.Song;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import app.pages.Page;
import app.users.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Library {
    private static Library instance = null;

    @Getter
    private final List<Song> songs;

    @Getter
    private final Map<String, List<Song>> addedSongs;

    @Getter
    private final List<Podcast> podcasts;

    @Getter
    private final Map<String, List<Podcast>> addedPodcasts;

    @Getter
    private final List<User> users;

    @Getter
    private final List<User> artists;

    @Getter
    private final List<User> hosts;

    @Getter
    private final Map<String, List<Playlist>> playlists;


    private Library() {
        songs = new ArrayList<>();
        addedSongs = new HashMap<>();
        podcasts = new ArrayList<>();
        addedPodcasts = new HashMap<>();
        users = new ArrayList<>();
        artists = new ArrayList<>();
        hosts = new ArrayList<>();
        playlists = new HashMap<>();
    }

    /**
     * Returns the instance of the class. If instance is null, it creates one
     * @return The instance of the class (it can't return null)
     */
    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }

        return instance;
    }

    /**
     * Resets the instance. It breaks a little bit the rules of singleton, but it's designed
     * for a specific usage, at the end of action, to prevent loading the library multiple times.
     */
    public static void deleteInstance() {
        instance = null;
    }

    /**
     * Maps all artist pages into a list.
     * @return The list of artist pages
     */
    public List<Page> getArtistPages() {
        List<Page> pages = new ArrayList<>();

        for (User user: artists) {
            pages.add(user.getPage());
        }

        return pages;
    }

    /**
     * Maps all host pages into a list.
     * @return The list of host pages
     */
    public List<Page> getHostPages() {
        List<Page> pages = new ArrayList<>();

        for (User user: hosts) {
            pages.add(user.getPage());
        }

        return pages;
    }

    /**
     * Adds a new normal user in the library. If the given user is an artist or a host,
     * it does nothing.
     * @param newUser New user to be added to the normal users database
     * @return true, if the user was added successfully, false otherwise
     */
    public boolean addUser(final User newUser) {
        if (!newUser.isNormalUser()) {
            return false;
        }

        playlists.put(newUser.getUsername(), new ArrayList<>());
        return users.add(newUser);
    }

    /**
     * Adds a new artist in the library. If the given user is a host or a normal user,
     * it does nothing.
     * @param newArtist New user to be added to the artists database
     * @return true, if the user was successfully added to the artists database, false otherwise
     */
    public boolean addArtist(final User newArtist) {
        if (!newArtist.isArtist()) {
            return false;
        }

        return artists.add(newArtist);
    }

    /**
     * Adds a new host in the library. If the given user is an artist or a normal user, it
     * does nothing.
     * @param newHost New user to be added to the hosts database
     * @return true, if the user was successfully added to the hosts database, false otherwise
     */
    public boolean addHost(final User newHost) {
        if (!newHost.isHost()) {
            return false;
        }

        return hosts.add(newHost);
    }

    /**
     * Remove all songs from an album added by the given artist.
     * @param artistName The name of the artist that owns the album
     * @param albumName The name of the album where the songs belong
     */
    public void removeSongsFromAlbum(final String artistName, final String albumName) {
        addedSongs.get(artistName).removeIf(songFromArtist ->
                songFromArtist.getAlbum().equals(albumName));
    }

    /**
     * Remove all songs that belong to the given album from the users liked songs
     * It removes for all users in the database.
     * @param albumName The name of the album where the songs belongs
     */
    public void removeSongsFromLiked(final String albumName) {
        users.forEach((user -> user.getLikes()
                .removeIf(likedSong -> likedSong.getAlbum().equals(albumName))));
    }

    /**
     * Unfollows each playlist that the user is following.
     * @param normalUser The user that should unfollow everything. If the user isn't a normal
     *                   user, it does nothing
     */
    public void removeAllFollowsFromUser(final User normalUser) {
        if (!normalUser.isNormalUser()) {
            return;
        }

        for (User user: users) {
            playlists.get(user.getUsername())
                    .forEach((playlist -> playlist.getUnfollowedBy(normalUser)));
        }

        normalUser.setFollowing(new ArrayList<>());
    }

    /**
     * Removes a user form the database, and break all connections with other users. Breaking
     * a connection means:
     * <ul>
     *     <li>
     *         for normal users : every playlist of the user is deleted, and everyone will
     *         automatically unfollow the playlist. Their liked songs will also be automatically
     *         unliked
     *     </li>
     *     <li>
     *          for artists : all albums are deleted. The album's songs gets automatically unliked
     *          by everyone
     *     </li>
     *     <li>
     *         for hosts: podcasts are deleted
     *     </li>
     * </ul>
     * @param oldUser The user to be removed. It should be previously checked if the user can be
     *                deleted
     */
    public void removeUser(final User oldUser) {
        if (oldUser.isNormalUser()) {
            // Unfollow all playlists
            removeAllFollowsFromUser(oldUser);

            String username = oldUser.getUsername();

            // Unfollow every playlist of the user from the others perspective
            playlists.get(username)
                    .forEach((playlist -> users.forEach((user -> user.unfollow(playlist)))));

            // Remove the map entry for playlists of the user
            playlists.remove(username);

            // Remove the user
            users.remove(oldUser);
        }

        if (oldUser.isArtist()) {
            for (Album album: oldUser.getAlbums()) {
                String albumName = album.getName();
                removeSongsFromAlbum(oldUser.getUsername(), albumName);
                removeSongsFromLiked(albumName);
            }

            artists.remove(oldUser);
        }

        if (oldUser.isHost()) {
            addedPodcasts.remove(oldUser.getUsername());
            hosts.remove(oldUser);
        }
    }

    /**
     * Adds a new playlists to the library.
     * @param owner The username of the user that created the playlist
     * @param newPlaylist The new playlist to be added
     */
    public void addPlaylist(final String owner, final Playlist newPlaylist) {
        List<Playlist> ownerPlaylists = playlists.computeIfAbsent(owner, k -> new ArrayList<>());
        ownerPlaylists.add(newPlaylist);
    }

    /**
     * Converts all input songs to the new format and stores them in the library.
     * @param library The library that contains songs as SongInput class instances
     */
    public void loadSongs(final LibraryInput library) {
        ArrayList<SongInput> inputs = library.getSongs();
        for (SongInput inputFormatSong: inputs) {
            /* Convert SongInput to Song, and add it to the List */
            Song mySongFormat = new Song(inputFormatSong);
            songs.add(mySongFormat);
        }
    }

    /**
     * Converts all input podcasts to the new format and stores them in the library.
     * @param library The library that contains podcasts as PodcastInput class instances
     */
    public void loadPodcasts(final LibraryInput library) {
        ArrayList<PodcastInput> inputs = library.getPodcasts();
        for (PodcastInput inputFormatPodcast: inputs) {
            Podcast myPodcastFormat = new Podcast(inputFormatPodcast);
            podcasts.add(myPodcastFormat);
        }
    }

    /**
     * Converts all input users to the new format and stores them in the library.
     * @param library The library that contains users as UserInput class instances
     */
    public void loadUsers(final LibraryInput library) {
        ArrayList<UserInput> inputs = library.getUsers();
        for (UserInput inputFormatUser: inputs) {
            User myUserFormat = new User(inputFormatUser);
            addUser(myUserFormat);
        }
    }

    /**
     * It creates a new list to store each user playlists.
     * <b>Method needs to be called only after the input users were added to the library.</b>
     */
    public void initPlaylists() {
        for (User user : users) {
            List<Playlist> userPlaylists = new ArrayList<>();
            String username = user.getUsername();
            playlists.put(username, userPlaylists);
        }
    }

    /**
     * Converts the input library to the current library format.
     * @param library The library as LibraryInput class instance
     */
    public void convertInputLibrary(final LibraryInput library) {
        loadUsers(library);
        loadSongs(library);
        loadPodcasts(library);
        initPlaylists();
    }

}