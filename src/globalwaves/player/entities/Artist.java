package globalwaves.player.entities;

public class Artist extends User {
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
    }

    @Override
    public boolean isArtist() {
        return true;
    }

    @Override
    public boolean isNormalUser() {
        return false;
    }
}
