package app.search.engines;

import app.pages.Page;
import app.properties.PlayableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public final class EngineResultsParser {
    private static final int INIT = 5;
    private static int truncSize = INIT;

    private EngineResultsParser() { }

    /**
     * Truncates the given list of objects to the first {@code truncSize} elements.
     * {@code truncSize} is initially set to 5, but it can be adjusted through getter and setters.
     * @param allResults The list of results to be truncated
     * @return The list of results truncated to {@code truncSize}
     * @param <T> The type of objects of the list. It can be applied on any type
     */
    public static <T> List<T>
    getRelevantResults(final List<T> allResults) {
        if (allResults.size() <= truncSize) {
            return allResults;
        }

        return allResults.subList(0, truncSize);
    }

    /**
     * Retrieves the names of the given entities and stores them in a list.
     * @param namedEntities A list of entities that have a name
     * @return A list of strings, containing the names of the entities
     */
    public static List<String>
    getNamesFromList(final List<? extends PlayableEntity> namedEntities) {
        List<String> names = new ArrayList<>();

        for (PlayableEntity entity : namedEntities) {
            names.add(entity.getName());
        }

        return names;
    }

    /**
     * Retrieves all the usernames for the given pages, and stores them into a
     * list. It should be called on a list that contains only {@code ArtistPage} or
     * {@code HostPage}. It skips the {@code null} returned by the {@code getUsername} method,
     * for other pages.
     *
     * @param pages The list of pages
     * @return A list containing the names of the artists or hosts.
     */
    public static List<String> getUsernamesFromPages(final List<Page> pages) {
        List<String> usernames = new ArrayList<>();

        for (Page page: pages) {
            if (page.getUsername() == null) {
                continue;
            }

            usernames.add(page.getUsername());
        }

        return usernames;
    }

}
