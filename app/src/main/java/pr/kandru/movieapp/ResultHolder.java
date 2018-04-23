package pr.kandru.movieapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper Class that holds image results, movie, tv, and actor results
 */
public class ResultHolder implements Serializable {
    private List<Result> results;
    /**
     * Constructor
     */
    public ResultHolder() {
        this.results = new ArrayList<>();
    }

    /**
     * Add result to our list
     * @param result A singluar result item
     */
    public void add(Result result) {
        results.add(result);
    }

    /**
     * Get our Poster URLS
     * @return List of strings
     */
    public List<String> getImages() {
        List<String> images = new ArrayList<>();
        for(Result r: results)
            images.add(r.getPoster());
        return images;
    }

    /**
     * Get our names/titles
     * @return List of strings
     */
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for(Result r: results)
            names.add(r.getName());
        return names;
    }

    /**
     * Get our TMDB ID's
     * @return List of strings
     */
    public List<String> getIDs() {
        List<String> ids = new ArrayList<>();
        for(Result r: results)
            ids.add(r.getId());
        return ids;
    }

    /**
     * Getter for the number of items ResultHolder contains
     * @return Integer number of results
     */
    public int size() {
        return results.size();
    }

    /**
     * Get item at a certain index of our Result list
     * @param index Integer
     * @return Result A result at a certain index
     */
    public Result get(int index){
        return results.get(index);
    }
}
