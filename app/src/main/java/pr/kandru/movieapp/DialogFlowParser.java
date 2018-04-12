package pr.kandru.movieapp;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

import ai.api.model.Result;

/**
 * Created by pkkan on 4/1/2018.
 */

public class DialogFlowParser {
    private Result json;
    private Context c;
    private String intent;

    private final String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=c67800592cc9f12da208901fb31247fd&language=en-US&page=1";
    private final String tv = "https://api.themoviedb.org/3/tv/";
    private final String movie = "https://api.themoviedb.org/3/movie/";


    public DialogFlowParser(Context context, Result json) {
        this.c = context;
        this.json = json;
        this.intent = json.getMetadata().getIntentName();
    }

    public String getIntent() {
        return this.intent;
    }


    public String getURL() {
        HashMap<String, JsonElement> params = json.getParameters();
        String result, name;
        JsonObject val = new JsonObject();
        URLBuilder builder = new URLBuilder(c);
        HashMap<String, String> info = new HashMap<String, String>();
        switch(this.intent) {
            case "Descriptor":
                if(params.containsKey("Descriptor") && params.containsKey("Type")) {
                    info.put("Descriptor", params.get("Descriptor").toString().replace("\"", "").toLowerCase());
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    result = builder.buildDescriptor(info);
                } else {
                    result = "fail";
                }
                break;
            case "DescriptorByYear":
                if(params.containsKey("Descriptor") && params.containsKey("Type") && params.containsKey("Year")) {
                    info.put("Descriptor", params.get("Descriptor").toString().replace("\"", "").toLowerCase());
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    info.put("Year", params.get("Year").toString().replace("\"", ""));
                    result = builder.buildDescriptorByYear(info);
                } else {
                    result = "fail";
                }
                break;
            case "Movie":
                if(params.containsKey("Title") && params.containsKey("Type")) {
                    info.put("Title", params.get("Title").toString().replace("\"", ""));
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    result = builder.buildMovie(info);
                } else if(params.containsKey("Title")){
                    info.put("Title", params.get("Title").toString().replace("\"", ""));
                    result = builder.buildMovie(info);
                } else {
                    result = "fail";
                }
                break;
            case "MovieGenre":
                if(params.containsKey("MovieGenre") && params.containsKey("Type") && params.containsKey("Year")) {
                    info.put("Genre", params.get("MovieGenre").toString().replace("\"", ""));
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    info.put("Year", params.get("Year").toString().replace("\"", ""));
                    result = builder.build(info, this.intent);
                    // BUILD URL
                } else if(params.containsKey("MovieGenre") && params.containsKey("Type")) {
                    info.put("Genre", params.get("MovieGenre").toString().replace("\"", ""));
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    result = builder.build(info, this.intent);
                    // BUILD URL
                } else {
                    result = "fail";
                }
                break;
            case "Person":
                name = checkPerson(params);
                if(!name.equals("fail")) {
                    info.put("Name", name);
                    result = builder.build(info, this.intent);
                } else {
                    result = "fail";
                }
                break;
            case "PersonForm":
                name = checkPerson(params);
                if(!name.equals("fail")) {
                    if(params.containsKey("Type")) {
                        info.put("Name", name);
                        info.put("Type", params.get("Type").toString().replace("\"", ""));
                        result = builder.build(info, this.intent);
                    } else {
                        result = "fail";
                    }
                } else {
                    result = "fail";
                }
                break;
            case "TVShowGenre":
                if(params.containsKey("TVGenre") && params.containsKey("Type") && params.containsKey("Year")) {
                    info.put("Genre", params.get("TVGenre").toString().replace("\"", ""));
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    info.put("Year", params.get("Year").toString().replace("\"", ""));
                    result = builder.build(info, this.intent);
                } else if (params.containsKey("MovieGenre") && params.containsKey("Type")) {
                    info.put("Genre", params.get("TVGenre").toString().replace("\"", ""));
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    result = builder.build(info, this.intent);
                } else {
                    result = "fail";
                }
                break;
            case "TVShows":
                if(params.containsKey("TVShow") && params.containsKey("Type")) {
                    info.put("Title", params.get("TVShow").toString().replace("\"", ""));
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    result = builder.build(info, this.intent);
                } else if(params.containsKey("TVShow")){
                    info.put("Title", params.get("TVShow").toString().replace("\"", ""));
                    result = builder.build(info, this.intent);
                    // BUILD URL MULTI?
                } else {
                    result = "fail";
                }
                break;
            case "WithTitle":
                if(params.containsKey("Title") && params.containsKey("Type")){
                    info.put("Title", params.get("Title").toString().replace("\"", ""));
                    info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    result = builder.build(info, this.intent);
                } else if(params.containsKey("Title")){
                    info.put("Title", params.get("Title").toString().replace("\"", ""));
                    result = builder.build(info, this.intent);
                    // BUILD URL MULTI
                } else {
                    result = "fail";
                }
                break;
            default:
                result = "fail";
                break;
        }
        return result;
    }

    private String checkPerson(HashMap<String, JsonElement> params) {
        String name;
        if(params.containsKey("Person")) {
            JsonElement person = params.get("Person");
            if(person.isJsonObject()) {
                JsonObject obj = person.getAsJsonObject();
                if (obj.has("First") && obj.has("Last")) {
                    name = obj.get("First").toString().replace("\"", "") + " " + obj.get("Last").toString().replace("\"", "");//name += ;
                    return name;
                } else if (obj.has("First")) {
                    name = obj.get("First").toString().replace("\"", "");
                    return name;
                } else {
                    return "fail";
                }
            } else {
                name = params.get("Person").toString().replace("\"", "");
                return name;
            }
        } else {
            return "fail";
        }
    }
}
