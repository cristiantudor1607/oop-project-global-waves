package app.exitstats.stagethree;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscribeExit {
    public enum Status {
        DOESNT_EXIST,
        NOT_ON_PAGE,
        SUBSCRIBED,
        UNSUBSCRIBED,
    }

    private Status status;
    private String publicName;
    private String username;
}
