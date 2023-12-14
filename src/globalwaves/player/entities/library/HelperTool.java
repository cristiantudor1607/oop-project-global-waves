package globalwaves.player.entities.library;

import globalwaves.player.entities.*;
import globalwaves.player.entities.utilities.SortByCreationTime;
import globalwaves.player.entities.utilities.SortByFollowers;
import globalwaves.player.entities.utilities.SortByInteger;
import globalwaves.player.entities.utilities.SortByLibraryOrder;

import java.util.*;

public class HelperTool {
    private static HelperTool instance = null;
    public static final int TRUNC_SIZE = 5;
    private HelperTool() { }

    public static HelperTool getInstance() {
        if (instance == null)
            instance = new HelperTool();

        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public boolean playlistsHaveArtist(final List<Playlist> playlists, final String artistName) {
        for (Playlist p: playlists) {
            if (p.hasAudiofileFromUser(artistName))
                return true;
        }

        return false;
    }

    public boolean playlistsHaveAlbum(final List<Playlist> playlists, final String albumName) {
        for (Playlist p: playlists) {
            if (p.hasSongFromAlbum(albumName))
                return true;
        }

        return false;
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

    public void sortByLibrary(List<Map.Entry<AudioFile, Integer>> sortedLikes) {
        sortedLikes.sort(new SortByLibraryOrder());
    }

    public void sortPlaylists(List<Playlist> publicPlaylists) {
        publicPlaylists.sort(new SortByFollowers());
    }

    public void sortPlaylistsByTime(List<Playlist> publicPlaylists) {
        publicPlaylists.sort(new SortByCreationTime());
    }

    public List<String> getUsernames(final List<User> users) {
        List<String> usernames = new ArrayList<>();

        for (User u: users)
            usernames.add(u.getUsername());

        return usernames;
    }

    // TODO: remove this if hasSameElementTwice works
    public boolean hasSameSongAtLeastTwice(final List<Song> songs) {
        for (int i = 0; i < songs.size() - 1; i++) {
            for (int j = i + 1; j < songs.size(); j++) {
                if (songs.get(i).compareTo(songs.get(j)) == 0)
                    return true;
            }
        }

        return false;
    }

    public <T> boolean hasSameElementTwice(final List<T> elements) {
        for (int i = 0; i < elements.size() - 1; i++) {
            for (int j = i + 1; j < elements.size(); j++) {
                if (elements.get(i).equals(elements.get(j)))
                    return true;
            }
        }

        return false;
    }


    public void setCreationTimestamp(final List<Song> songs, final int timestamp) {
        for (Song s : songs)
            s.setCreationTime(timestamp);
    }

    public <T> void truncateResults(List<T> results) {
        if (results.size() < TRUNC_SIZE)
            return;

        results.subList(TRUNC_SIZE, results.size()).clear();
    }

}
