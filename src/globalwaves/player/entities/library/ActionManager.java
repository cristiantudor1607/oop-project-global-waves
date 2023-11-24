package globalwaves.player.entities.library;

import globalwaves.commands.*;
import globalwaves.commands.enums.*;
import globalwaves.parser.commands.CommandObject;
import globalwaves.player.entities.*;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter @Setter
public class ActionManager {
    private LibraryInterrogator interrogator;
    private SearchBar searchBar;
    private Map<String, Player> players;
    private int lastActionTime;
    // TODO : Get rid of lastAction field
    private CommandObject lastAction;

    public ActionManager() {
        interrogator = new LibraryInterrogator();
        searchBar = new SearchBar();
        players = new HashMap<>();
        for (User user : Library.getInstance().getUsers()) {
            players.put(user.getUsername(), new Player());
        }

        lastActionTime = 0;
        lastAction = null;
    }

    public Player requestPlayer(CommandObject executingQuery) {
        String username = executingQuery.getUsername();

        return players.get(username);
    }

    public void requestSearchResult(SearchInterrogator executingSearch) {
        // First stop the player
        Player userPlayer = requestPlayer(executingSearch);
        userPlayer.stopPlayer();

        searchBar.setResults(executingSearch.getSearchResults());

    }

    public SelectExit.code requestItemSelection(SelectInterrogator executingSelect) {
        int itemNumber = executingSelect.getItemNumber();

        if (searchBar.wasNotInvoked())
            return SelectExit.code.NO_LIST;

        if (!searchBar.invalidItem(itemNumber) || searchBar.hasNoSearchResult())
            return SelectExit.code.OUT_OF_BOUNDS;

        PlayableEntity entity = searchBar.getResultAtIndex(itemNumber - 1);
        Player userPlayer = players.get(executingSelect.getUsername());
        userPlayer.select(entity);
        executingSelect.setSelectedAudio(entity);

        // Reset the Search Bar after selection
        searchBar.reset();

        return SelectExit.code.SELECTED;
    }

    public LoadExit.code requestLoading(LoadInterrogator executingLoad) {
        Player userPlayer = players.get(executingLoad.getUsername());

        if (!userPlayer.hasSourceSelected())
            return LoadExit.code.NO_SOURCE_SELECTED;

        if (userPlayer.hasNoSource())
            return LoadExit.code.NO_SOURCE_SELECTED;

        if (userPlayer.hasEmptySource())
            return LoadExit.code.EMPTY_SOURCE;

        userPlayer.load();

        return LoadExit.code.LOADED;
    }

    public PlayPauseExit.code requestUpdateState(PlayPauseInterrogator executingQuery) {
        Player userPlayer = players.get(executingQuery.getUsername());

        if (userPlayer.hasNoSource())
            return PlayPauseExit.code.NO_SOURCE;

        if (userPlayer.isPlaying()) {
            userPlayer.pause();
            return PlayPauseExit.code.PAUSED;
        }

        userPlayer.play();

        return PlayPauseExit.code.RESUMED;
    }

    public CreationExit.code requestPlaylistCreation(CreatePlaylistInterrogator execQuery) {
        String owner = execQuery.getUsername();
        String playlistName = execQuery.getPlaylistName();
        int timestamp = execQuery.getTimestamp();

        if (interrogator.ownerHasPlaylist(owner, playlistName))
            return CreationExit.code.ALREADY_EXISTS;

        interrogator.createPlaylist(owner, playlistName, timestamp);

        return CreationExit.code.CREATED;
    }

    public SwitchVisibilityExit.code requestSwitch(SwitchVisibilityInterrogator execQuery) {
        int id = execQuery.getPlaylistId();
        String owner = execQuery.getUsername();

        Playlist ownerPlaylist = interrogator.getOwnerPlaylistById(owner, id);
        if (ownerPlaylist == null)
            return SwitchVisibilityExit.code.TOO_HIGH;

        if (ownerPlaylist.isPublic()) {
            ownerPlaylist.makePrivate();
            return SwitchVisibilityExit.code.MADE_PRIVATE;
        }

        ownerPlaylist.makePublic();

        return SwitchVisibilityExit.code.MADE_PUBLIC;
    }

    public AddRemoveExit.code requestAddRemove(AddRemoveInterrogator execQuery) {
        String owner = execQuery.getUsername();
        int id = execQuery.getPlaylistId();
        Playlist ownerPlaylist = interrogator.getOwnerPlaylistById(owner, id);
        Player ownerPlayer = players.get(owner);

        if (!ownerPlayer.hasSourceLoaded())
            return  AddRemoveExit.code.NO_SOURCE;

        if (!ownerPlayer.getPlayingFile().canBeLiked())
            return AddRemoveExit.code.NOT_A_SONG;

        if (ownerPlaylist == null)
            return AddRemoveExit.code.INVALID_PLAYLIST;

        AudioFile selectedSource = ownerPlayer.getPlayingFile();
        if (ownerPlaylist.hasSong(selectedSource)) {
            ownerPlaylist.removeSong(selectedSource);
            return AddRemoveExit.code.REMOVED;
        } else {
            ownerPlaylist.addSong(selectedSource);
            return AddRemoveExit.code.ADDED;
        }
    }

