package globalwaves.player.entities.library;

import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import globalwaves.player.entities.Playlist;
import globalwaves.player.entities.Podcast;
import globalwaves.player.entities.Song;
import globalwaves.player.entities.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For Library, I'll use the Singleton Pattern. I designed it as a Database, and I was
 * inspired by the Database exercise from Lab
 */

public class Library {
    private static Library instance = null;

    /**
     * List of all songs available in the Library
     */
    @Getter
    private List<Song> songs = null;

    /**
     * List of all podcasts available in the Library
     */
    @Getter
    private List<Podcast> podcasts = null;

    /**
     * List of all users
     */
    @Getter
    private List<User> users = null ;

    @Getter
    private Map<String, List<Playlist>> playlists = null;


    private Library() {
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        users = new ArrayList<>();
        playlists = new HashMap<>();
    }

    public static Library getInstance() {
        if (instance == null)
            instance = new Library();

        return instance;
    }

    public void addSong(Song newSong) {
        songs.add(newSong);
    }

    public void addPodcast(Podcast newPodcast) {
        podcasts.add(newPodcast);
    }

    public void addUser(User newUser) {
        users.add(newUser);
    }

    public void addPlaylist(String owner, Playlist newPlaylist) {
        List<Playlist> ownerPlaylists = playlists.get(owner);
        ownerPlaylists.add(newPlaylist);
    }

    /**
     *
     * @param library input library from which the songs are loaded to the
     *                Library Database
     */
    public void loadSongs(LibraryInput library) {
        if (!songs.isEmpty())
            return;

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
        if (!podcasts.isEmpty())
            return;

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
        if (!users.isEmpty())
            return;

        ArrayList<UserInput> inputs = library.getUsers();
        for (UserInput inputFormatUser: inputs) {
            User myUserFormat = new User(inputFormatUser);
            addUser(myUserFormat);
        }
    }

    public void initPlaylists() {
        if (users.isEmpty())
            return;

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
