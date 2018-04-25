package pr.kandru.movieapp;

import java.util.List;

/**
 * Actor class which holds attributes to display in InfoActivity
 * Name, Biography, Poster is an image url, Images is a list of image urls, holder is a container for an actor's filmography
 */
public class Actor extends InfoOverview {
    final private String name, biography, poster;
    final private List<String> images;
    final private ResultHolder holder;

    public Actor(String name, String biography, String poster, List<String> images, ResultHolder holder) {
        this.name = name;
        this.biography = biography;
        this.poster = poster;
        this.images = images;
        this.holder = holder;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return biography;
    }

    public String getPoster() {
        return poster;
    }

    public List<String> getImages() {
        return images;
    }

    public ResultHolder getHolder() {
        return holder;
    }
}
