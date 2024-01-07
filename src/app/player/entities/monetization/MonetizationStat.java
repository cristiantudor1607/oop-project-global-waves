package app.player.entities.monetization;

import app.player.entities.Song;
import app.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MonetizationStat {
    private final Double songRevenue;
    private final Double merchRevenue;
    @Setter
    private int ranking;
    private final String mostProfitableSong;

    public MonetizationStat(final User artist) {
        songRevenue = Math.round(artist.getSongRevenue() * 100.0) / 100.0;
        merchRevenue = Math.round(artist.getMerchRevenue() * 100.0) / 100.0;

        ranking = 0;

        Song song = artist.getMostProfitableSong();
        if (song == null) {
            mostProfitableSong = "N/A";
        } else {
            mostProfitableSong = song.getName();
        }
    }
}
