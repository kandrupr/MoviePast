package pr.kandru.movieapp;

import android.content.Context;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Singleton Class for our URLBuilder
 */
public class URLBuilder {
    private static URLBuilder single_instance = null;   // Singleton instance
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

    private final Context c;    // Context Used to get API Key and instance

    /**
     * Constructor
     * @param c Application Context
     */
    private URLBuilder(Context c) {
        this.c = c;
        this.apiKey = "?api_key=" + c.getString(R.string.TMDBAPI);
    }

    /**
     * Static method to create instance of Singleton class
     * @return URLBuilder
     */
    public static synchronized URLBuilder getInstance(Context c) {
        if (single_instance == null)
            single_instance = new URLBuilder(c.getApplicationContext());
        return single_instance;
    }

    /**
     * Builds a URL based off Descriptor
     * @param desc String Upcoming, Popular, and Best Rated
     * @param type String Actor, Movie, TV Show
     * @return String TMDB Descriptor URL
     */
    public String buildDescriptor(String desc, String type) {
        String url = tmdbUrl;
        // Upcoming not supported for TV Shows
        if(type.equals("tv") && desc.equals("upcoming"))
            return "invalid";
        else
            url += type + "/" + desc + apiKey + ending;
        return url;
    }

    /**
     * If given a descriptor and a year
     * @param desc String Upcoming, Popular, and Best Rated
     * @param type String Actor, Movie, TV Show
     * @param year String Year
     * @return String TMDB Descriptor URL with a time constraint
     */
    public String buildDescriptorByYear(String desc, String type, String year) {
        String url = tmdbUrl + "discover/";
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(type.equals("tv")) {     // Looking for a TV Show
            // TV
            url += "tv" + apiKey;
            if(desc.equals("upcoming")) {   // That is upcoming this year
                if (Integer.parseInt(year) == currentYear)
                    url += "&first_air_date.gte=" + year + "-1-1&first_air_date.lte=" + year + "-12-31&with_original_language=en" + ending;
                else
                    return "invalid";
            } else {
                if (Integer.parseInt(year) <= currentYear) { // That is currently airing or aired in the past
                    if(desc.equals("popular"))               // That is popular
                        url += "&sort_by=popularity.desc&air_date.gte=" + year + "-1-1&air_date.lte=" + year + "-12-31&vote_count.gte=50&with_original_language=en" + ending;
                    else                                     // That is top rated by user
                        url += "&sort_by=vote_average.desc&air_date.gte=" + year + "-1-1&air_date.lte=" + year + "-12-31&vote_count.gte=50&with_original_language=en" + ending;;
                } else
                    return "invalid";                       // Failed to put request together
            }
        } else {
            // MOVIE              // Looking for a Movie
            url += "movie" + apiKey + "&region=US";
            if(desc.equals("upcoming")) {   // That is not in the past
                if (Integer.parseInt(year) < currentYear)
                    return "invalid";
                else                        // TMDB has future movies already posted, info might be scarce
                    url += "&primary_release_date.gte=" + year + "-" + month + "-" + day + "&primary_release_date.lte=" + year + "-12-31" + ending;
            } else {
                if(desc.equals("popular"))  // Popular
                    url += "&sort_by=popularity.desc&page=1&primary_release_year=" + year;
                else                        // Top Rated
                    url += "&sort_by=vote_average.desc&page=1&primary_release_year=" + year;
                if (Integer.parseInt(year) > currentYear)
                    url += ending;          // Not enough votes for future movies
                else                        // Needs at least 50 to be considered
                    url += "&vote_count.gte=50" + ending;
            }
        }
        return url;
    }

    /**
     * Builds a Movie URL based off of DialogFlow result
     * @param title String Title of the movie
     * @return String TMDB Search Movie URL
     */
    public String buildMovie(String title) {
        String url = tmdbUrl + "search/movie" + apiKey + "&region=US&query=";
        // Encodes spaces as %20
        try {
            url += URLEncoder.encode(title, "UTF-8").replace("+", "%20") + ending;
        } catch (UnsupportedEncodingException ignored) {
            url += title + ending;
        }
        return url;
    }

