package globalwaves.player.entities;

import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    private String username;
    private int age;
    private String city;
    private List<Song> likes;

    public User(UserInput input) {
        username = input.getUsername();
        age = input.getAge();
        city = input.getCity();
        likes = new ArrayList<>();
    }

    public boolean hasLikedSong(Song likedSong) {
        return likes.contains(likedSong);
    }

    public void addSongToLikes(Song newLikedSong) {
        likes.add(newLikedSong);
    }

    public void removeSongFromLikes(Song likedSong) {
        likes.remove(likedSong);
    }

    @Override
    public String toString() {
        return "User{" +
                "\nusername='" + username + '\'' +
                "\nage=" + age +
                "\ncity='" + city + '\'' +
                '}';
    }
}
