package globalwaves.player.entities.paging;

import globalwaves.player.entities.properties.ContentVisitor;
import globalwaves.player.entities.properties.Visitable;

public abstract class Page implements Visitable {
    @Override
    public abstract void accept(ContentVisitor v);
}
