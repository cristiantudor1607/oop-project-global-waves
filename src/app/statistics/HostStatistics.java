package app.statistics;

import lombok.Getter;

import java.util.Map;

@Getter
public class HostStatistics extends StatisticsTemplate {
    private Map<String, Integer> topEpisodes;
    private int listeners;
}
