package globalwaves.player.entities.library;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import globalwaves.player.entities.*;
import globalwaves.player.entities.paging.Page;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Library {
    private static Library instance = null;

    @Getter
    private List<Song> songs;

    @Getter
    private Map<String, List<Song>> addedSongs;

    @Getter
    private List<Podcast> podcasts;

    @Getter
    private Map<String, List<Podcast>> addedPodcasts;

    @Getter
    private List<User> users;

    @Getter
    private List<User> artists;

    @Getter
    private List<User> hosts;

    @Getter
    private Map<String, List<Playlist>> playlists;


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

    public static Library getInstance() {
        if (instance == null)
            instance = new Library();

        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public List<Page> getArtistPages() {
        List<Page> pages = new ArrayList<>();

        for (User user: artists)
            pages.add(user.getPage());

        return pages;
    }

    public List<Page> getHostPages() {
        List<Page> pages = new ArrayList<>();

        for (User user: hosts)
            pages.add(user.getPage());

        return pages;
    }

    public void addSong(Song newSong) {
        songs.add(newSong);
    }

    public void addPodcast(Podcast newPodcast) {
        podcasts.add(newPodcast);
    }

    public boolean addUser(User newUser) {
        if (!newUser.isNormalUser())
            return false;

        return users.add(newUser);
    }

    public boolean addArtist(User newArtist) {
        if (!newArtist.isArtist())
            return false;

        return artists.add(newArtist);
    }

    public boolean addArtist(Artist newArtist) {
        return artists.add(newArtist);
    }

    public boolean addHost(User newHost) {
        if (!newHost.isHost())
            return false;

        return hosts.add(newHost);
    }

    public boolean addHost(Host newHost) {
        return hosts.add(newHost);
    }

    public void addPlaylist(String owner, Playlist newPlaylist) {
        List<Playlist> ownerPlaylists = playlists.get(owner);
        if (ownerPlaylists == null) {
            ownerPlaylists = new ArrayList<>();
            playlists.put(owner, ownerPlaylists);
        }
        ownerPlaylists.add(newPlaylist);
    }

    /**
     *
     * @param library input library from which the songs are loaded to the
     *                Library Database
     */
    public void loadSongs(LibraryInput library) {
        ArrayList<SongInput> inputs = library.getSongs();
        for (SongInput inputFormatSong: inputs) {
            /* Convert SongInput to Song, and add it to the List */
            Song mySongFormat = new Song(inputFormatSong);
            addSong(mySongFormat);
        }
    }

    /**
     *
     * @param library input library from which the songs are loaded to the
     *                Library Database
     */
    public void loadPodcasts(LibraryInput library) {
        ArrayList<PodcastInput> inputs = library.getPodcasts();
        for (PodcastInput inputFormatPodcast: inputs) {
            Podcast myPodcastFormat = new Podcast(inputFormatPodcast);
            addPodcast(myPodcastFormat);
        }
    }

    /**
     *
     * @param library input library from which the songs are loaded to the
     *                Library Database
     */
    public void loadUsers(LibraryInput library) {
        ArrayList<UserInput> inputs = library.getUsers();
        for (UserInput inputFormatUser: inputs) {
            User myUserFormat = new User(inputFormatUser);
            addUser(myUserFormat);
        }
    }

    public void initPlaylists() {
        for (User user : users) {
            List<Playlist> userPlaylists = new ArrayList<>();
            String username = user.getUsername();
            playlists.put(username, userPlaylists);
        }
    }

    public void loadLibraryData(LibraryInput library) {
        loadUsers(library);
        loadSongs(library);
        loadPodcasts(library);
        initPlaylists();
    }

}
