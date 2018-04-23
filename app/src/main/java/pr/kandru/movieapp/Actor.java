package pr.kandru.movieapp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pkkan on 4/19/2018.
 */

public class Actor extends InfoOverview {
    private String name, biography, poster;
    private List<String> images;
    private ResultHolder holder;

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
