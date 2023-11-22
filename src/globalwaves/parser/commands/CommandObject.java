package globalwaves.parser.commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import globalwaves.commands.*;
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
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "repeat"),
        @JsonSubTypes.Type(value = ShuffleCommandObject.class, name = "shuffle"),
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "forward"),
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "backward"),
        @JsonSubTypes.Type(value = LikeInterrogator.class, name = "like"),
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "next"),
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "prev"),
        @JsonSubTypes.Type(value = AddRemoveInterrogator.class, name = "addRemoveInPlaylist"),
        @JsonSubTypes.Type(value = StatusInterrogator.class, name = "status"),
        @JsonSubTypes.Type(value = CreatePlaylistInterrogator.class, name = "createPlaylist"),
        @JsonSubTypes.Type(value = SwitchVisibilityInterrogator.class, name = "switchVisibility"),
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "follow"),
        @JsonSubTypes.Type(value = ShowPlaylistsInterrogator.class, name = "showPlaylists"),
        @JsonSubTypes.Type(value = ShowLikesInterrogator.class, name = "showPreferredSongs"),
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "getTop5Songs"),
        @JsonSubTypes.Type(value = NoArgsCommandObject.class, name = "getTop5Playlists"),

})
@Getter @Setter
public abstract class CommandObject {
    protected String username;
    protected Integer timestamp;

    public JsonNode execute(ActionManager manager) {
        System.out.println("Not implemented yet");
        return null;
    }

    public boolean hasFilters() {
        return false;
    }

    public Map<String, List<String>> getFilters() {
        return null;
    }

    public void setFilters(Map<String, List<String>> mappedFilters) {}

}