    /**
     * Builds a request that searches for movies based off of genre
     * @param fields String[] holding Genre first and then year is available
     * @return String TMDB Discover Movies based on genre URL
     */
    public String buildMovieGenre(String [] fields) {
        // Using revenue instead of popularity or vote average
        String url = tmdbUrl + "discover/movie" + apiKey + "&sort_by=revenue.desc&region=US&with_genres=" + movieGenre.get(fields[0]);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(fields.length == 2) {    // Has year
            String year = fields[1];
            if(Integer.parseInt(year) > currentYear)
                return "invalid";   // Can't be in the future
            else
                url += "&primary_release_year=" + year;
        }
        url += ending;
        return url;
    }

    /**
     * Builds a request that searches for tv shows based off of genre
     * @param fields String[] holding Genre first and then year is available
     * @return String TMDB Discover TV Shows based on genre URL
     */
    public String buildTVGenre(String [] fields) {
        // Using revenue instead of popularity or vote average
        String url = tmdbUrl + "discover/tv" + apiKey + "&sort_by=popularity.desc&vote_count.gte=50&with_original_language=en&with_genres=" + tvGenre.get(fields[0]);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(fields.length == 2) {    // Has year
            String year = fields[1];
            if(Integer.parseInt(year) > currentYear) {
                return "invalid";   // Can't be in the future
            } else {
                url += "&air_date.gte="+ year +"-1-1&air_date.lte="+ year +"-12-31";
            }
        }
        url += ending;
        return url;
    }

    /**
     * Builds a TV URL based off of DialogFlow result
     * @param title String Title of the tv show
     * @return String TMDB Search TV URL
     */
    public String buildTV(String title) {
        String url = tmdbUrl + "search/tv" + apiKey + "&query=";
        // Encodes spaces as %20
        try {
            url += URLEncoder.encode(title, "UTF-8").replace("+", "%20") + ending;
        } catch (UnsupportedEncodingException ignored) {
            url += title + ending;
        }
        return url;
    }

    /**
     * Builds a search URL based off Title, If DialogFlow cannot place the RequestType
     * @param fields String[] holds title and
     * @return String A TMDB RequestType URL or a Multi-search URL
     */
    public String buildFromTitle(String[] fields) {
        String url = tmdbUrl + "search/";
        if(fields.length == 2) {    // Type defined
            if(fields[1].equals("tv"))
                url += "tv" + apiKey + "&query=";
            else if(fields[1].equals("movie"))
                url += "movie" + apiKey + "&region=US&query=";
            else
                url += "person" + apiKey + "&region=US&query=";

        } else                      // No type defined
            url += "multi" + apiKey + "&region=US&query=";
        // Encodes spaces as %20
        try {
            url += URLEncoder.encode(fields[0], "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ignored) {
            url += fields[0];
        }
        url += ending;
        return url;
    }

    /**
     * Builds an TMDB actor search URL
     * @param name String The name of the actor
     * @return String TMDB search Actor URL
     */
    public String buildFromPerson(String name) {
        String url = tmdbUrl + "search/person" + apiKey + "&region=US&query=";
        // Encodes spaces as %20
        try {
            url += URLEncoder.encode(name, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ignored) {
            url += name;
        }
        url += ending;
        return url;
    }

    /**
     * Builds an filmography of specific actor
     * @param id String TMDB ID
     * @param form String Movies, TV Shows
     * @return String TMDB URL for an actors credits
     */
    public String buildPersonForm(String id, String form) {
        String url = tmdbUrl + "person/" + id;
        if(form.equals("movie"))
            url += "/movie_credits";
        else
            url += "/tv_credits";
        url += apiKey + "&language=en-US";
        return url;
    }

    /**
     * Gets all the information about an actor
     * @param id String TMDB ID
     * @return String gets an actor's basic information, images, and all their credits
     */
    public String buildActorInfo(String id) {
        return tmdbUrl + "person/" + id + apiKey + "&language=en-US&append_to_response=images%2Ccombined_credits";
    }

    /**
     * Gets all the information about a movie
     * @param id String TMDB ID
     * @return String gets an movie's basic information, cast, and similar movies
     */
    public String buildMovieInfo(String id) {
        return tmdbUrl + "movie/" + id + apiKey + "&language=en-US&append_to_response=release_dates%2Ccredits%2Csimilar";
    }

    /**
     * Gets all the information about a tv show
     * @param id String TMDB ID
     * @return String gets an show's basic information, cast, and similar shows
     */
    public String buildTVInfo(String id) {
        return tmdbUrl + "tv/" + id + apiKey + "&language=en-US&append_to_response=content_ratings%2Ccredits%2Csimilar";
    }
}
