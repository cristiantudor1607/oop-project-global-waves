package globalwaves.player.entities;

import globalwaves.commands.enums.UserType;
import globalwaves.player.entities.library.Library;
import lombok.Getter;
import lombok.NonNull;

public class Admin extends User {
    @Getter
    protected Library database;

    public Admin() {
        database = Library.getInstance();
    }

    public User createUser(final String username, final int age, final String city,
                           final UserType.Type type) {
        return switch (type) {
            case USER -> new User(username, age, city);
            case ARTIST -> new Artist(username, age, city);
            case HOST -> new Host(username, age, city);
            default -> null;
        };
    }

    public boolean addUser(@NonNull User newUser) {
        if (newUser.isNormalUser())
            return database.addUser(newUser);

        if (newUser.isArtist())
            return database.addArtist(newUser);

        if (newUser.isHost())
            return database.addHost(newUser);

        return false;
    }

    @Override
    public boolean isNormalUser() {
        return false;
    }

}
