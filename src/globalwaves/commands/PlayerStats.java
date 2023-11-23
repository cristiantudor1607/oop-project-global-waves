package globalwaves.commands;

import globalwaves.player.entities.Player;
import lombok.Getter;

@Getter
public class PlayerStats {
    private final String name;
    private final int remainedTime;
    private final String repeat;
    private final boolean shuffle;
    private final boolean paused;

    public PlayerStats(Player userPlayer) {
        if (userPlayer.getSelectedSource() == null || userPlayer.getPlayingFile() == null) {
            name = "";
            remainedTime = 0;
            repeat = "No Repeat";
            shuffle = false;
            paused = true;
        } else {
            name = userPlayer.getPlayingFile().getName();
            remainedTime = userPlayer.getRemainedTime();
            shuffle = userPlayer.isShuffle();
            paused = userPlayer.getState() != Player.PlayerStatus.PLAYING;

            int repeatValue = userPlayer.getRepeat();
            repeat = userPlayer.getSelectedSource().getRepeatStateName(repeatValue);
        }
    }
}
