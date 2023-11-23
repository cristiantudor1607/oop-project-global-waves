package globalwaves.player.entities;

import fileio.input.UserInput;
import globalwaves.player.entities.properties.PlayableEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    private String username;
    private int age;
    private String city;
    private List<AudioFile> likes;

    public User(UserInput input) {
        username = input.getUsername();
        age = input.getAge();
        city = input.getCity();
        likes = new ArrayList<>();
    }

    public boolean hasLikedSong(AudioFile file) {
        return likes.contains(file);
    }

    public void addSongToLikes(AudioFile file) {
        likes.add(file);
    }

    public void removeSongFromLikes(AudioFile file) {
        likes.remove(file);
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
