package globalwaves.player.entities;

import globalwaves.commands.enums.exitstats.stageone.FollowExit;
import globalwaves.commands.enums.exitstats.stageone.ShuffleExit;
import globalwaves.player.entities.properties.OwnedEntity;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Getter @Setter
public class Playlist implements PlayableEntity, OwnedEntity {
    private String name;
    private String owner;
    private int creationTime;
    private int followersNumber;
    private boolean visible;
    private List<AudioFile> songs;
    private List<Integer> playOrder;
    private List<String> followers;

    public Playlist(String owner, String name, int creationTime) {
        this.name = name;
        this.owner = owner;
        this.creationTime = creationTime;
        followersNumber = 0;
        visible = true;
        songs = new ArrayList<>();
        followers = new ArrayList<>();
        playOrder = getNumericalOrder();
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
        for (AudioFile playlistSong : songs)
            if (playlistSong == searchedSong)
                return true;

        return false;
    }

    public void addSong(AudioFile songToBeAdded) {
        songs.add(songToBeAdded);
        playOrder = getNumericalOrder();
    }

    public void removeSong(AudioFile songToBeRemoved) {
        songs.remove(songToBeRemoved);
        playOrder = getNumericalOrder();
    }

    public List<Integer> getNumericalOrder() {
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

    public AudioFile getPrevSong(AudioFile currentSong) {
        int currentSongIndex = getSongIndex(currentSong);
        if (currentSongIndex == 0)
            return null;

        int prevSongIndex = playOrder.get(currentSongIndex - 1);
        return songs.get(prevSongIndex);
    }

    public boolean isFollowedByUser(String username) {
        for (String name : followers)
            if (name.equals(username))
                return true;

        return false;
    }

    public boolean isOwnedByUser(String username) {
        return owner.equals(username);
    }

    public void addUserToFollowers(String username) {
        followers.add(username);
        followersNumber++;
    }

    public void removeUserFromFollowers(String username) {
        followers.remove(username);
        followersNumber--;
    }

    @Override
    public String getRepeatStateName(int repeatValue) {
        switch (repeatValue) {
            case 0 -> {
                return "No Repeat";
            }
            case 1 -> {
                return "Repeat All";
            }
            case 2 -> {
                return "Repeat Current Song";
            }
        }

        return null;
    }

    @Override
    public AudioFile getNextForPlaying(AudioFile currentFile, int repeatValue) {
       if (repeatValue == 2)
           return currentFile;

       AudioFile next = getNextSong(currentFile);

       if (repeatValue == 1 && next == null)
           return getPlayableFile();

       return next;
    }

    @Override
    public AudioFile getPrevForPlaying(AudioFile currentFile, int repeatValue) {
        if (repeatValue == 2)
            return currentFile;

        AudioFile prev = getPrevSong(currentFile);
        if (repeatValue == 1 && prev == null)
            return songs.get(playOrder.get(playOrder.size() - 1));

        return prev;
    }

    @Override
    public boolean isEmptyPlayableFile() {
        return songs.isEmpty();
    }

    @Override
    public AudioFile getPlayableFile() {
        if (songs.isEmpty())
            return null;

        return songs.get(playOrder.get(0));
    }

    @Override
    public int getDuration() {
        if (songs.isEmpty())
            return -1;

        return songs.get(0).getDuration();
    }


    @Override
    public FollowExit.Status follow(String username) {
        if (isOwnedByUser(username))
            return FollowExit.Status.OWNER;

        if (isFollowedByUser(username)) {
            removeUserFromFollowers(username);
            return FollowExit.Status.UNFOLLOWED;
        }

        addUserToFollowers(username);
        return FollowExit.Status.FOLLOWED;
    }

    @Override
    public ShuffleExit.Status shuffle(int seed) {
        Collections.shuffle(playOrder, new Random(seed));
        return ShuffleExit.Status.ACTIVATED;
    }

    @Override
    public ShuffleExit.Status unshuffle() {
        playOrder = getNumericalOrder();
        return ShuffleExit.Status.DEACTIVATED;
    }

    @Override
    public boolean needsHistoryTrack() {
        return false;
    }

    @Override
    public boolean cantGoForwardOrBackward() {
        return true;
    }
}
