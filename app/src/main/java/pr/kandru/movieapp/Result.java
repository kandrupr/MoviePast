package pr.kandru.movieapp;

import java.io.Serializable;

/**
 * Class is a container for a valid result from an TMDB search
 */
public class Result implements Serializable {
    private RequestType type;       /// Actor, Movie, TV Show
    private String name, id, poster; /// Title/Name, TMDB ID, Poster URL

    /**
     * Constrcutor
     * @param type Actor, Movie, TV Show
     * @param name Title/Name
     * @param id TMDB ID
     * @param poster Poster URL
     */
    public Result(RequestType type, String name, String id, String poster) {
        this.type = type;
        this.name = name;
        this.id = id;
        if(poster.equals("blank")) {
            this.poster = poster;
        } else {
            this.poster = "https://image.tmdb.org/t/p/w154" + poster;
        }
    }

    public RequestType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }
}