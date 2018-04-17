package pr.kandru.movieapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pkkan on 4/17/2018.
 */

public class ResultHolder implements Serializable {
    List<Result> results;
    public ResultHolder() {
        this.results = new ArrayList<>();
    }

    public void add(Result result) {
        results.add(result);
    }

    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        for(Result r: results) {
            images.add(r.getPoster());
        }
        return images;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for(Result r: results) {
            names.add(r.getPoster());
        }
        return names;
    }

    public int size() {
        return results.size();
    }
}
