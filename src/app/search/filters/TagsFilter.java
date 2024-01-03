package app.search.filters;

import app.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TagsFilter implements Filter<Song> {
    private List<String> tags;

    public TagsFilter(final List<String> tags) {
        this.tags = tags;
    }

    /**
     * Checks if the song contains all the tags from {@code tags} field.
     * @param matchingObject The song to be matched
     * @return true, if the song contains all this tags, false otherwise
     */
    @Override
    public boolean matches(final Song matchingObject) {
        List<String> songTags = matchingObject.getTags();

        int foundTags = 0;
        for (String tag: songTags) {
            if (tags.contains(tag)) {
                foundTags++;
            }
        }

        return foundTags == tags.size();
    }
}
