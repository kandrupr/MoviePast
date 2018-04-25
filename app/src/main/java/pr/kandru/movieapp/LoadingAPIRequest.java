package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Load TMDB URL based off DialogFlow
 */
public class LoadingAPIRequest extends AppCompatActivity {
    private String intentName;
    private String query;
    private String form;
    private final BuildResult buildResult = new BuildResult();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        Intent intent = getIntent();

        String url = intent.getStringExtra("URL");
        intentName = intent.getStringExtra("TYPE");
        query = intent.getStringExtra("QUERY");
        if(intentName.equals("actorForm")) {
            form = intent.getStringExtra("FORM");
        }
        // Log.d("TYPE URL", url);
        // Log.d("TYPE INTENT", intentName);
        JsonObjectRequest jsonObjectRequest = createObject(url, getApplicationContext());
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * JSONObject of Volley Request
     * @param url String TMDB Search URL
     * @param c Context Activity Context
     * @return JSONObject of results
     */
    private JsonObjectRequest createObject(String url, final Context c) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        Log.d("RESPONSE INTENT", intentName);
                        switch (intentName) {
                            case "movie": // COMPLETE
                                parseData(response, RequestType.MOVIE);
                                break;
                            case "tv": // COMPLETE
                                parseData(response, RequestType.TV);
                                break;
                            case "actor": // COMPLETE
                                parseData(response, RequestType.ACTOR);
                                break;
                            case "actorForm": // COMPLETE
                                String id = getActorID(response);
                                getActorForm(c,id);
                                break;
                            default:
                                parseMulti(response); // COMPLETE
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
     * When given an actor and a form of media
     * @param url String TMDB Actor ID URL
     * @param c Application Context
     * @return JSONObject of actor credits
     */
    private JsonObjectRequest actorFormObject(String url, final Context c) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        if(form.equals("movie")) {
                            // Log.d("ACTOR FORM", response.toString());
                            parseActorCredits(response, RequestType.MOVIE);
                        } else {
                            // Log.d("ACTOR FORM", response.toString());
                            parseActorCredits(response, RequestType.TV);
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
     * Create Volley request for an actors credits
     * @param c Context Application Context
     * @param id String an Actor's TMDB ID
     */
    private void getActorForm(Context c, String id) {
        URLBuilder builder = URLBuilder.getInstance(c);
        String url = builder.buildPersonForm(id, form);
        JsonObjectRequest jsonObjectRequest = actorFormObject(url, this);
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Parse TMDB Search Results
     * @param response JSONObject results from TMDB search request
     * @param type RequestType Actor, Movie, TVShow
     */
    private void parseData(JSONObject response, RequestType type) {
        try {
            JSONArray jsonResults = response.getJSONArray("results");
            int size = jsonResults.length();
            if(size == 0){                  // No results
                finish();
                Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
            } else if(size == 1) {          // Single Result
                Result result = null;
                JSONObject obj = (JSONObject) jsonResults.get(0);
                if(type.equals(RequestType.ACTOR)) {    // Check if popularity or vote count meets minimum
                    if (Float.parseFloat(obj.get("popularity").toString()) >= 1.0) { result = buildResult.checkData(obj, type); }
                } else {
                    if (Integer.parseInt(obj.get("vote_count").toString()) >= 2) { result = buildResult.checkData(obj, type); }
                }
                if (result != null) {
                    singleResult(result);   // New InfoActivity
                } else {                    // After filtering, didn't make the cut
                    finish();
                    Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
                }
            } else {
                ResultHolder results = searchResults(jsonResults, type);
                if(results.size() == 0){    // No Results
                    finish();
                    Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
                } else if(results.size() == 1) {
                    singleResult(results.get(0));   // InfoActivity
                } else {
                    multipleResults(results, query); // ResultsActivity beacause of multiple results
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
            Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();        }
    }

    /**
     * Parse an actor's credits
     * @param response JSONObject TMDB search actor results
     * @param type  RequestType Actor, Movie, TVShow
     */
    private void parseActorCredits(JSONObject response, RequestType type) {
        try {
            JSONArray jsonResults = response.getJSONArray("cast");
            if(jsonResults.length() == 0){  // No results
                finish();
                Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
            } else {
                ResultHolder results = new ResultHolder();
                Result result;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = jsonResults.getJSONObject(i);
                    result = buildResult.checkData(obj, type);
                    if(result != null){ results.add(result); }
                }
                if(results.size() == 0) {
                    finish();           // No Results
                    Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
                } else {
                    multipleResults(results, query);    // RESULTS ACTIVITY
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
            Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();        }
    }

    /**
     * Parse Multi-Search data
     * @param response TMDB Search Results
     */
    private void parseMulti(JSONObject response) {
        try {
            JSONArray jsonResults = response.getJSONArray("results");
            if(jsonResults.length() == 0){
                finish();
                Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
            } else {
                ResultHolder results = new ResultHolder();
                Result result;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = jsonResults.getJSONObject(i);
                    result = buildResult.checkMultiData(obj);
                    if(result != null){ results.add(result); }
                }
                if(results.size() == 0) {           // No results
                    finish();
                    Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
                } else {
                    multipleResults(results, query); // Results Activity
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
            Toast.makeText(this, "Didn't find anything", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get the Actor ID from TMDB result
     * @param response TMDB Result
     * @return String TMDB ID
     */
    @Nullable
    private String getActorID(JSONObject response) {
        try {
            JSONArray jsonResults = response.getJSONArray("results");
            JSONObject obj = (JSONObject) jsonResults.get(0);
            return obj.get("id").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse through array of results to see if it made the cut
     * @param arr JSONArray Items to keep
     * @param type RequestType Actor, Movie, TVShow
     * @return ResultHolder Our Filtered Results
     */
    private ResultHolder searchResults(JSONArray arr, RequestType type) {
        ResultHolder results = new ResultHolder();
        Result result;
        try {
            if(type.equals(RequestType.ACTOR)){
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    if (Float.parseFloat(obj.get("popularity").toString()) >= 0.5) {
                        result = buildResult.checkData(obj, type);
                        if (result != null) { results.add(result); }
                    }
                }
            } else {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    if (Integer.parseInt(obj.get("vote_count").toString()) >= 2) {
                        result = buildResult.checkData(obj, type);
                        if (result != null) { results.add(result); }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Start new InfoActivity
     * @param result Result A single result
     */
    private void singleResult(Result result) {
        Intent intent = new Intent(this, LoadingInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RESULT", result);
        intent.putExtras(bundle);
        finish();
        startActivity(intent);
    }

    /**
     * Start new ResultsActivity
     * @param results ResultHolder results from parsing and filtering data
     * @param query String What the user was asking for
     */
    private void multipleResults(ResultHolder results, String query) {
        Intent intent = new Intent(this, ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RESULTS", results);
        intent.putExtras(bundle);
        intent.putExtra("QUERY", query);
        finish();
        startActivity(intent);
    }
}
