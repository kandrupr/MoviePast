package pr.kandru.movieapp;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by pkkan on 4/7/2018.
 */

public class URLBuilder {
    private final String tmdbUrl = "https://api.themoviedb.org/3/";
    private String apiKey;
    private final String ending = "&language=en-US&page=1";
    private final HashMap<String, String> tvGenre = new HashMap<String, String>()
    {{
        put("Action & Adventure","10759"); put("Animation","16"); put("Comedy","35"); put("Crime","80");
        put("Documentary","99"); put("Drama","18"); put("Family","10751"); put("Kids","10762"); put("Mystery","9648");
        put("News","10763"); put("Reality","10764"); put("Sci-Fi & Fantasy","10765"); put("Soap","10766"); put("Talk","10767");
        put("War & Politics","10768"); put("Western","37");
    }};

    private final HashMap<String, String> movieGenre = new HashMap<String, String>()
    {{
        put("Action","28"); put("Adventure","12");put("Animation","16"); put("Comedy","35"); put("Crime","80");
        put("Documentary","99"); put("Drama","18"); put("Family","10751"); put("Fantasy","14"); put("History","36");
        put("Horror","27"); put("Music","10402"); put("Mystery","9648"); put("Romance","10749");put("Science Fiction","878");
        put("TV Movie","10770"); put("Thriller","53"); put("War","10752"); put("Western","37");
    }};

    private final Context c;

    public URLBuilder(Context c) {
        this.c = c;
        this.apiKey = "?api_key=" + c.getString(R.string.TMDBAPI);
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
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(params.get("Type").equals("tv")) {
            // TV
            url += "tv" + apiKey;
            if(desc.equals("upcoming")) {
                if(Integer.parseInt(year) == currentYear) {
                    url += "&first_air_date.gte=" + year + "-1-1&first_air_date.lte=" + year + "-12-31&with_original_language=en" + ending;
                } else {
                    url = "invalid";
                }
            } else if(desc.equals("popular")) {
                if(Integer.parseInt(year) <= currentYear){
                    url += "&sort_by=popularity.desc&air_date.gte=" + year + "-1-1&air_date.lte=" + year +"-12-31&vote_count.gte=50&with_original_language=en" + ending;
                } else {
                    url = "invalid";
                }
            } else {
                if(Integer.parseInt(year) <= currentYear){
                    url += "&sort_by=vote_average.desc&air_date.gte=" + year + "-1-1&air_date.lte=" + year +"-12-31&vote_count.gte=50&with_original_language=en" + ending;
                } else {
                    url = "invalid";
                }
            }
        } else {
            url += "movie" + apiKey;
            if(desc.equals("upcoming")) {
                if(Integer.parseInt(year) < currentYear) {
                    url = "invalid";
                } else {
                    // &primary_release_date.gte=2018-9-15&primary_release_date.lte=2018-10-15&language=en-US&region=US&page=1
                    url += "&region=US&primary_release_date.gte=" + year + "-" + month + "-" + day + "&primary_release_date.lte=" + year + "-12-31" + ending;
                }
            } else if(desc.equals("popular")) {
                url += "&region=US&sort_by=popularity.desc&page=1&primary_release_year=" + year;
                if(Integer.parseInt(year) > currentYear){
                     url += ending;
                } else {
                    url += "&vote_count.gte=50" + ending;
                }
            } else {
                url += "&region=US&sort_by=vote_average.desc&page=1&primary_release_year=" + year;
                if(Integer.parseInt(year) > currentYear){
                    url += ending;
                } else {
                    url += "&vote_count.gte=50" + ending;
                }
            }
        }
        return url;
    }

    public String buildMovie(String title) {
        String url = tmdbUrl + "search/";
        try {
            url += "movie" + apiKey + "&region=US&query=" + URLEncoder.encode(title, "UTF-8").replace("+", "%20") + ending;

        } catch (UnsupportedEncodingException ignored) {
            url += "movie" + apiKey + "&region=US&query=" + title + ending;
        }
        return url;
    }

    public String buildMovieGenre(HashMap<String, String> params) {
        String url = tmdbUrl + "discover/movie" + apiKey + "&sort_by=revenue.desc&region=US&with_genres=" + movieGenre.get(params.get("Genre"));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        // primary_release_year=2019&language=en-US&page=1
        if(params.containsKey("Year")) {
            String year = params.get("Year");
            if(Integer.parseInt(year) > currentYear) {
                return "invalid";
            } else {
                url += "&primary_release_year=" + year;
            }
        }
        url += ending;
        return url;
    }

    public String buildTVGenre(HashMap<String, String> params) {
        Log.d("TYPE TV GENRE", params.toString());
        String url = tmdbUrl + "discover/tv" + apiKey + "&sort_by=popularity.desc&vote_count.gte=50&with_original_language=en&with_genres=" + tvGenre.get(params.get("Genre"));
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(params.containsKey("Year")) {
            String year = params.get("Year");
            if(Integer.parseInt(year) > currentYear) {
                return "invalid";
            } else {
                url += "&air_date.gte="+ year +"-1-1&air_date.lte="+ year +"-12-31";
            }
        }
        url += ending;
        return url;
    }

    public String buildTV(String title) {
        String url = tmdbUrl + "search/";
        url += "tv" + apiKey + "&query=";
        try {
            url += URLEncoder.encode(title, "UTF-8").replace("+", "%20") + ending;
        } catch (UnsupportedEncodingException ignored) {
            url += title + ending;
        }
        return url;
    }

    public String buildFromTitle(HashMap<String, String> params) {
        String url = tmdbUrl + "search/";
        if(params.containsKey("Type")) {
            if(params.get("Type").equals("tv")) {
                url += "tv" + apiKey + "&query=";
            } else {
                url += "movie" + apiKey + "&region=US&query=";
            }
        } else {
            url += "multi" + apiKey + "&region=US&query=";
        }
        try {
            url += URLEncoder.encode(params.get("Title"), "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ignored) {
            url += params.get("Title");
        }
        url += ending;
        return url;
    }

    public String buildFromPerson(String name) {
        String url = tmdbUrl + "search/person" + apiKey + "&region=US&query=";
        try {
            url += URLEncoder.encode(name, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ignored) {
            url += name;
        }
        url += ending;
        return url;
    }

    public String buildPersonFrom(String id, String form) {
        String url = tmdbUrl + "person/" + id;
        if(form.equals("movie")) {
            url += "/movie_credits";
        } else {
            url += "/tv_credits";
        }
        url += apiKey + "&language=en-US";
        return url;
    }
}
