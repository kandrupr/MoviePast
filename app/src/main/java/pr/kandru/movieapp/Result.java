package pr.kandru.movieapp;

import java.io.Serializable;

/**
 * Created by pkkan on 4/17/2018.
 */

public class Result implements Serializable {
    private RequestType type;
    private String name, id, poster;

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