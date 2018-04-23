package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Activity that loads a built TMDB URL result
 */
public class LoadingInfo extends AppCompatActivity {
    private final String image_url = "https://image.tmdb.org/t/p/w154";
    private RequestType type;       // Actor, Movie, TV Show
    private String name, id, poster;    // Result attributes
    private BuildResult buildResult = new BuildResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        URLBuilder builder = URLBuilder.getInstance(getApplicationContext());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Result result = (Result) bundle.getSerializable("RESULT");
            // Result attributes
            if(result != null) {
                name = result.getName();
                type = result.getType();
                id = result.getId();
                poster = result.getPoster();
            } else {
                finish();
                Toast.makeText(this, "Failed to load page", Toast.LENGTH_SHORT).show();
            }
        } else {
            finish();
            Toast.makeText(this, "Failed to load page", Toast.LENGTH_SHORT).show();
        }

        // New URL with specific ID
        String url = "";
        switch(type) {
            case ACTOR:
                url = builder.buildActorInfo(id);
                break;
            case MOVIE:
                url = builder.buildMovieInfo(id);
                break;
            case TV:
                url = builder.buildTVInfo(id);
                break;
            default:
                // FAIL AND TOAST;
                finish();
                break;
        }
        // Get results on specific result
        getInfo(url);
    }

    /**
     * Adds a request to our Volley Singleton
     * @param url String TMDB ID URL
     */
    private void getInfo(String url) {
        JsonObjectRequest jsonObjectRequest = createObject(url, this);
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * JSONObject of Volley Request
     * @param url String TMDB ID URL
     * @param c Context Activity Context
     * @return JSONObject of results
     */
    private JsonObjectRequest createObject(String url, final Context c) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        switch(type) {
                            case ACTOR:
                                Actor actor = parseActorObject(response);
                                showMedia(actor, RequestType.ACTOR);
                                break;
                            case MOVIE:
                                Movie movie = parseMovieObject(response);
                                showMedia(movie, RequestType.MOVIE);
                                break;
                            case TV:
                                TVShow tv = parseTVObject(response);
                                showMedia(tv, RequestType.TV);
                                break;
                            default:
                                // FAIL AND TOAST;
                                break;
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        Toast.makeText(c, "Couldn't load page", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Find a specific field in a JSONObject
     * @param obj Object Results
     * @param field String What we are looking for
     * @param error String Error Message
     * @return String Field
     */
    String findField(JSONObject obj, String field, String error) {
        String val;
        try {
            val = obj.get(field).toString();
            if(val.isEmpty() || val.equals("null")){
                val = error;        // Check if it is empty
            }
        } catch (JSONException e) {
            e.printStackTrace();
            val = error;
        }
        return val;
    }

    /**
     * Create ResultHolder object for images, cast, and similar
     * @param response JSONObject TMDB results
     * @param first String first field to get object
     * @param second String second field for an array
     * @param type RequestType Actor, Movie, TV Show
     * @return ResultHolder
     */
    ResultHolder findCarouselInfo(JSONObject response, String first, String second, RequestType type){
        ResultHolder results = new ResultHolder();
        try {
            JSONArray jsonCast = response.getJSONObject(first).getJSONArray(second);
            int size = jsonCast.length();
            if(size > 10) { size = 10; }
            for (int i = 0; i < size; i++) {
                JSONObject obj = jsonCast.getJSONObject(i);
                Result result = buildResult.checkData(obj, type);
                if (result != null) { results.add(result); }    // Add to results if not empty
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Create a comma separated String of genres
     * @param obj JSONObject holding array of genres
     * @return String genres
     */
    private String findGenres(JSONObject obj) {
        String genres = "";
        try {
            JSONArray jsonGenres = obj.getJSONArray("genres");
            for (int i = 0; i < jsonGenres.length(); i++){
                String genre = jsonGenres.getJSONObject(i).get("name").toString();
                if(!genre.isEmpty() || !genre.equals("null"))   // Check to see if string is applicable
                    genres += genre + ", ";
            }
            if(genres.length() == 0){
                genres = "No genres available";
            } else {    // Remove trailing space and comma
                genres = genres.substring(0,genres.length()-2);
            }
        } catch (JSONException e) { // Some Error or no genres
            e.printStackTrace();
            genres = "No genres available";
        }
        return genres;
    }

    /**
     * Gets the first element in a JSONArray for a specific field
     * @param obj JSONObject TMDB ID results
     * @param arr String the array we are looking for
     * @param field String field we are looking
     * @param error String Error Message
     * @return String Field
     */
    private String findFirstArrayElement(JSONObject obj, String arr, String field, String error) {
        String val;
        try {
            val = obj.getJSONArray(arr).getJSONObject(0).get(field).toString();
            if(val.isEmpty() || val.equals("null")){
                val = error;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            val = error;
        }
        return val;
    }

    /**
     * Create an Actor Object
     * @param response JSONObject TMDB Results
     * @return Actor Object
     */
    private Actor parseActorObject(JSONObject response) {
        List<String> images = new ArrayList<>();    // Image URL
        ResultHolder holder = new ResultHolder();   // Filmography
        String biography;
        // Get actor images
        try {
            JSONArray arr = response.getJSONObject("images").getJSONArray("profiles");
            int size = arr.length();
            if(size > 10) { size = 10; }    // Up to 10
            String path;
            for (int i = 0; i < size; i++) {
                JSONObject index = arr.getJSONObject(i);
                path = index.get("file_path").toString();
                if(!path.equals("null") || !path.isEmpty())
                    images.add(image_url + path);   // Add Images
            }
        } catch (JSONException e) {
            e.printStackTrace();    // NO IMAGES THATS OKAY

        }
        // Get filmography
        try {
            JSONArray arr = response.getJSONObject("combined_credits").getJSONArray("cast");
            int size = arr.length();
            if(size > 0) {  // Make List of JSONObject to sort
                List<JSONObject> myJsonArrayAsList = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++)
                    myJsonArrayAsList.add(arr.getJSONObject(i));
                // Sort based off of popularity
                Collections.sort(myJsonArrayAsList, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                        int compare = 0;
                        try {
                            float keyA = (float) jsonObjectA.getDouble("popularity");
                            float keyB = (float) jsonObjectB.getDouble("popularity");
                            compare = Float.compare(keyB, keyA);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return compare;
                    }
                });
                // Get at least 20 results
                arr = new JSONArray();
                if (size > 20) { size = 20; }
                for (int i = 0; i < size; i++)
                    arr.put(myJsonArrayAsList.get(i));
                // Get 10 most popular results
                for (int i = 0; i < size; i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Result result = buildResult.checkMultiData(obj);
                    if (result != null) {
                        holder.add(result);
                    }
                    if(holder.size() == 10){ break; }
                }
            }
        } catch (JSONException e) { // No filmography
            e.printStackTrace();
        }
        biography = findField(response, "biography","No Information Available");
        return new Actor(name, biography, poster, images, holder);
    }

    /**
     * Create a Movie Object based off TMDB ID results
     * @param response JSONObject TMDB ID results
     * @return Movie Object
     */
    private Movie parseMovieObject(JSONObject response) {
        ResultHolder cast;      // Credits
        ResultHolder similar;   // Similar movies
        String rating , runtime, releaseDate, overview ,genres;
        String mpaa = "";

        // Get Movie attributes
        genres = findGenres(response);
        overview = findField(response, "overview", "No overview available");
        releaseDate = findField(response, "release_date", "No release date available");
        runtime = findField(response, "runtime", "No runtime available");
        rating = findField(response, "vote_average", "0/10");
        // Parse MPAA ratings
        try {
            JSONArray mpaaRatings = response.getJSONObject("release_dates").getJSONArray("results");
            for(int i = 0; i < mpaaRatings.length(); i++) {
                if(mpaaRatings.getJSONObject(i).get("iso_3166_1").equals("US")){
                    mpaa = mpaaRatings.getJSONObject(i).getJSONArray("release_dates").getJSONObject(0).get("certification").toString();
                    break;
                }
            }
            if(mpaa.isEmpty() || mpaa.equals("null")) {
                mpaa = "No rating available";
            }
        } catch (JSONException e) {
            e.printStackTrace();    // No ratings available
            mpaa = "No rating available";
        }
        cast = findCarouselInfo(response, "credits", "cast", RequestType.ACTOR);
        similar = findCarouselInfo(response, "similar", "results", type);

        return new Movie(name, poster, rating, mpaa, runtime, releaseDate, overview, genres, cast, similar);
    }

    /**
     * Create TV Object based of TMDB ID results
     * @param response JSONObject TMDB ID results
     * @return TV Object
     */
    private TVShow parseTVObject(JSONObject response) {
        String runTime, firstDate,  genres, network, origin,  numEpisodes, numSeason,overview, status, contentRatings;
        ResultHolder cast;      // Cast
        ResultHolder similar;   // Similar TV Shows
        try {
            runTime = response.getJSONArray("episode_run_time").get(0).toString();
            if(runTime.isEmpty() || runTime.equals("null")){
                runTime = "Run Time is not available";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            runTime = "Run time is not available";
        }
        // Parse TV attributes
        firstDate = findField(response, "first_air_date", "First airing not available");
        genres = findGenres(response);
        network = findFirstArrayElement(response, "networks", "name", "No TV Network available");
        origin = findFirstArrayElement(response, "networks", "origin_country", "Don't have origin country");
        numEpisodes = findField(response, "number_of_episodes", "Number of seasons unavailable");
        numSeason = findField(response, "number_of_seasons", "Number of seasons unavailable");
        overview = findField(response, "overview", "No overview available");
        status = findField(response, "status", "Don't have current status");
        // Parse content ratings
        try {
            JSONObject cRatings = response.getJSONObject("content_ratings");
            contentRatings = findFirstArrayElement(cRatings,"results", "rating", "No Content Rating available");
        } catch (JSONException e) {
            e.printStackTrace();
            contentRatings = "No Content Rating available";
        }

        cast = findCarouselInfo(response, "credits", "cast", RequestType.ACTOR);
        similar = findCarouselInfo(response, "similar", "results", type);

        return new TVShow(name, poster, runTime, firstDate, genres, network, origin, numEpisodes, numSeason, overview, status, contentRatings, cast, similar);
    }

    /**
     * Move to InfoActivity now that all information is set
     * @param info InfoOverview Our result object cast to its parent
     * @param type RequestType Actor, Movie, TVShow
     */
    private void showMedia(InfoOverview info, RequestType type) {
        Intent intent = new Intent(this, InfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", info);
        intent.putExtras(bundle);
        intent.putExtra("FORM", type);
        finish();
        startActivity(intent);
    }
}

