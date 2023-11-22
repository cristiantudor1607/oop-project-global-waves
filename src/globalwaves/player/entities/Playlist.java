package globalwaves.player.entities;

import globalwaves.player.entities.properties.OwnedEntity;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Playlist implements PlayableEntity, OwnedEntity {
    private String name;
    private String owner;
    private int followers;
    private boolean visible;
    private List<AudioFile> songs;
    private List<Integer> playOrder;

    public Playlist(String owner, String name) {
        this.name = name;
        this.owner = owner;
        followers = 0;
        visible = true;
        songs = new ArrayList<>();
        playOrder = getStandardOrder();
    }

    public boolean isPublic() {
        return visible;
    }

    public void makePrivate() {
        visible = false;
    }

    public void makePublic() {
        visible = true;
    }

    public boolean hasSong(AudioFile searchedSong) {
        String searchedSongName = searchedSong.getName();

        for (AudioFile playlistSong : songs)
           if (playlistSong.getName().equals(searchedSongName))
               return true;

        return false;
    }

    public void addSong(AudioFile songToBeAdded) {
        songs.add(songToBeAdded);
        playOrder = getStandardOrder();
    }

    public void removeSong(AudioFile songToBeRemoved) {
        songs.remove(songToBeRemoved);
        playOrder = getStandardOrder();
    }

    public List<Integer> getStandardOrder() {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++)
            order.add(i);

        return order;
    }

    public int getSongIndex(AudioFile queriedSong) {
        int index = songs.indexOf(queriedSong);
        return getPlayOrder().indexOf(index);
    }

    public AudioFile getNextSong(AudioFile currentSong) {
        int currentSongIndex = getSongIndex(currentSong);
        if (currentSongIndex + 1 >= playOrder.size())
            return null;

        int nextSongIndex = playOrder.get(currentSongIndex + 1);
        return songs.get(nextSongIndex);
    }

    @Override
    public boolean isEmptyPlayableFile() {
        return songs.isEmpty();
    }

    @Override
    public AudioFile getPlayableFile() {
        if (songs.isEmpty())
            return null;

        return songs.get(0);
    }

    @Override
    public int getDuration() {
        if (songs.isEmpty())
            return -1;

        return songs.get(0).getDuration();
    }

    @Override
    public boolean hasNextForPlaying(AudioFile currentFile) {
        return getNextSong(currentFile) != null;
    }

    @Override
    public AudioFile getNextForPlaying(AudioFile currentFile) {
        return getNextSong(currentFile);
    }

    @Override
    public boolean needsHistoryTrack() {
        return false;
    }
}
