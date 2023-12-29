package app.utilities.constants;

public final class StringConstants {
    private StringConstants() { }

    public static final String OFFLINE_DESCRIPTOR = " is offline.";
    public static final String DOESNT_EXIST = " doesn't exist.";

    // Load
    public static final String LOAD_SUCCESS = "Playback loaded successfully.";
    public static final String LOAD_EMPTY_SRC = "You can't load an empty audio collection!";
    public static final String LOAD_NO_SELECT =  "Please select a source before attempting "
            + "to load.";

    // PlayPause
    public static final String RESUMED = "Playback resumed successfully.";
    public static final String PAUSED = "Playback paused successfully.";
    public static final String PLAY_PAUSE_NO_SRC = "Please load a source before attempting to "
            + "pause or resume playback.";

    // AddRemove
    public static final String ADDED_TO_PLAYLIST = "Successfully added to playlist.";
    public static final String REMOVED_FROM_PLAYLIST = "Successfully removed from playlist.";
    public static final String NOT_A_SONG = "The loaded source is not a song.";
    public static final String PLAYLIST_NOT_EXIST = "The specified playlist does not exist.";
    public static final String ADD_REMOVE_NO_SOURCE = "Please load a source before adding to or "
            + "removing from the playlist.";

    // CreatePlaylist
    public static final String CREATE_SUCCESS = "Playlist created successfully.";
    public static final String PLAYLISTS_EXIST = "A playlist with the same name already exists.";

    // Shuffle
    public static final String SHUFFLE_ON = "Shuffle function activated successfully.";
    public static final String SHUFFLE_OFF = "Shuffle function deactivated successfully.";
    public static final String SHUFFLE_NOT_COLLECTION = "The loaded source is not a playlist "
            + "or an album.";
    public static final String SHUFFLE_NO_SRC = "Please load a source before using the "
            + "shuffle function.";

    // SwitchVisibility
    public static final String PLAYLIST_ID_HIGH = "The specified playlist ID is too high.";
    public static final String MADE_PRIVATE = "Visibility status updated successfully to "
            + "private.";
    public static final String MADE_PUBLIC = "Visibility status updated successfully to "
            + "public.";

    // AddAlbum
    public static final String IS_NOT_ARTIST = " is not an artist.";
    public static final String ALBUM_SAME_NAME = " has another album with the same name.";
    public static final String ALBUM_SAME_SONG = " has the same song at least twice in this album.";
    public static final String ADD_ALBUM_SUCCESS = " has added new album successfully.";
}
