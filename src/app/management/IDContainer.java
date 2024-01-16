package app.management;

public final class IDContainer {
    private static IDContainer instance;
    private int songId;
    private int albumId;
    private int userId;
    private int playlistId;
    private int inboxId;

    private IDContainer() {
        songId = 1;
        albumId = 1;
        userId = 1;
        playlistId = 1;
        inboxId = 1;
    }

    /**
     * Returns the only instance of the class.
     *
     * @return The instance of the class
     */
    public static IDContainer getInstance() {
        if (instance == null) {
            instance = new IDContainer();
        }

        return instance;
    }

    /**
     * Resets the instance.
     * It is necessarily because the tests are called within a function,
     * and they aren't verified separately.
     * It doesn't respect the Singleton pattern at all, but it is designed just for this
     * specific usage.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Uses the current songId. After calling this method, the id can never be used
     * again.
     * @return The song id
     */
    public int useSongId() {
        songId++;
        return songId - 1;
    }

    /**
     * Uses the current albumId. After calling this method, the id can never be used
     * again.
     * @return The album id
     */
    public int useAlbumId() {
        albumId++;
        return albumId - 1;
    }

    /**
     * Uses the current userId. After calling this method, the id can never be used
     * again.
     * @return The user id
     */
    public int useUserId() {
        userId++;
        return userId - 1;
    }

    /**
     * Uses the current playlistId. After calling this method, the id can never be used again.
     * @return The playlist id
     */
    public int usePlaylistId() {
        playlistId++;
        return playlistId - 1;
    }

    /**
     * Uses the current inboxId. After calling this method, the id can never be used again.
     * @return The inbox id
     */
    public int useInboxId() {
        inboxId++;
        return inboxId - 1;
    }
}
