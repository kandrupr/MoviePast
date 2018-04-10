package pr.kandru.movieapp;

import android.content.Context;
import android.util.Log;

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

    public JsonObject getURL() {
        String intent = getIntent();
        HashMap<String, JsonElement> params = json.getParameters();
        String descriptor, type, year, title, genre, name;
        JsonObject val = new JsonObject();
        switch(intent) {
            case "Descriptor":
                if(params.containsKey("Descriptor") && params.containsKey("Type")) {
                    descriptor = params.get("Descriptor").toString().replace("\"", "").toLowerCase();
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    // BUILD URL
                } else {
                    // ERROR
                }
                break;
            case "DescriptorByYear":
                if(params.containsKey("Descriptor") && params.containsKey("Type") && params.containsKey("Year")) {
                    descriptor = params.get("Descriptor").toString().replace("\"", "").toLowerCase();
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    year = params.get("Year").toString().replace("\"", "");
                    // BUILD URL
                } else {
                    // ERROR
                }
                break;
            case "Movie":
                if(params.containsKey("Title") && params.containsKey("Type")) {
                    title  = params.get("Title").toString().replace("\"", "");
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    // BUILD URL
                } else if(params.containsKey("Title")){
                    title  = params.get("Title").toString().replace("\"", "");
                    // BUILD URL MULTI?
                } else {
                    // FAIL
                }
                break;
            case "MovieGenre":
                if(params.containsKey("MovieGenre") && params.containsKey("Type") && params.containsKey("Year")) {
                    genre = params.get("MovieGenre").toString().replace("\"", "");
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    year = params.get("Year").toString().replace("\"", "");
                    // BUILD URL
                } else if(params.containsKey("MovieGenre") && params.containsKey("Type")) {
                    genre = params.get("MovieGenre").toString().replace("\"", "");
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    // BUILD URL
                } else {
                    // FAIL
                }
                break;
            case "Person":
                name = checkPerson(params);
                if(!name.equals("fail")) {
                    // BUILD URL
                } else {
                    // FAIL
                }
                break;
            case "PersonForm":
                name = checkPerson(params);
                if(!name.equals("fail")) {
                    if(params.containsKey("Type")) {
                        type = params.get("Type").toString().replace("\"", "");
                    } else {
                        // FAIL
                    }
                } else {
                    // FAIL
                }
                break;
            case "TVShowGenre":
                if(params.containsKey("TVGenre") && params.containsKey("Type") && params.containsKey("Year")) {
                    genre = params.get("TVGenre").toString().replace("\"", "");
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    year = params.get("Year").toString().replace("\"", "");
                    // BUILD URL
                } else if (params.containsKey("MovieGenre") && params.containsKey("Type")) {
                    genre = params.get("TVGenre").toString().replace("\"", "");
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    // BUILD URL
                } else {
                    // FAIL
                }
                break;
            case "TVShows":
                if(params.containsKey("TVShow") && params.containsKey("Type")) {
                    title  = params.get("TVShow").toString().replace("\"", "");
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    // BUILD URL
                } else if(params.containsKey("TVShow")){
                    title  = params.get("TVShow").toString().replace("\"", "");
                    // BUILD URL MULTI?
                } else {
                    // FAIL
                }
                break;
            case "WithTitle":
                if(params.containsKey("Title") && params.containsKey("Type")){
                    title = params.get("Title").toString().replace("\"", "");
                    type = params.get("Type").toString().replace("\"", "").toLowerCase();
                    // BUILD URL
                } else if(params.containsKey("Title")){
                    title = params.get("Title").toString().replace("\"", "");
                    // BUILD URL MULTI
                } else {
                    // FAIL
                }
                break;
            default:
                // FAIL
                break;
        }
        return val;
    }

    private String checkPerson(HashMap<String, JsonElement> params) {
        String name;
        if(params.containsKey("Person")) {
            JsonElement person = params.get("Person");
            if(person.isJsonObject()) {
                JsonObject obj = person.getAsJsonObject();
                if (obj.has("First") && obj.has("Last")) {
                    name = obj.get("First").toString().replace("\"", "") + " " + obj.get("Last").toString().replace("\"", "");//name += ;
                    Log.d("CLUTCH", name);
                    Log.d("CLUTC", "POG");

                    return "name";
                } else if (obj.has("First")) {
                    name = obj.get("First").toString().replace("\"", "");
                    Log.d("HARD", name);
                    return name;
                } else {
                    return "fail";
                }
            } else {
                name = params.get("Person").toString().replace("\"", "");
                Log.d("REG", name);
                return name;
            }
        } else {
            return "fail";
        }
    }
}
