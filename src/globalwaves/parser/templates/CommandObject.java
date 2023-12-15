package globalwaves.parser.templates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.stageone.*;
import globalwaves.commands.stagetwo.*;
import globalwaves.player.entities.library.ActionManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "command")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SearchInterrogator.class, name = "search"),
        @JsonSubTypes.Type(value = SelectInterrogator.class, name = "select"),
        @JsonSubTypes.Type(value = LoadInterrogator.class, name = "load"),
        @JsonSubTypes.Type(value = PlayPauseInterrogator.class, name = "playPause"),
        @JsonSubTypes.Type(value = RepeatInterrogator.class, name = "repeat"),
        @JsonSubTypes.Type(value = ShuffleInterrogator.class, name = "shuffle"),
        @JsonSubTypes.Type(value = ForwardInterrogator.class, name = "forward"),
        @JsonSubTypes.Type(value = BackwardInterrogator.class, name = "backward"),
        @JsonSubTypes.Type(value = LikeInterrogator.class, name = "like"),
        @JsonSubTypes.Type(value = NextInterrogator.class, name = "next"),
        @JsonSubTypes.Type(value = PrevInterrogator.class, name = "prev"),
        @JsonSubTypes.Type(value = AddRemoveInterrogator.class, name = "addRemoveInPlaylist"),
        @JsonSubTypes.Type(value = StatusInterrogator.class, name = "status"),
        @JsonSubTypes.Type(value = CreatePlaylistInterrogator.class, name = "createPlaylist"),
        @JsonSubTypes.Type(value = VisibilityInterrogator.class, name = "switchVisibility"),
        @JsonSubTypes.Type(value = FollowInterrogator.class, name = "follow"),
        @JsonSubTypes.Type(value = ShowPlaylistsInterrogator.class, name = "showPlaylists"),
        @JsonSubTypes.Type(value = ShowLikesInterrogator.class, name = "showPreferredSongs"),
        @JsonSubTypes.Type(value = TopFiveSongsInterrogator.class, name = "getTop5Songs"),
        @JsonSubTypes.Type(value = TopFivePlaylistsInterrogator.class, name = "getTop5Playlists"),

        @JsonSubTypes.Type(value = ChangePageInterrogator.class, name = "changePage"),
        @JsonSubTypes.Type(value = PrintPageInterrogator.class, name = "printCurrentPage"),
        @JsonSubTypes.Type(value = AddUserInterrogator.class, name = "addUser"),
        @JsonSubTypes.Type(value = DeleteUserInterrogator.class, name = "deleteUser"),
        @JsonSubTypes.Type(value = ShowAlbumsInterrogator.class, name = "showAlbums"),
        @JsonSubTypes.Type(value = ShowPodcastsInterrogator.class, name = "showPodcasts"),
        @JsonSubTypes.Type(value = AddAlbumInterrogator.class, name = "addAlbum"),
        @JsonSubTypes.Type(value = RemoveAlbumInterrogator.class, name = "removeAlbum"),
        @JsonSubTypes.Type(value = AddEventInterrogator.class, name = "addEvent"),
        @JsonSubTypes.Type(value = RemoveEventInterrogator.class, name = "removeEvent"),
        @JsonSubTypes.Type(value = AddMerchInterrogator.class, name = "addMerch"),
        @JsonSubTypes.Type(value = AddPodcastInterrogator.class, name = "addPodcast"),
        @JsonSubTypes.Type(value = RemovePodcastInterrogator.class, name = "removePodcast"),
        @JsonSubTypes.Type(value = AddAnnouncementInterrogator.class, name = "addAnnouncement"),
        @JsonSubTypes.Type(value = RemoveAnnouncementInterrogator.class, name = "removeAnnouncement"),
        @JsonSubTypes.Type(value = ConnectionInterrogator.class, name = "switchConnectionStatus"),
        @JsonSubTypes.Type(value = TopFiveAlbumsInterrogator.class, name = "getTop5Albums"),
        @JsonSubTypes.Type(value = TopFiveArtistsInterrogator.class, name = "getTop5Artists"),
        @JsonSubTypes.Type(value = AllUsersInterrogator.class, name = "getAllUsers"),
        @JsonSubTypes.Type(value = OnlineUsersInterrogator.class, name = "getOnlineUsers"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public abstract class CommandObject {
    protected String username;
    protected Integer timestamp;
    @JsonIgnore
    protected ActionManager manager;
    @JsonIgnore
    protected boolean approval;

    public CommandObject() {
        manager = ActionManager.getInstance();
    }


    public abstract void execute();

    public abstract JsonNode formatOutput();

    /**
     * Method that checks if the executing command has to work with filters.
     * @return true, if it has to work with filters, false otherwise
     */
    public boolean hasFiltersField() {
        return false;
    }

    /**
     * Method used to set filters field, if the command requires them. Otherwise, it does nothing.
     * @param mappedFilters The filters parsed as a Map
     */
    public void setFiltersField(final Map<String, List<String>> mappedFilters) { }

}
