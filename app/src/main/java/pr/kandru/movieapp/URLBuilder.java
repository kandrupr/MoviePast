package pr.kandru.movieapp;

import java.util.HashMap;

/**
 * Created by pkkan on 4/7/2018.
 */

public class URLBuilder {
    private final String tmdbUrl = "https://api.themoviedb.org/3/";
    private final String apiKey = "?api_key=" + R.string.TMDBAPI;
    private final String ending = "&language=en-US&page=1";
    final HashMap<String, String> tvGenre = new HashMap<String, String>()
    {{
        put("Action & Adventure","10759"); put("Animation","16"); put("Comedy","35"); put("Crime","80");
        put("Documentary","99"); put("Drama","18"); put("Family","10751"); put("Kids","10762"); put("Mystery","9648");
        put("News","10763"); put("Reality","10764"); put("Sci-Fi & Fantasy","10765"); put("Soap","10766"); put("Talk","10767");
        put("War & Politics","10768"); put("Western","37");

    }};

    final HashMap<String, String> movieGenre = new HashMap<String, String>()
    {{
        put("Action","28"); put("Adventure","12");put("Animation","16"); put("Comedy","35"); put("Crime","80");
        put("Documentary","99"); put("Drama","18"); put("Family","10751"); put("Fantasy","14"); put("History","36");
        put("Horror","27"); put("Music","10402"); put("Mystery","9648"); put("Romance","10749");put("Science Fiction","878");
        put("TV Movie","10770"); put("Thriller","53"); put("War","10752"); put("Western","37");

    }};

    public URLBuilder() {}

    public String build(HashMap<String, String> params, String intent) {
        String url = tmdbUrl;
        switch(intent) {
            case("Descriptor"):
                if(params.get("Type").equals("tv") && params.get("Descriptor").equals("upcoming")) {
                    url = "fail";
                } else {
                    url += params.get("Type") + "/" + params.get("Descriptor") + apiKey + ending;
                }
            case("DescriptorByYear"):
                if(params.get("Type").equals("tv")) {
                    String desc = getDiscoverDescriptor(params.get("Descriptor"));
                } else {
                    // MOVIE
                }
                break;
            default:
                url = "fail";
                break;
        }
        return url;
    }

    private String getDiscoverDescriptor(String desc) {
        if(desc.equals("upcoming")) {
            return "";
        } else if(desc.equals("popular")) {
            return "";
        } else {
            return "";
        }
    }
}
