package app.statistics;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class UserStatistics extends StatisticsTemplate {
    private Map<String, Integer> topArtists;
    private Map<String, Integer> topGenres;
    private Map<String, Integer> topSongs;
    private Map<String, Integer> topAlbums;
    private Map<String, Integer> topEpisodes;
}
