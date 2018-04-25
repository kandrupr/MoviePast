package pr.kandru.movieapp;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import ai.api.model.Result;

/**
 * Class that processes the language from DialogFlow/API.AI result
 */
public class DialogFlowParser {
    private final Result json;    /// DialogFlow result
    private final Context c;
    private final String intent;  /// The type of request DialogFlow thinks it is

    /**
     * Constructor
     * @param context Application Context
     * @param json DialogFlow result
     */
    public DialogFlowParser(Context context, Result json) {
        this.c = context;
        this.json = json;
        this.intent = json.getMetadata().getIntentName();
    }

    /**
     * Returns the TMDB URL request
     * @return A valid TMDB URL or a fail flag
     */
    public String getURL() {
        HashMap<String, JsonElement> params = json.getParameters(); // Parameters for the intent
        String result, name;        // Result being what we send back, Name being an actor/actress name
        URLBuilder builder = URLBuilder.getInstance(c);     // Builds our TMDB URL
        String fields;      // Parameters for the intent as a String
        switch(this.intent) {
            case "Descriptor":  // Needs a descriptor and Type being Movie or TV Show
                if(params.containsKey("Descriptor") && params.containsKey("Type")) {
                    result = builder.buildDescriptor(params.get("Descriptor").toString().replace("\"", "").toLowerCase(),
                            params.get("Type").toString().replace("\"", "").toLowerCase());
                } else
                    return "fail";
                break;          // Needs a descriptor and Type being a Movie or TV Show and valid year
            case "DescriptorByYear":
                if(params.containsKey("Descriptor") && params.containsKey("Type") && params.containsKey("Year")) {
                    result = builder.buildDescriptorByYear(params.get("Descriptor").toString().replace("\"", "").toLowerCase(),
                            params.get("Type").toString().replace("\"", "").toLowerCase(),
                            params.get("Year").toString().replace("\"", ""));
                } else
                    return "fail";
                break;
            case "Movie":       // Movie Title
                if(params.containsKey("Title"))
                    result = builder.buildMovie(params.get("Title").toString().replace("\"", ""));
                else
                    return "fail";
                break;
            case "MovieGenre":  // Needs a genre, year is optional
                if(params.containsKey("moviegenre")) {
                    fields = params.get("moviegenre").toString().replace("\"", "");
                    if(params.containsKey("year")) {
                        fields += "," + params.get("year").toString().replace("\"", "");
                    }
                    result = builder.buildMovieGenre(fields.split(","));
                } else
                    return "fail";
                break;
            case "Person":      // Actor name
                name = checkPerson(params);
                result = personURL(name, builder);
                break;
            case "PersonForm":  // Actor name and movie/tv shows
                if(params.containsKey("Type")) {
                    name = checkPerson(params);
                    result = personURL(name, builder);
                } else
                    return "fail";
                break;
            case "TVShowGenre":     // Needs a genre, year is optional
                if(params.containsKey("tvgenre") && params.containsKey("type")) {
                    fields = params.get("tvgenre").toString().replace("\"", "");
                    if(params.containsKey("year"))
                        fields += "," + params.get("year").toString().replace("\"", "");
                    result = builder.buildTVGenre(fields.split(","));
                } else
                    return "fail";
                break;
            case "TVShows":         // TV Show title
                if(params.containsKey("Title")) {
                    String title = params.get("Title").toString().replace("\"", "");
                    result = builder.buildTV(title);
                } else {
                    return "fail";
                }
                break;
            case "WithTitle":       // Gets a title, but not certain type of media
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

    /**
     * Parses the title from an inner object of the param
     * @param title Title of a TV Show or Movie Series
     * @return String Title
     */
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

    /**
     * Parses the name from an inner object of the param
     * @param params check if a person is just a string or an Object with fields "First" and "Last"
     * @return A name as a string
     */
    private String checkPerson(HashMap<String, JsonElement> params) {
        String name;
        if(params.containsKey("person")) {
            JsonElement person = params.get("person");
            if(person.isJsonObject()) { // Object has "First" and "Last" in obj
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

    /**
     * Gets an TMDB Actor URL
     * @param name Our built name
     * @param builder Our URL Builder
     * @return A valid TMDB Actor URL
     */
    private String personURL(String name, URLBuilder builder) {
        String result;
        if(!name.equals("fail"))
            result = builder.buildFromPerson(name);
        else
            return "fail";

        return result;
    }
}
