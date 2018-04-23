package pr.kandru.movieapp;

import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that parses JSON data retrieved from TMDB API
 */
public class BuildResult {
    public BuildResult() {}
    /**
     * Parse through an Actor, Movie, and TV Show request from TMDB
     * @param obj JSONObject with varying fields for an Actor, Movie, and TV Show
     * @param type Enumerated type to define if the request was for an actor, movie or tv show
     * @return Result A valid result with its form of media, Actor Name/Movie or TV Show title, ID, and poster path
     */
    @Nullable
    public Result checkData(JSONObject obj, RequestType type) {
        String name, id, poster;
        try {
            if(type.equals(RequestType.MOVIE))      // Movies have titles, tv shows and actors have names
                name = obj.get("title").toString();
            else
                name = obj.get("name").toString();

            id = obj.get("id").toString();          // All need to have IDs
            if(name.equals("null") || id.equals("null")) { return null; }

            if(type.equals(RequestType.ACTOR))      // Actors have profile paths, movies and tv shows have poster paths
                poster = obj.get("profile_path").toString();
            else
                poster = obj.get("poster_path").toString();

            if(poster.equals("null")){ return null; }   // No poster, nothing to display
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Result(type, name, id, poster);
    }

    /**
     * Parse through a Multi-Search result from TMDB
     * @param obj JSONObject with varying fields for an Actor, Movie, and TV Show
     * @return Result A valid result with its form of media, Actor Name/Movie or TV Show title, ID, and poster path
     */
    @Nullable
    public Result checkMultiData(JSONObject obj) {
        String name, id, poster, media;
        RequestType type;
        try {
            media = obj.get("media_type").toString();
            switch(media) {
                case "movie":
                    if(Integer.parseInt(obj.get("vote_count").toString()) <= 2){ return null;} // Not enough votes
                    name = obj.get("title").toString();
                    poster = obj.get("poster_path").toString();
                    type = RequestType.MOVIE;
                    break;
                case "tv":
                    if(Integer.parseInt(obj.get("vote_count").toString()) <= 2){ return null;} // Not enough votes
                    if(removeTalkShows(obj.get("character").toString(), obj.get("genre_ids").toString())){
                        return null;    // obj holds information for a TV Show
                    }
                    poster = obj.get("poster_path").toString();
                    name = obj.get("name").toString();
                    type = RequestType.TV;
                    break;
                case "person":
                    if(Float.parseFloat(obj.get("popularity").toString()) <= 1.0){ return null; } // Not popular enough
                    name = obj.get("name").toString();
                    type = RequestType.ACTOR;
                    poster = obj.get("profile_path").toString();
                    break;
                default:
                    return null;
            }
            id = obj.get("id").toString();
            if(name.equals("null") || id.equals("null")) { return null;}    // No title or way to ID
            if(poster.equals("null")){ return null; }   // No poster, nothing to display
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Result(type, name, id, poster);
    }

    /**
     * Attempts to parse out talk shows from based on criteria
     * @param character String Character played on a TV show
     * @param genres String A list of genres made into a string
     * @return bool Is this current result a talk show
     */
    private boolean removeTalkShows(String character, String genres) {
        if(character.equals("Himself") || character.equals("Herself") || character.isEmpty()) {
            if(genres.contains("10767") || genres.contains("10763")) { // Talk Shows and News
                return true;
            }
        }
        return false;
    }
}
