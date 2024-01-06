package app.parser.commands.templates;

import app.commands.stageone.AddRemoveInterrogator;
import app.commands.stageone.BackwardInterrogator;
import app.commands.stageone.CreatePlaylistInterrogator;
import app.commands.stageone.FollowInterrogator;
import app.commands.stageone.ForwardInterrogator;
import app.commands.stageone.LikeInterrogator;
import app.commands.stageone.LoadInterrogator;
import app.commands.stageone.NextInterrogator;
import app.commands.stageone.PlayPauseInterrogator;
import app.commands.stageone.PrevInterrogator;
import app.commands.stageone.RepeatInterrogator;
import app.commands.stageone.SearchInterrogator;
import app.commands.stageone.SelectInterrogator;
import app.commands.stageone.ShowLikesInterrogator;
import app.commands.stageone.ShowPlaylistsInterrogator;
import app.commands.stageone.ShuffleInterrogator;
import app.commands.stageone.StatusInterrogator;
import app.commands.stageone.TopFivePlaylistsInterrogator;
import app.commands.stageone.TopFiveSongsInterrogator;
import app.commands.stageone.VisibilityInterrogator;
import app.commands.stagethree.*;
import app.commands.stagetwo.AddAlbumInterrogator;
import app.commands.stagetwo.AddAnnouncementInterrogator;
import app.commands.stagetwo.AddEventInterrogator;
import app.commands.stagetwo.AddMerchInterrogator;
import app.commands.stagetwo.AddPodcastInterrogator;
import app.commands.stagetwo.AddUserInterrogator;
import app.commands.stagetwo.AllUsersInterrogator;
import app.commands.stagetwo.ChangePageInterrogator;
import app.commands.stagetwo.ConnectionInterrogator;
import app.commands.stagetwo.DeleteUserInterrogator;
import app.commands.stagetwo.OnlineUsersInterrogator;
import app.commands.stagetwo.PrintPageInterrogator;
import app.commands.stagetwo.RemoveAlbumInterrogator;
import app.commands.stagetwo.RemoveAnnouncementInterrogator;
import app.commands.stagetwo.RemoveEventInterrogator;
import app.commands.stagetwo.RemovePodcastInterrogator;
import app.commands.stagetwo.ShowAlbumsInterrogator;
import app.commands.stagetwo.ShowPodcastsInterrogator;
import app.commands.stagetwo.TopFiveAlbumsInterrogator;
import app.commands.stagetwo.TopFiveArtistsInterrogator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import app.management.ActionManager;
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
        @JsonSubTypes.Type(value = RemoveAnnouncementInterrogator.class,
                name = "removeAnnouncement"),
        @JsonSubTypes.Type(value = ConnectionInterrogator.class, name = "switchConnectionStatus"),
        @JsonSubTypes.Type(value = TopFiveAlbumsInterrogator.class, name = "getTop5Albums"),
        @JsonSubTypes.Type(value = TopFiveArtistsInterrogator.class, name = "getTop5Artists"),
        @JsonSubTypes.Type(value = AllUsersInterrogator.class, name = "getAllUsers"),
        @JsonSubTypes.Type(value = OnlineUsersInterrogator.class, name = "getOnlineUsers"),

        @JsonSubTypes.Type(value = WrappedInterrogator.class, name = "wrapped"),
        @JsonSubTypes.Type(value = BuyMerchInterrogator.class, name = "buyMerch"),
        @JsonSubTypes.Type(value = SeeMerchInterrogator.class, name = "seeMerch"),
        @JsonSubTypes.Type(value = BuyPremiumInterrogator.class, name = "buyPremium"),
        @JsonSubTypes.Type(value = CancelPremiumInterrogator.class, name = "cancelPremium"),
        @JsonSubTypes.Type(value = EmptyCommand.class, name = "adBreak"),
        @JsonSubTypes.Type(value = EmptyCommand.class, name = "subscribe"),
        @JsonSubTypes.Type(value = EmptyCommand.class, name = "getNotifications"),
        @JsonSubTypes.Type(value = EmptyCommand.class, name = "updateRecommendations"),
        @JsonSubTypes.Type(value = EmptyCommand.class, name = "loadRecommendations"),
        @JsonSubTypes.Type(value = EmptyCommand.class, name = "previousPage"),
        @JsonSubTypes.Type(value = EmptyCommand.class, name = "nextPage"),
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

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * After calling {@code execute} method, the output of the command can be
     * generated using this method.
     * @return A JsonNode containing the output data
     */
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
