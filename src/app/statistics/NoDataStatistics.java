package app.statistics;

import lombok.Getter;

@Getter
public class NoDataStatistics extends StatisticsTemplate {
    private final String message;

    public NoDataStatistics(final String message) {
        this.message = message;
    }

    /**
     * If there are no statistics, it returns the user-specific "No data to show" message.
     * @return {@code null}, if the user has statistics, the "No data to show" message,
     * otherwise
     */
    @Override
    public String getSpecificMessage() {
        return message;
    }
}
