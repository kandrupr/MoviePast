package pr.kandru.movieapp;

import android.support.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pkkan on 4/19/2018.
 */

public class BuildResult {
    public BuildResult() {}

    @Nullable
    public Result checkData(JSONObject obj, RequestType type) {
        String name, id, poster;
        try {
            if(type.equals(RequestType.MOVIE))
                name = obj.get("title").toString();
            else
                name = obj.get("name").toString();

            id = obj.get("id").toString();
            if(name.equals("null") || id.equals("null")) { return null; }

            if(type.equals(RequestType.ACTOR))
                poster = obj.get("profile_path").toString();
            else
                poster = obj.get("poster_path").toString();

            if(poster.equals("null")){ return null; }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Result(type, name, id, poster);
    }

    @Nullable
    public Result checkMultiData(JSONObject obj) {
        String name, id, poster, media;
        RequestType type;
        try {
            media = obj.get("media_type").toString();
            switch(media) {
                case "movie":
                    if(Integer.parseInt(obj.get("vote_count").toString()) <= 2){ return null;}
                    name = obj.get("title").toString();
                    poster = obj.get("poster_path").toString();
                    type = RequestType.MOVIE;
                    break;
                case "tv":
                    if(Integer.parseInt(obj.get("vote_count").toString()) <= 2){ return null;}
                    if(removeTalkShows(obj.get("character").toString(), obj.get("genre_ids").toString())){
                        return null;
                    }
                    poster = obj.get("poster_path").toString();
                    name = obj.get("name").toString();
                    type = RequestType.TV;
                    break;
                case "person":
                    if(Float.parseFloat(obj.get("popularity").toString()) <= 1.0){ return null; }
                    name = obj.get("name").toString();
                    type = RequestType.ACTOR;
                    poster = obj.get("profile_path").toString();
                    break;
                default:
                    return null;
            }
            id = obj.get("id").toString();
            if(name.equals("null") || id.equals("null")) { return null;}
            if(poster.equals("null")){ return null; }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return new Result(type, name, id, poster);
    }

    private boolean removeTalkShows(String character, String genres) {
        if(character.equals("Himself") || character.equals("Herself") || character.isEmpty()) {
            if(genres.contains("10767") || genres.contains("10763")) {
                return true;
            }
        }
        return false;
    }
}
