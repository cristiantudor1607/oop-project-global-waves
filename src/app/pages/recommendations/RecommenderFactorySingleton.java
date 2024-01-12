package app.pages.recommendations;

import app.player.entities.Player;
import lombok.NonNull;

public class RecommenderFactorySingleton {
    private final static String RANDOM_SONG = "random_song";
    private final static String RANDOM_PLAYLIST = "random_playlist";
    private final static String FANS_PLAYLIST = "fans_playlist";

    private static RecommenderFactorySingleton instance = null;

    private RecommenderFactorySingleton() { }

    /**
     * Returns the unique recommender factory instance. It creates one when {@code getInstance}
     * is called first time.
     *
     * @return The only instance of the class
     */
    public static RecommenderFactorySingleton getInstance() {
        if (instance == null) {
            instance = new RecommenderFactorySingleton();
        }

        return instance;
    }

    /**
     * Creates a specific recommender based on the type provided.
     *
     * @param type The type of the recommender. It can be one of the following:
     *             <ul>
     *             <li><b>random_song</b> - creates a recommender that generates a song</li>
     *             <li><b>random_playlist</b> - creates a recommender that generates a
     *             user-specific playlist</li>
     *             <li><b>fans_playlist</b> - creates a recommender that generates a fans-specific
     *             playlist</li>
     *             </ul>
     * @param player The player of the user that requested the recommendation
     * @return The specific recommender, if the type is one of the above, {@code null} otherwise
     */
    public Recommender createRecommender(final String type, @NonNull final Player player) {
        if (type.equals(RANDOM_SONG)) {
            return new SongRecommender(player);
        }

        if (type.equals(RANDOM_PLAYLIST)) {
            return new PlaylistRecommender(player);
        }

        if (type.equals(FANS_PLAYLIST)) {
            return new FansPlaylistRecommender(player);
        }

        return null;
    }
}
