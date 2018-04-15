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

    public DialogFlowParser(Context context, Result json) {
        this.c = context;
        this.json = json;
        this.intent = json.getMetadata().getIntentName();
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
                if(params.containsKey("Title")) {
                    String title = params.get("Title").toString().replace("\"", "");
                    result = builder.buildMovie(title);
                } else {
                    result = "fail";
                }
                break;
            case "MovieGenre":
                if(params.containsKey("MovieGenre") && params.containsKey("Type")) {
                    info.put("Genre", params.get("MovieGenre").toString().replace("\"", ""));
                    if(params.containsKey("Year")) {
                        info.put("Year", params.get("Year").toString().replace("\"", ""));
                    }
                    result = builder.buildMovieGenre(info);
                } else {
                    result = "fail";
                }
                break;
            case "Person":
                name = checkPerson(params);
                result = personURL(name, builder);
                break;
            case "PersonForm":
                name = checkPerson(params);
                result = personURL(name, builder);
                break;
            case "TVShowGenre":
                if(params.containsKey("TVGenre") && params.containsKey("Type")) {
                    info.put("Genre", params.get("TVGenre").toString().replace("\"", ""));
                    if(params.containsKey("Year")) {
                        info.put("Year", params.get("Year").toString().replace("\"", ""));
                    }
                    result = builder.buildTVGenre(info);
                } else {
                    result = "fail";
                }
                break;
            case "TVShows":
                if(params.containsKey("Title")) {
                    String title = params.get("Title").toString().replace("\"", "");
                    result = builder.buildTV(title);
                } else {
                    result = "fail";
                }
                break;
            case "WithTitle":
                if(params.containsKey("Title")){
                    String title = getTitle(params.get("Title"));
                    info.put("Title", title);
                    if(params.containsKey("Type")) {
                        info.put("Type", params.get("Type").toString().replace("\"", "").toLowerCase());
                    }
                    result = builder.buildFromTitle(info);
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

    private String getTitle(JsonElement title) {
        if(title.isJsonObject()){
            JsonObject obj = title.getAsJsonObject();
            if(obj.has("TVShow")){
                return obj.get("TVShow").toString().replace("\"", "");
            } else {
                return obj.get("Movie").toString().replace("\"", "");
            }
        } else {
            return title.toString().replace("\"", "");
        }
    }

    private String checkPerson(HashMap<String, JsonElement> params) {
        String name;
        if(params.containsKey("Person")) {
            JsonElement person = params.get("Person");
            if(person.isJsonObject()) {
                JsonObject obj = person.getAsJsonObject();
                if (obj.has("First")) {
                    name = obj.get("First").toString().replace("\"", "");
                    if(obj.has("Last")) {
                        name += " " + obj.get("Last").toString().replace("\"", "");
                    }
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

    public String personURL(String name, URLBuilder builder) {
        String result;
        if(!name.equals("fail")) {
            result = builder.buildFromPerson(name);
        } else {
            result = "fail";
        }
        return result;
    }
}
