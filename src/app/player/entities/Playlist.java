package app.player.entities;

import app.exitstats.stageone.ShuffleExit;
import app.properties.OwnedEntity;
import app.properties.PlayableEntity;
import app.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter @Setter
public class Playlist implements PlayableEntity, OwnedEntity {
    private final String name;
    private final String owner;
    private final int creationTime;
    private int followersNumber;
    private boolean visible;
    private List<Song> songs;
    private List<User> followers;


    public Playlist(String owner, String name, int creationTime) {
        this.name = name;
        this.owner = owner;
        this.creationTime = creationTime;
        followersNumber = 0;
        visible = true;
        songs = new ArrayList<>();
        followers = new ArrayList<>();
    }

    @Override
    public int getIndexOfFile(AudioFile file) {
        return songs.indexOf((Song) file);
    }

    @Override
    public AudioFile getAudioFileAtIndex(int index) {
        if (index >= songs.size())
            return null;

        return songs.get(index);
    }

    @Override
    public int getSize() {
        return songs.size();
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
    }

    public void removeSong(Song alreadyInListSong) {
        songs.remove(alreadyInListSong);
    }


    public List<Integer> getNumericalOrder() {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++)
            order.add(i);

        return order;
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

    public boolean hasSongFromAlbum(final String albumName) {
        for (Song song: songs)
            if (song.getAlbum().equals(albumName))
                return true;

        return false;
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
    public boolean isEmptyPlayableFile() {
        return songs.isEmpty();
    }

    @Override
    public AudioFile getFirstAudioFile() {
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
    public Playlist getCurrentPlaylist() {
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

    @Override
    public String getPublicPerson() {
        return owner;
    }

    @Override
    public boolean hasAudiofileFromUser(String username) {
        for (Song s : songs) {
            if (s.hasAudiofileFromUser(username))
                return true;
        }

        return false;
    }

    @Override
    public Album getCurrentAlbum() {
        return null;
    }

    @Override
    public boolean isPlaylist() {
        return true;
    }
}
