package globalwaves.player.entities;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Getter
public class Event {
    private final String description;
    private final LocalDate date;

    public Event(final String description, final LocalDate date) {
        this.description = description;
        this.date = date;
    }
}
