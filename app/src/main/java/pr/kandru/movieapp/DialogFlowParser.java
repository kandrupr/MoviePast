package pr.kandru.movieapp;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import ai.api.model.Metadata;
import ai.api.model.Result;

/**
 * Created by pkkan on 4/1/2018.
 */

public class DialogFlowParser {
    private Result json;
    private Context c;

    private final String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=c67800592cc9f12da208901fb31247fd&language=en-US&page=1";
    private final String tv = "https://api.themoviedb.org/3/tv/";
    private final String movie = "https://api.themoviedb.org/3/movie/";


    public DialogFlowParser(Context context, Result json) {
        this.c = context;
        this.json = json;
    }

    public String getIntent() {
        final Metadata metadata =  json.getMetadata();
        return metadata.getIntentName();
    }

    public JsonObject getParams() {
        String intent = getIntent();
        HashMap<String, JsonElement> params = json.getParameters();
        String descriptor, type, year, title, genre;
        JsonObject val = new JsonObject();
        switch(intent) {
            case "Descriptor":
                descriptor = params.get("Descriptor").toString().toLowerCase();
                type = params.get("Type").toString().toLowerCase();
                break;
            case "DescriptorByYear":
                descriptor = params.get("Descriptor").toString().toLowerCase();
                type = params.get("Type").toString().toLowerCase();
                year = params.get("Year").toString();
                break;
            case "Movie":
                title = params.get("Title").toString();
                if(title != ""){
                    //FAIL
                } else {
                    type = params.get("Type").toString().toLowerCase();
                }
                break;
            case "MovieGenre":
                if(params.size() == 3) {
                    genre = params.get("MovieGenre").toString();
                    type = params.get("Type").toString().toLowerCase();
                    year = params.get("Year").toString();

                } else {
                    genre = params.get("MovieGenre").toString();
                    type = params.get("Type").toString().toLowerCase();
                }
                break;
            case "Person":
                break;
            case "PersonForm":
                break;
            case "TVShowGenre":
                if(params.size() == 3) {
                    genre = params.get("TVGenre").toString();
                    type = params.get("Type").toString().toLowerCase();
                    year = params.get("Year").toString();

                } else {
                    genre = params.get("TVGenre").toString();
                    type = params.get("Type").toString().toLowerCase();
                }
                break;
            case "TVShows":
                break;
            case "WithTitle":
                title = params.get("Title").toString().toLowerCase();
                type = params.get("Type").toString().toLowerCase();

                if(title != "" || type != ""){
                    //FAIL
                } else {
                    //PASS
                }
                break;
            default:
                break;
        }
        return val;
    }
}
