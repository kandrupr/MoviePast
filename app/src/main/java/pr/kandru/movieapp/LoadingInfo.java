package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
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
 * Created by pkkan on 4/19/2018.
 */

public class LoadingInfo extends AppCompatActivity {
    final String image_url = "https://image.tmdb.org/t/p/w500";
    private URLBuilder builder;
    private RequestType type;
    private String name;
    private String id;
    private String poster;
    BuildResult buildResult = new BuildResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        builder = new URLBuilder(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Result result = (Result) bundle.getSerializable("RESULT");
        name = result.getName();
        type = result.getType();
        id = result.getId();
        poster = result.getPoster();

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
        getInfo(url);
    }

    private void getInfo(String url) {
        JsonObjectRequest jsonObjectRequest = createObject(url, this);
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private JsonObjectRequest createObject(String url, final Context c) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());

                        switch(type) {
                            case ACTOR:
                                Actor actor = parseActorObject(response);
                                showActor(actor);
                                break;
                            case MOVIE:
                                Movie movie = parseMovieObject(response);
                                showMovie(movie);
                                break;
                            case TV:
                                TVShow tv = parseTVObject(response);
                                showTVShow(tv);
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
                        Toast.makeText(c, "Network Error", Toast.LENGTH_LONG).show();
                    }
                });
    }


    String findField(JSONObject obj, String field, String error) {
        String val;
        try {
            val = obj.get(field).toString();
            if(val.isEmpty() || val.equals("null")){
                val = error;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            val = error;
        }
        return val;
    }

    ResultHolder findCarouselInfo(JSONObject response, String first, String second, RequestType type){
        ResultHolder results = new ResultHolder();
        try {
            JSONArray jsonCast = response.getJSONObject(first).getJSONArray(second);
            int size = jsonCast.length();
            if(size > 10) {
                size = 10;
            }
            for (int i = 0; i < size; i++) {
                JSONObject obj = jsonCast.getJSONObject(i);
                Result result = buildResult.checkData(obj, type);
                if (result != null) {
                    results.add(result);
                }
                if(results.size() == 10){
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    private String findGenres(JSONObject obj) {
        String genres = "";
        try {
            JSONArray jsonGenres = obj.getJSONArray("genres");
            for (int i = 0; i < jsonGenres.length(); i++){
                String genre = jsonGenres.getJSONObject(i).get("name").toString();
                if(!genre.isEmpty() || !genre.equals("null")){
                    genres += genre + ", ";
                }
            }
            if(genres.length() == 0){
                genres = "No genres available";
            } else {
                genres = genres.substring(0,genres.length()-2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            genres = "No genres available";
            // NO IMAGES THATS OKAY
        }
        return genres;
    }

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

    private Actor parseActorObject(JSONObject response) {
        List<String> images = new ArrayList<>();
        ResultHolder holder = new ResultHolder();
        String biography;
        try {
            JSONArray arr = response.getJSONObject("images").getJSONArray("profiles");
            int size = arr.length();
            if(size > 10) { size = 10; }
            String path;
            for (int i = 0; i < size; i++) {
                JSONObject index = arr.getJSONObject(i);
                path = index.get("file_path").toString();
                if(!path.equals("null") || !path.isEmpty()) {
                    images.add(image_url + path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // NO IMAGES THATS OKAY
        }

        try {
            JSONArray arr = response.getJSONObject("combined_credits").getJSONArray("cast");
            int size = arr.length();
            if(size > 0) {
                List<JSONObject> myJsonArrayAsList = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++)
                    myJsonArrayAsList.add(arr.getJSONObject(i));

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

                arr = new JSONArray();
                if (size > 20) { size = 20; }
                for (int i = 0; i < size; i++)
                    arr.put(myJsonArrayAsList.get(i));

                for (int i = 0; i < size; i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Result result = buildResult.checkMultiData(obj);
                    if (result != null) {
                        holder.add(result);
                    }
                    if(holder.size() == 10){
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        biography = findField(response, "biography","No Information Available");
        return new Actor(name, biography, poster, images, holder);
    }

    private Movie parseMovieObject(JSONObject response) {
        ResultHolder cast;
        ResultHolder similar;
        String rating , runtime, releaseDate, overview ,genres;
        String mpaa = "";

        genres = findGenres(response);
        //Log.d("MOVIE GENRES", genres);

        overview = findField(response, "overview", "No overview available");
        //Log.d("MOVIE OVERVIEW", overview);

        releaseDate = findField(response, "release_date", "No release date available");
        //Log.d("MOVIE RELEASE", releaseDate);

        runtime = findField(response, "runtime", "No runtime available");
        //Log.d("MOVIE RUNTIME", runtime);

        rating = findField(response, "vote_average", "0/10");
        //Log.d("MOVIE RATING", rating);

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
            e.printStackTrace();
            mpaa = "No rating available";
            // NO IMAGES THATS OKAY
        }
        //Log.d("MOVIE MPAA", mpaa);

        cast = findCarouselInfo(response, "credits", "cast", RequestType.ACTOR);
        similar = findCarouselInfo(response, "similar", "results", type);



        return new Movie(name, poster, rating, mpaa, runtime, releaseDate, overview, genres, cast, similar);
    }

    private TVShow parseTVObject(JSONObject response) {
        String runTime, firstDate,  genres, network, origin,  numEpisodes, numSeason,overview, status, contentRatings;
        ResultHolder cast;
        ResultHolder similar;
        try {
            runTime = response.getJSONArray("episode_run_time").get(0).toString();
            if(runTime.isEmpty() || runTime.equals("null")){
                runTime = "Run Time is not available";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            runTime = "Run time is not available";
        }
        //Log.d("TV RUN", runTime);

        firstDate = findField(response, "first_air_date", "First airing not available");
        //Log.d("TV FIRST AIR", firstDate);

        genres = findGenres(response);
        //Log.d("TV GENREs", genres);

        network = findFirstArrayElement(response, "networks", "name", "No TV Network available");
        //Log.d("TV NETWORK", network);

        origin = findFirstArrayElement(response, "networks", "origin_country", "Don't have origin country");
        //Log.d("TV ORIGIN", origin);

        numEpisodes = findField(response, "number_of_episodes", "Number of seasons unavailable");
        //Log.d("TV EPISODES", numEpisodes);

        numSeason = findField(response, "number_of_seasons", "Number of seasons unavailable");
        //Log.d("TV SEASONs", numSeason);

        overview = findField(response, "overview", "No overview available");
        //Log.d("TV OVERVIEW", overview);

        status = findField(response, "status", "Don't have current status");
        //Log.d("TV STATUS", status);

        try {
            JSONObject cRatings = response.getJSONObject("content_ratings");
            contentRatings = findFirstArrayElement(cRatings,"results", "rating", "No Content Rating available");
        } catch (JSONException e) {
            e.printStackTrace();
            contentRatings = "No Content Rating available";
        }
        //Log.d("TV CONTENT", contentRatings);

        cast = findCarouselInfo(response, "credits", "cast", RequestType.ACTOR);
        similar = findCarouselInfo(response, "similar", "results", type);

        return new TVShow(name, poster, runTime, firstDate, genres, network, origin, numEpisodes, numSeason, overview, status, contentRatings, cast, similar);
    }

    private void showActor(Actor actor) {
        Intent intent = new Intent(this, InfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", actor);
        intent.putExtras(bundle);
        intent.putExtra("FORM", RequestType.ACTOR);
        finish();
        startActivity(intent);
    }

    private void showMovie(Movie movie) {
        Intent intent = new Intent(this, InfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", movie);
        intent.putExtras(bundle);
        intent.putExtra("FORM", RequestType.MOVIE);
        finish();
        startActivity(intent);
    }

    private void showTVShow(TVShow tv) {
        Intent intent = new Intent(this, InfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", tv);
        intent.putExtras(bundle);
        intent.putExtra("FORM", RequestType.TV);
        finish();
        startActivity(intent);
    }
}

