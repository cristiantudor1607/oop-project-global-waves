package app.pages.features;

import app.utilities.DateMapper;

import java.time.LocalDate;

public record Event(String name, String description, LocalDate date) {

    /**
     * Returns the event date in string form.
     *
     * @return A string containing the date in dd-mm-yyyy form
     */
    public String getFormattedDate() {
        return DateMapper.parseDateToString(date);
    }
}
