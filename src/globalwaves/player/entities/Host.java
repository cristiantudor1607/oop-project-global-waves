package globalwaves.player.entities;

public class Host extends User {
    public Host(String username, int age, String city) {
        super(username, age, city);
    }

    @Override
    public boolean isNormalUser() {
        return false;
    }

    @Override
    public boolean isHost() {
        return true;
    }
}
