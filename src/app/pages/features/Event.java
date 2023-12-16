package app.pages.features;

import app.utilities.DateMapper;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Event {
    private final String name;
    private final String description;
    private final LocalDate date;

    public Event(final String name, final String description, final LocalDate date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getFormattedDate() {
        return DateMapper.parseDateToString(date);
    }
}
