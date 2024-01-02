package app.users;

import app.enums.UserType;
import app.player.entities.Album;
import app.player.entities.Podcast;
import app.management.Library;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Admin extends User {
    protected Library database;

    public Admin() {
        database = Library.getInstance();
    }

    /**
     * Creates a new user.
     * @param username The username of new user. It doesn't check if a user with the same
     *                 username already exists.
     * @param age The age of new user.
     * @param city The city of new user.
     * @param type The type of new user. It can be:
     *             <ul>
     *             <li>user</li>
     *             <li>artist</li>
     *             <li>host</li>
     *             </ul>
     * @return The user with the specified fields
     */
    public User createUser(final String username, final int age, final String city,
                           final UserType.Type type) {
        return switch (type) {
            case USER -> new User(username, age, city);
            case ARTIST -> new Artist(username, age, city);
            case HOST -> new Host(username, age, city);
            default -> null;
        };
    }

    /**
     * Adds the user to the database. It has to be a newly created user.
     * @param newUser The user to be added.
     * @return {@code true}
     */
    public boolean addUser(@NonNull final User newUser) {
        if (newUser.isNormalUser()) {
            return database.addUser(newUser);
        }

        if (newUser.isArtist()) {
            return database.addArtist(newUser);
        }

        if (newUser.isHost()) {
            return database.addHost(newUser);
        }

        return false;
    }

    /**
     * Removes a user from database. It has to be in the database.
     * @param oldUser The user to be removed
     */
    public void removeUser(@NonNull final User oldUser) {
        database.deleteUser(oldUser);
    }

    /**
     * Removes the album from library. It also removes other users connections with the album.
     * For example, it unlikes the song for every user.
     * @param oldAlbum The album to be removed. It doesn't check if the album already exists.
     */
    public void removeAlbum(@NonNull final Album oldAlbum) {
        String albumName = oldAlbum.getName();
        String artistName = oldAlbum.getArtist();

        database.removeSongsFromAlbum(artistName, albumName);
        database.removeSongsFromLiked(albumName);
    }

    /**
     * Removes the podcast from library.
     * @param oldPodcast The podcast to be removed. It has to exist in the library
     */
    public void removePodcast(@NonNull final Podcast oldPodcast) {
        String hostName = oldPodcast.getOwner();

        database.getAddedPodcasts().get(hostName).remove(oldPodcast);
    }

    /**
     * Checks if the user is a normal user. <b>Admins aren't normal users.</b>
     * @return {@code true}, if the user is a normal user, {@code false}, otherwise
     */
    @Override
    public boolean isNormalUser() {
        return false;
    }

}
