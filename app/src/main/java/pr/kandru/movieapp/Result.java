package pr.kandru.movieapp;

import java.io.Serializable;

/**
 * Created by pkkan on 4/17/2018.
 */

public class Result implements Serializable {
    private Type type;
    private String name;
    private String id;
    private String poster;

    public Result(Type type, String name, String id, String poster) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.poster = "https://image.tmdb.org/t/p/w500" + poster;
    }

    public Type getType() {
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