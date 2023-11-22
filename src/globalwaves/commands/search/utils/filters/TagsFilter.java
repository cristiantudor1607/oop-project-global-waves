package globalwaves.commands.search.utils.filters;

import globalwaves.player.entities.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TagsFilter implements Filter<Song> {
    private List<String> tags;

    public TagsFilter(final List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean matches(Song MatchingObject) {
        List<String> songTags = MatchingObject.getTags();

        int foundTags = 0;
        for (String tag: songTags) {
            if (tags.contains(tag))
                foundTags++;
        }

        return foundTags == tags.size();
    }
}
