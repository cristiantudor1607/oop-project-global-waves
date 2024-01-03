package app.utilities;

import app.player.entities.Playlist;
import app.player.entities.Song;
import app.properties.NamedObject;
import app.users.User;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HelperTool {
    private static HelperTool instance = null;
    public static final int TRUNC_SIZE = 5;
    private HelperTool() { }

    /**
     * Returns the single instance of the class.
     * @return The instance of the class
     */
    public static HelperTool getInstance() {
        if (instance == null) {
            instance = new HelperTool();
        }

        return instance;
    }

    /**
     * Returns a list with the names of all users from the provided list.
     * @param users The list of users
     * @return A list containig the usernames
     */
    public List<String> getUsernames(final List<User> users) {
        List<String> usernames = new ArrayList<>();

        for (User u: users) {
            usernames.add(u.getUsername());
        }

        return usernames;
    }

    /**
     * Returns a list containing all the numbers from {@code 0} to {@code size}, in ascending
     * order.
     * @param size The size of the list, and also the upper bound for generating the numbers
     * @return The list containing all the number form {@code 0} to {@code size}
     */
    public List<Integer> getIndexesList(final int size) {
        List<Integer> order = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            order.add(i);
        }

        return order;
    }

    /**
     * Sets the creation time of all songs in the list to {@code timestamp}.
     * @param songs The list of songs whose {@code creationTime} will be setted
     * @param timestamp The creation time of the songs
     */
    public void setCreationTimestamp(final List<Song> songs, final int timestamp) {
        for (Song s : songs) {
            s.setCreationTime(timestamp);
        }
    }

    /**
     * Sorts descending the playlists by followers number. For playlists with the
     * same number of followers, they're sorted by the time they're created.
     * @param playlists The playlists to be sorted
     */
    public void sortPlaylistsByFollowers(final List<Playlist> playlists) {
        playlists.sort(new SortByFollowers().reversed()
                .thenComparing(new SortByCreationTime()));
    }

    /**
     * Checks if at least one of the playlists has a song that belongs to the artist
     * with the given name.
     * @param playlists The playlists were the artist is searched
     * @param artistName The name of the artist to be searched
     * @return true, if there was found at least one song that belongs to the artist
     * with the given name, false, otherwise
     */
    public boolean playlistsHaveArtist(final List<Playlist> playlists, final String artistName) {
        for (Playlist p: playlists) {
            if (p.hasAudiofileFromUser(artistName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Truncates the list of objects to the first {@code TRUNC_SIZE} elements.
     * @param objects The list of objects to be truncated
     * @param <T> The type of objects. It can be any class that extends {@code Object}
     */
    public <T> void truncateResults(final List<T> objects) {
        if (objects.size() < TRUNC_SIZE) {
            return;
        }

        objects.subList(TRUNC_SIZE, objects.size()).clear();
    }

    /**
     * Checks if the list has the same element at least twice. It uses
     * {@code equals} to compare the elements of the list. The method isn't
     * optimised, it has the time complexity of <b>O(n<sup>2</sup>)</b>.
     * @param elements The list of objects
     * @return true, if the list contains one element at least twice,
     * false, otherwise
     * @param <T> The type of the objects. It can be any class that extends
     *           {@code Object}
     */
    public <T> boolean hasSameElementTwice(final List<T> elements) {
        for (int i = 0; i < elements.size() - 1; i++) {
            for (int j = i + 1; j < elements.size(); j++) {
                if (elements.get(i).equals(elements.get(j))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Takes a map which stores named objects and a counter, and converts it to
     * a list of entries, in order to be sorted.
     * @param history The map to be converted
     * @return A list containing all entries from the given map
     */
    public <T extends NamedObject> List<Map.Entry<String, Integer>>
    unrollHistoryData(final Map<T, Integer> history) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        for (Map.Entry<T, Integer> pair : history.entrySet()) {
            list.add(new AbstractMap.SimpleEntry<>(pair.getKey().getName(), pair.getValue()));
        }

        return list;
    }
}
