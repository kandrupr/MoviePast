package pr.kandru.movieapp;

import android.content.Context;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by pkkan on 4/7/2018.
 */

public class URLBuilder {
    private final String tmdbUrl = "https://api.themoviedb.org/3/";
    private String apiKey;
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

    private final Context c;

    public URLBuilder(Context c) {
        this.c = c;
        this.apiKey = "&api_key=" + c.getString(R.string.TMDBAPI);
    }

    public String buildDescriptor(HashMap<String, String> params) {
        String url = tmdbUrl;
        if(params.get("Type").equals("tv") && params.get("Descriptor").equals("upcoming")) {
            url = "invalid";
        } else {
            url += params.get("Type") + "/" + params.get("Descriptor") + apiKey + ending;
        }
        return url;
    }

    public String buildDescriptorByYear(HashMap<String, String> params) {
    // &region=US&sort_by=vote_average.desc&page=1&primary_release_year=2017&vote_count.gte=50&language=en-US
        String url = tmdbUrl + "discover/";
        String desc = params.get("Descriptor");
        String year = params.get("Year");
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.MONTH);

        if(params.get("Type").equals("tv")) {
            // TV
            url += "tv/" + apiKey;
            if(desc.equals("upcoming")) {
                if(Integer.parseInt(year) == Calendar.getInstance().get(Calendar.YEAR)) {
                    url += "&first_air_date.gte=" + year + "-1-1&first_air_date.lte=" + year + "-12-31&with_original_language=en" + ending;
                } else {
                    url = "invalid";
                }
            } else if(desc.equals("popular")) {
                if(Integer.parseInt(year) >= Calendar.getInstance().get(Calendar.YEAR)){
                    url += "&sort_by=popularity.desc&air_date.gte=" + year + "-1-1&air_date.lte=" + year +"-12-31&vote_count.gte=50&with_original_language=en" + ending;
                } else {
                    url = "invalid";
                }
            } else {
                if(Integer.parseInt(year) >= Calendar.getInstance().get(Calendar.YEAR)){
                    url += "&sort_by=vote_average.desc&air_date.gte=" + year + "-1-1&air_date.lte=" + year +"-12-31&vote_count.gte=50&with_original_language=en" + ending;
                } else {
                    url = "invalid";
                }
            }
        } else {
            url += "movie/" + apiKey;
            if(desc.equals("upcoming")) {
                if(Integer.parseInt(year) < Calendar.getInstance().get(Calendar.YEAR)) {
                    url = "invalid";
                } else {
                    // &primary_release_date.gte=2018-9-15&primary_release_date.lte=2018-10-15&language=en-US&region=US&page=1
                    url += "&region=US&primary_release_date.gte=" + year + "-" + month + "-" + day + "primary_release_date.lte=" + year + "-12-31" + ending;
                }
            } else if(desc.equals("popular")) {
                if(Integer.parseInt(year) > Calendar.getInstance().get(Calendar.YEAR)){
                    url += "&region=US&sort_by=popularity.desc&page=1&primary_release_year=" + year + ending;
                } else {
                    url += "&region=US&sort_by=popularity.desc&page=1&primary_release_year=" + year + "&vote_count.gte=50" + ending;
                }
            } else {
                if(Integer.parseInt(year) > Calendar.getInstance().get(Calendar.YEAR)){
                    url += "&region=US&sort_by=vote_average.desc&page=1&primary_release_year=" + year + ending;
                } else {
                    url += "&region=US&sort_by=vote_average.desc&page=1&primary_release_year=" + year + "&vote_count.gte=50" + ending;
                }
            }
        }
        return url;
    }

    public String build(HashMap<String, String> info, String intent) {
        return "";
    }

    public String getApiKey() {
        return apiKey;
    }
}
