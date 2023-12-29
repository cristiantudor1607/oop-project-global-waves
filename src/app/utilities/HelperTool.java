package app.utilities;

import app.player.entities.*;
import app.users.User;

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

    public boolean playlistsHaveArtist(final List<Playlist> playlists, final String artistName) {
        for (Playlist p: playlists) {
            if (p.hasAudiofileFromUser(artistName))
                return true;
        }

        return false;
    }

    public void sortPlaylists(List<Playlist> publicPlaylists) {
        publicPlaylists.sort(new SortByFollowers().reversed());
    }

    public void sortPlaylistsByTime(List<Playlist> publicPlaylists) {
        publicPlaylists.sort(new SortByFollowers().reversed().thenComparing(new SortByCreationTime()));
    }

    public List<String> getUsernames(final List<User> users) {
        List<String> usernames = new ArrayList<>();

        for (User u: users)
            usernames.add(u.getUsername());

        return usernames;
    }

    public void setCreationTimestamp(final List<Song> songs, final int timestamp) {
        for (Song s : songs)
            s.setCreationTime(timestamp);
    }

    public List<Integer> getAscendingOrder(final int size) {
        List<Integer> order = new ArrayList<>();

        for (int i = 0; i < size; i++)
            order.add(i);

        return order;
    }

    public <T> void truncateResults(List<T> results) {
        if (results.size() < TRUNC_SIZE)
            return;

        results.subList(TRUNC_SIZE, results.size()).clear();
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
}
