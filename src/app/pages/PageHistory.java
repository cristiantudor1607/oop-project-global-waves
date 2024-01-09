package app.pages;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

@Getter
public class PageHistory {
    private final Deque<Page> prevPages;
    private final Deque<Page> nextPages;

    public PageHistory() {
        prevPages = new ArrayDeque<>();
        nextPages = new ArrayDeque<>();
    }

    public void visitPage(final Page page) {
        prevPages.push(page);
    }

    /**
     * Adds the previous page to the {@code nextPages} list, removes it
     * from {@code prevPage} list and returns it.
     *
     * @return An {@code Optional} containing the page, if there are previous
     * pages left, or an empty {@code Optional} otherwise
     */
    public Optional<Page> getPreviousPage() {
        if (prevPages.isEmpty()) {
            return Optional.empty();
        }

        Page prevPage = prevPages.pop();
        nextPages.push(prevPage);
        return Optional.of(prevPage);
    }

    /**
     * Adds the next page to the {@code prevPages} list, removes it from
     * {@code nextPages} list and returns it.
     *
     * @return An {@code Optional} containing the page, if there are next pages
     * left, or an empty {@code Optional} otherwise
     */
    public Optional<Page> getNextPage() {
        if (nextPages.isEmpty()) {
            return Optional.empty();
        }

        Page nextPage = nextPages.pop();
        prevPages.push(nextPage);
        return Optional.of(nextPage);
    }

    /**
     * Clears the {@code nextPages} list.
     */
    public void resetNextPages() {
        nextPages.clear();
    }
}
