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
    private List<Song> songs;
    @Setter
    private List<Integer> playOrder;
    private List<User> followers;

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

    public int getTotalLikesNumber() {
        int sum = 0;
        for (Song s: songs)
            sum += s.getLikesNumber();

        return sum;
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

    public boolean hasSong(Song song) {
        for (Song s: songs) {
            if (s == song)
                return true;
        }

        return false;
    }

    public void addSong(Song newSong) {
        songs.add(newSong);
        playOrder = getNumericalOrder();
    }

    public void removeSong(Song alreadyInListSong) {
        songs.remove(alreadyInListSong);
        playOrder = getNumericalOrder();
    }


    public List<Integer> getNumericalOrder() {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++)
            order.add(i);

        return order;
    }

    public int getSongIndex(AudioFile queriedSong) {
        int index = songs.indexOf((Song)queriedSong);
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

    public boolean isFollowedByUser(User user) {
        for (User u: followers) {
            if (u.getUsername().equals(user.getUsername()))
                return true;
        }

        return false;
    }

    public boolean isOwnedByUser(User user) {
        return owner.equals(user.getUsername());
    }

    public void getFollowedBy(User user) {
        followers.add(user);
        followersNumber++;
    }

    public void getUnfollowedBy(User user) {
        followers.remove(user);
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
    public Playlist getWorkingOnPlaylist() {
        return this;
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
