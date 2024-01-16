package app.pages.recommendations;

import app.player.entities.Player;
import app.player.entities.Playlist;
import app.player.entities.Song;
import app.properties.PlayableEntity;
import app.users.Accessor;
import app.users.User;
import app.utilities.SortAlphabeticallyByKey;
import app.utilities.SortByIntegerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlaylistRecommender extends Recommender {
    private final Accessor accessor;
    private static final int TRUNC_SIZE = 3;
    private static final int GENRE1 = 5;
    private static final int GENRE2 = 3;
    private static final int GENRE3 = 2;

    private static final int SIZE1 = 1;
    private static final int SIZE2 = 2;
    private static final int SIZE3 = 3;

    private final User user;
    private final Map<String, Integer> genreMap;

    public PlaylistRecommender(final Player player) {
        accessor = new Accessor();

        user = player.getUser();
        genreMap = new HashMap<>();
    }

    /**
     * Iterates over the list and counts each genre occurrence. The number of occurrences
     * are stored in the {@code genreMap}, if it doesn't contain the genre, otherwise, it adds
     * the number to the value from {@code genreMap}.
     * @param songs The songs to be iterated
     */
    public void countGenres(final List<Song> songs) {
        songs.forEach(song -> {
            String genre = song.getGenre().toLowerCase();
            if (!genreMap.containsKey(genre)) {
                genreMap.put(genre, 0);
            }

            int num = genreMap.get(genre);
            genreMap.put(genre, ++num);
        });
    }

    /**
     * Returns a list with the Top {@code N} Genres stored in {@code genreMap}. If there aren't
     * {@code N} genres, it returns a list with all the genres. They're sorted by the number of
     * occurrences, and for equal occurrences, lexicographically.
     *
     * @param trunc The {@code N} number
     * @return A list containing the Top {@code N } genres
     */
    public List<String> getTopGenres(final int trunc) {
        return genreMap.entrySet().stream()
                .sorted(new SortByIntegerValue<String>().reversed()
                        .thenComparing(new SortAlphabeticallyByKey<>()))
                .limit(trunc)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Returns a map containing all the songs from specified genre with their number of
     * listens.
     * @param genre The genre of the songs
     * @return A map with the songs found
     */
    public Map<Song, Integer> mapSongsFromGenreByListens(final String genre) {
        List<User> artists = accessor.getArtistWithListens();

        Map<Song, Integer> songsMap = new HashMap<>();

        artists.forEach(artist -> {
            Map<Song, Integer> temp = artist.getSongHistory().entrySet()
                    .stream().filter(entry -> entry.getKey()
                            .getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            songsMap.putAll(temp);
        });

        return songsMap;
    }

    /**
     * Sorts the map entries by values, and returns a list with the first {@code num} elements
     * after being sorted.
     * @param songs The songs mapped by their number of listens
     * @param num The number of songs to take
     * @return A list with {@code num} or fewer songs
     */
    public List<Song> takeFirstByListens(final Map<Song, Integer> songs, final int num) {
        List<Map.Entry<Song, Integer>> unrolled = new ArrayList<>(songs.entrySet());

        return unrolled.stream()
                .sorted(new SortByIntegerValue<Song>().reversed())
                .limit(num)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Returns the recommended playlist found. <br>
     * It first counts the genres from likes, playlists and follows. <br>
     * Then, it makes a top with those genres and takes the first 3. <br>
     * Finally, it takes the genre from the top it made previously, and for each of
     * them, it searches for the most listened songs with the same genre.
     *
     * @return The playlist recommended, if it can be found, {@code null otherwise}
     */
    @Override
    public PlayableEntity getRecommendation() {
        List<Song> playlistSongs = new ArrayList<>();

        countGenres(user.getLikes());

        List<Playlist> playlists = accessor.getUserPlaylists(user.getUsername());
        if (playlists != null) {
            playlists.forEach(playlist -> {
                countGenres(playlist.getSongs());
            });
        }

        user.getFollowing().forEach(playlist -> {
            countGenres(playlist.getSongs());
        });

        List<String> topGenres = getTopGenres(TRUNC_SIZE);

        int size = topGenres.size();
        Map<Song, Integer> temp;
        List<Song> foundSongsForGenre;
        if (size >= SIZE1) {
            temp = mapSongsFromGenreByListens(topGenres.get(0));
            foundSongsForGenre = takeFirstByListens(temp, GENRE1);
            playlistSongs.addAll(foundSongsForGenre);
        }

        if (size >= SIZE2) {
            temp = mapSongsFromGenreByListens(topGenres.get(1));
            foundSongsForGenre = takeFirstByListens(temp, GENRE2);
            playlistSongs.addAll(foundSongsForGenre);
        }

        if (size >= SIZE3) {
            temp = mapSongsFromGenreByListens(topGenres.get(2));
            foundSongsForGenre = takeFirstByListens(temp, GENRE3);
            playlistSongs.addAll(foundSongsForGenre);
        }

        if (playlistSongs.isEmpty()) {
            return null;
        }

        String playlistName = user.getUsername() + "'s recommendations";

        return new Playlist.Builder()
                .name(playlistName)
                .owner(user.getUsername())
                .ownerLink(user)
                .songs(playlistSongs)
                .build();
    }
}
