package globalwaves.player.entities.properties;

import globalwaves.player.entities.*;
import globalwaves.player.entities.paging.ArtistPage;
import globalwaves.player.entities.paging.HomePage;

import java.util.List;

public class ContentVisitor implements Visitor {
    @Override
    public String visit(HomePage page) {
        List<Song> recommendedSongs = page.getRecommendedSongs();
        List<Playlist> recommendedPlaylists = page.getRecommendedPlaylist();

        StringBuilder songNames = new StringBuilder();
        for (int i = 0; i < recommendedSongs.size(); i++) {
            if (i != 0)
                songNames.append(", ");

            songNames.append(recommendedSongs.get(i).getName());
        }

        StringBuilder playlistNames = new StringBuilder();
        for (int i = 0; i < recommendedPlaylists.size(); i++) {
            if (i != 0)
                playlistNames.append(", ");

            playlistNames.append(recommendedPlaylists.get(i).getName());
        }

        return "Liked songs:\n\t[" + songNames + "]\n\nFollowed playlists:\n\t["
                + playlistNames + "]";
    }

    @Override
    public String visit(ArtistPage page) {
        List<Event> events = page.getEvents();
        List<Merch> merchandises = page.getMerchandising();
        List<Album> albums = page.getAlbums();

        StringBuilder albumNames = new StringBuilder();
        for (int i = 0; i < albums.size(); i++) {
            if (i != 0)
                albumNames.append(", ");

            albumNames.append(albums.get(i).getName());
        }

        StringBuilder eventNames = new StringBuilder();
        for (int i = 0; i < events.size(); i++) {
            if (i != 0)
                eventNames.append(", ");

            eventNames.append(events.get(i).getName()).append(" - ");
            eventNames.append(events.get(i).getFormattedDate()).append(":\n\t");
            eventNames.append(events.get(i).getDescription());
        }

        StringBuilder merchNames = new StringBuilder();
        for (int i = 0; i < merchandises.size(); i++) {
            if (i != 0)
                merchNames.append(", ");

            merchNames.append(merchandises.get(i).getName()).append(" - ");
            merchNames.append(merchandises.get(i).getPrice()).append(":\n\t");
            merchNames.append(merchandises.get(i).getDescription());
        }

        return "Albums:\n\t[" + albumNames + "]\n\nMerch:\n\t[" + merchNames
                + "]\n\nEvents:\n\t[" + eventNames + "]";
    }
}
