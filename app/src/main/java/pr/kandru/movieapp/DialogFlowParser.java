package pr.kandru.movieapp;

import android.content.Context;
import android.util.Log;

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
        URLBuilder builder = URLBuilder.getInstance(c);
        String fields;
        switch(this.intent) {
            case "Descriptor":
                if(params.containsKey("Descriptor") && params.containsKey("Type")) {
                    result = builder.buildDescriptor(params.get("Descriptor").toString().replace("\"", "").toLowerCase(),
                            params.get("Type").toString().replace("\"", "").toLowerCase());
                } else
                    return "fail";
                break;
            case "DescriptorByYear":
                if(params.containsKey("Descriptor") && params.containsKey("Type") && params.containsKey("Year")) {
                    result = builder.buildDescriptorByYear(params.get("Descriptor").toString().replace("\"", "").toLowerCase(),
                            params.get("Type").toString().replace("\"", "").toLowerCase(),
                            params.get("Year").toString().replace("\"", ""));
                } else
                    return "fail";
                break;
            case "Movie":
                if(params.containsKey("Title"))
                    result = builder.buildMovie(params.get("Title").toString().replace("\"", ""));
                else
                    return "fail";
                break;
            case "MovieGenre":
                if(params.containsKey("moviegenre")) {
                    fields = params.get("moviegenre").toString().replace("\"", "");
                    if(params.containsKey("year")) {
                        fields += "," + params.get("year").toString().replace("\"", "");
                    }
                    result = builder.buildMovieGenre(fields.split(","));
                } else
                    return "fail";
                break;
            case "Person":
                name = checkPerson(params);
                result = personURL(name, builder);
                break;
            case "PersonForm":
                if(params.containsKey("Type")) {
                    name = checkPerson(params);
                    result = personURL(name, builder);
                } else
                    return "fail";

                break;
            case "TVShowGenre":
                if(params.containsKey("tvgenre") && params.containsKey("type")) {
                    fields = params.get("tvgenre").toString().replace("\"", "");
                    if(params.containsKey("year"))
                        fields += "," + params.get("year").toString().replace("\"", "");
                    result = builder.buildTVGenre(fields.split(","));
                } else
                    return "fail";
                break;
            case "TVShows":
                if(params.containsKey("Title")) {
                    String title = params.get("Title").toString().replace("\"", "");
                    result = builder.buildTV(title);
                } else {
                    return "fail";
                }
                break;
            case "WithTitle":
                if(params.containsKey("Title")){
                    fields = getTitle(params.get("Title"));
                    if(params.containsKey("Type"))
                        fields += "," + params.get("Type").toString().replace("\"", "").toLowerCase();
                    result = builder.buildFromTitle(fields.split(","));
                } else
                    return "fail";
                break;
            default:
                return "fail";
        }
        return result;
    }

    private String getTitle(JsonElement title) {
        if(title.isJsonObject()){
            JsonObject obj = title.getAsJsonObject();
            if(obj.has("TVShow"))
                return obj.get("TVShow").toString().replace("\"", "");
            else
                return obj.get("Movie").toString().replace("\"", "");
        } else
            return title.toString().replace("\"", "");
    }

    private String checkPerson(HashMap<String, JsonElement> params) {
        String name;
        if(params.containsKey("person")) {
            JsonElement person = params.get("person");
            if(person.isJsonObject()) {
                JsonObject obj = person.getAsJsonObject();
                if (obj.has("First")) {
                    name = obj.get("First").toString().replace("\"", "");
                    if(obj.has("Last"))
                        name += " " + obj.get("Last").toString().replace("\"", "");
                    return name;
                } else
                    return "fail";
            } else {
                name = params.get("person").toString().replace("\"", "");
                return name;
            }
        } else
            return "fail";
    }

    public String personURL(String name, URLBuilder builder) {
        String result;
        if(!name.equals("fail"))
            result = builder.buildFromPerson(name);
        else
            return "fail";

        return result;
    }
}
