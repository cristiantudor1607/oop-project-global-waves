package app.pages.recommendations;

import app.properties.PlayableEntity;

public abstract class Recommender {
    /**
     * Returns the recommendation found. It can be either a song or a playlist.
     *
     * @return The song or playlist recommended, it the recommendation can be found,
     * {@code null otherwise}
     */
    public abstract PlayableEntity getRecommendation();
}
