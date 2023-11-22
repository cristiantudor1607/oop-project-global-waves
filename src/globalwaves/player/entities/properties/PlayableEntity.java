package globalwaves.player.entities.properties;

public interface PlayableEntity {
    String getName();
    int getDuration();
    boolean isEmptyPlayableFile();
    boolean isPlaylist();
    boolean isSong();
}
