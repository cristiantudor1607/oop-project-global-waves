package input;

import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class LibraryInput {
    private ArrayList<fileio.input.SongInput> songs;
    private ArrayList<fileio.input.PodcastInput> podcasts;
    private ArrayList<fileio.input.UserInput> users;

    public LibraryInput() {
    }

    public ArrayList<fileio.input.SongInput> getSongs() {
        return songs;
    }

    public void setSongs(final ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public ArrayList<fileio.input.PodcastInput> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(final ArrayList<PodcastInput> podcasts) {
        this.podcasts = podcasts;
    }

    public ArrayList<fileio.input.UserInput> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<UserInput> users) {
        this.users = users;
    }
}
