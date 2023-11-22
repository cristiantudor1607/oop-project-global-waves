package globalwaves.player.entities;

import globalwaves.player.entities.properties.PlayableEntity;
import globalwaves.player.entities.properties.OwnedEntity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Playlist implements PlayableEntity, OwnedEntity {
    private String name;
    private String owner;
    private int duration;
    private int followers;
    private boolean visible;
    private List<Song> songs;
    private List<Integer> playOrder;


    public Playlist(String owner, String name) {
        this.name = name;
        this.owner = owner;
        duration = 0;
        followers = 0;
        visible = true;
        songs = new ArrayList<>();
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

    public boolean hasSong(Song searchedSong) {
        String searchedSongName = searchedSong.getName();

        for (Song playlistSong : songs)
           if (playlistSong.getName().equals(searchedSongName))
               return true;

        return false;
    }

    public void addSong(Song songToBeAdded) {
        songs.add(songToBeAdded);
        duration += songToBeAdded.getDuration();
    }

    public void removeSong(Song songToBeRemoved) {
        songs.remove(songToBeRemoved);
        duration -= songToBeRemoved.getDuration();
    }

    public List<Integer> getStandardOrder() {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++)
            order.add(i);

        return order;
    }

    public int getSongIndex(Song queriedSong) {
        int index = songs.indexOf(queriedSong);
        return getPlayOrder().indexOf(index);
    }

    public Song getNextSong(Song currentSong) {
        int currentSongIndex = getSongIndex(currentSong);
        if (currentSongIndex >= playOrder.size())
            return null;

        int nextSongIndex = playOrder.get(currentSongIndex + 1);
        return songs.get(nextSongIndex);
    }

    @Override
    public boolean isEmptyPlayableFile() {
        return songs.isEmpty();
    }

    @Override
    public boolean isPlaylist() {
        return true;
    }

    @Override
    public boolean isSong() {
        return false;
    }
}