    public List<Playlist> requestOwnerPlaylists(final String owner) {
        return interrogator.getOwnerPlaylists(owner);
    }

    public LikeExit.code requestLikeAction(LikeInterrogator execQuery) {
        String username = execQuery.getUsername();
        User queriedUser = interrogator.getUserByUsername(username);
        Player queriedUserPlayer = getPlayers().get(username);

        if (!queriedUserPlayer.hasSourceLoaded())
            return LikeExit.code.NO_SOURCE;

        AudioFile queriedSong = queriedUserPlayer.getPlayingFile();

        if (!queriedSong.canBeLiked())
            return LikeExit.code.NOT_A_SONG;

        if (queriedUser.hasLikedSong(queriedSong)) {
            queriedUser.removeSongFromLikes(queriedSong);
            return LikeExit.code.UNLIKED;
        } else {
            queriedUser.addSongToLikes(queriedSong);
            return LikeExit.code.LIKED;
        }
    }

    public List<String> requestLikedSongs(ShowLikesInterrogator execQuery) {
        String username = execQuery.getUsername();

        List<AudioFile> songs = interrogator.getUserLikedSongs(username);

        List<String> names = new ArrayList<>();
        for (AudioFile file : songs)
            names.add(file.getName());

        return names;
    }

    public FollowExit.code requestFollowAction(FollowInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);
        String username = execQuery.getUsername();

        if (userPlayer.hasNoSource())
            return FollowExit.code.NO_SOURCE;

        return userPlayer.getSelectedSource().follow(username);
    }

    public String requestRepeatAction(RepeatInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource())
            return "Please load a source before setting the repeat status.";

        userPlayer.changeRepeatState();
        int newRepeat = userPlayer.getRepeat();
        String stateName = userPlayer.getSelectedSource().getRepeatStateName(newRepeat);
        return "Repeat mode changed to " + stateName.toLowerCase() + ".";
    }

    public ShuffleExit.code requestShuffling(ShuffleInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);
        int seed = execQuery.getSeed();

        if (!userPlayer.hasSourceLoaded() || userPlayer.hasNoSource())
            return ShuffleExit.code.NO_SOURCE_LOADED;

        ShuffleExit.code shuffleExit;
        if (userPlayer.isShuffle()) {
            userPlayer.setShuffle(false);
            shuffleExit = userPlayer.getSelectedSource().unshuffle();
            userPlayer.changeOrderAfterShuffle();
        } else {
            userPlayer.setShuffle(true);
            shuffleExit = userPlayer.getSelectedSource().shuffle(seed);
            userPlayer.changeOrderAfterShuffle();
        }

        return shuffleExit;
    }

    public String requestNext(NextInterrogator execQuery) {
       Player userPlayer = requestPlayer(execQuery);

       if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
           return "Please load a source before skipping to the next track.";

       if (userPlayer.playNext()) {
           String track = userPlayer.getPlayingFile().getName();
           return "Skipped to next track successfully. The current track is " + track + ".";
       }

        return "Please load a source before skipping to the next track.";
    }

    public String requestPrev(PrevInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);

        if (execQuery.getTimestamp() == 6390)
            System.out.println("aici pula");

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before returning to the previous track.";

        userPlayer.playPrev(execQuery.getTimestamp() - lastActionTime);
        return "Returned to previous track successfully. The current track is " +
                userPlayer.getPlayingFile().getName() + ".";
    }

    public String requestForward(ForwardInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before attempting to forward.";

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward())
            return "The loaded source is not a podcast.";

        userPlayer.skipForward();
        return "Skipped forward successfully.";
    }

    public String requestBackward(BackwardInterrogator execQuery) {
        Player userPlayer = requestPlayer(execQuery);

        if (userPlayer.hasNoSource() || !userPlayer.hasSourceLoaded())
            return "Please load a source before rewinding.";

        if (userPlayer.getSelectedSource().cantGoForwardOrBackward())
            return "The loaded source is not a podcast.";

        userPlayer.rewoundBackward();
        return "Rewound successfully.";
    }

    public List<String> requestTopFiveSongs() {
        return interrogator.getTopFiveSongs();
    }

    public List<String> requestTopFivePlaylists() {
        return interrogator.getTopFivePlaylists();
    }

    public void updatePlayersData(CommandObject nextToExecuteCommand) {
        int diff = nextToExecuteCommand.getTimestamp() - lastActionTime;
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            Player currPlayer = entry.getValue();
            currPlayer.updatePlayer(diff);
        }
    }

}
