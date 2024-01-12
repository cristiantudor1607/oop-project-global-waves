package app.pages;

import app.pages.features.Announcement;
import app.pages.features.Event;
import app.pages.features.Merch;
import app.player.entities.Album;
import app.player.entities.Episode;
import app.player.entities.Playlist;
import app.player.entities.Podcast;
import app.player.entities.Song;
import app.properties.Visitor;

import java.util.List;

public class ContentVisitor implements Visitor {
    /**
     * Visits a {@code HomePage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    @Override
    public String visit(final HomePage page) {
        List<Song> likedSongs = page.getLikedSongs();
        List<Playlist> followingPlaylists = page.getFollowingPlaylists();

        List<Song> recommendedSongs = page.getSongRecommendations();
        List<Playlist> recommendedPlaylists = page.getPlaylistRecommendation();

        String liked = "Liked songs:\n\t%s"
                .formatted(likedSongs.stream()
                        .map(Song::getName)
                        .toList());

        String following = "Followed playlists:\n\t%s"
                .formatted(followingPlaylists.stream()
                        .map(Playlist::getName)
                        .toList());

        String songRecoms = "Song recommendations:\n\t%s"
                .formatted(recommendedSongs.stream()
                        .map(Song::getName)
                        .toList());

        String playlistRecoms = "Playlists recommendations:\n\t%s"
                .formatted(recommendedPlaylists.stream()
                        .map(Playlist::getName)
                        .toList());

        return liked + "\n\n" + following + "\n\n" + songRecoms + "\n\n" + playlistRecoms;
    }

    /**
     * Visits an {@code ArtistPage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    @Override
    public String visit(final ArtistPage page) {
        List<Event> events = page.getEvents();
        List<Merch> merchandises = page.getMerchandising();
        List<Album> albums = page.getAlbums();

        StringBuilder albumNames = new StringBuilder();
        for (int i = 0; i < albums.size(); i++) {
            if (i != 0) {
                albumNames.append(", ");
            }

            albumNames.append(albums.get(i).getName());
        }

        StringBuilder eventNames = new StringBuilder();
        for (int i = 0; i < events.size(); i++) {
            if (i != 0) {
                eventNames.append(", ");
            }

            eventNames.append(events.get(i).name()).append(" - ");
            eventNames.append(events.get(i).getFormattedDate()).append(":\n\t");
            eventNames.append(events.get(i).description());
        }

        StringBuilder merchNames = new StringBuilder();
        for (int i = 0; i < merchandises.size(); i++) {
            if (i != 0) {
                merchNames.append(", ");
            }

            merchNames.append(merchandises.get(i).name()).append(" - ");
            merchNames.append(merchandises.get(i).price()).append(":\n\t");
            merchNames.append(merchandises.get(i).description());
        }

        return "Albums:\n\t[" + albumNames + "]\n\nMerch:\n\t[" + merchNames
                + "]\n\nEvents:\n\t[" + eventNames + "]";
    }

    /**
     * Visits a {@code HostPage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    @Override
    public String visit(final HostPage page) {
        List<Podcast> podcasts = page.getHost().getPodcasts();
        List<Announcement> announcements = page.getAnnouncements();

        StringBuilder podcastFormat = new StringBuilder();
        for (int i = 0; i < podcasts.size(); i++) {
            if (i != 0) {
                podcastFormat.append(", ");
            }

            podcastFormat.append(podcasts.get(i).getName()).append(":\n\t[");

            List<Episode> episodes = podcasts.get(i).getEpisodes();
            for (int j = 0; j < episodes.size(); j++) {
                if (j != 0) {
                    podcastFormat.append(", ");
                }

                podcastFormat.append(episodes.get(j).getName())
                        .append(" - ")
                        .append(episodes.get(j).getDescription());
            }

            podcastFormat.append("]\n");
        }

        StringBuilder announceFormat = new StringBuilder();
        for (int i = 0; i < announcements.size(); i++) {
            if (i != 0) {
                announceFormat.append(", ");
            }

            announceFormat.append(announcements.get(i).name())
                    .append(":")
                    .append("\n\t")
                    .append(announcements.get(i).description());
        }

        return "Podcasts:\n\t[" + podcastFormat + "]\n\nAnnouncements:\n\t["
                + announceFormat + "]";
    }

    /**
     * Visits a {@code LikedContentPage} and returns its content.
     * @param page The page to be visited
     * @return A string containing the page content
     */
    @Override
    public String visit(final LikedContentPage page) {
        List<Song> songs = page.getLikedSongs();
        List<Playlist> playlists = page.getFollowingPlaylists();

        StringBuilder songsFormat = new StringBuilder();
        for (int i = 0; i < songs.size(); i++) {
            if (i != 0) {
                songsFormat.append(", ");
            }

            songsFormat.append(songs.get(i).getName())
                    .append(" - ")
                    .append(songs.get(i).getArtistName());
        }

        StringBuilder playlistsFormat = new StringBuilder();
        for (int i = 0; i < playlists.size(); i++) {
            if (i != 0) {
                playlistsFormat.append(", ");
            }

            playlistsFormat.append(playlists.get(i).getName())
                    .append(" - ")
                    .append(playlists.get(i).getOwner());
        }

        return "Liked songs:\n\t[" + songsFormat + "]\n\nFollowed playlists:\n\t["
                + playlistsFormat + "]";
    }
}
