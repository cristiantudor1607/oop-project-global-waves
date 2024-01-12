package app.pages;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
public class PageHistory {
    /**
     * The pages are stored from the first visited (at index {@code 0}), to
     * the last visited.
     * To get the previous page, {@code removeLast} or {@code getLast} should be called.
     * To add a page to {@code prevPages}, {@code addLast} should be called.
     */
    private final Deque<Page> prevPages;

    /**
     * The pages are stored from the one that will be visited next (at index {@code 0}),
     * to the one that will be visited in the end.
     * To get the next page, {@code removeFirst} or {@code getFirst} should be called.
     * To add a page to {@code nextPages}, {@code addFirst} should be called.
     */
    private final Deque<Page> nextPages;

    private Page currentPage;

    public PageHistory() {
        prevPages = new ArrayDeque<>();
        nextPages = new ArrayDeque<>();
        currentPage = null;
    }

    /**
     * Visits a new page.
     * Sets the {@code currentPage} to the given page.
     *
     * @param page The page to be visited
     */
    public void visitPage(final Page page) {
        currentPage = page;
    }

    /**
     * Visits the previous page if there is one.
     * It sets the {@code currentPage} field to the last page from {@code prevPages}
     * list, and removes it from the list.
     *
     * @return {@code true}, if the previous page was visited successfully, {@code false}
     * otherwise
     */
    public boolean visitPreviousPage() {
        if (prevPages.isEmpty()) {
            return false;
        }

        nextPages.addFirst(currentPage);
        currentPage = prevPages.removeLast();
        return true;
    }

    /**
     * Visits the next page if there is one.
     * It sets the {@code currentPage} field to the first page from {@code nextPages}
     * list, and removes it from the list.
     *
     * @return {@code true}, if the next page was visited successfully, {@code false}
     * otherwise.
     */
    public boolean visitNextPage() {
        if (nextPages.isEmpty()) {
            return false;
        }

        prevPages.addLast(currentPage);
        currentPage = nextPages.removeFirst();
        return true;
    }

    /**
     * Clears the {@code nextPages} list.
     */
    public void resetNextPages() {
        nextPages.clear();
    }
}
