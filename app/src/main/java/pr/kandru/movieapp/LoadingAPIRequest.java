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
 * Created by pkkan on 3/31/2018.
 */

public class LoadingAPIRequest extends AppCompatActivity {
    private String intentName;
    private String query;
    private String form;
    private BuildResult buildResult = new BuildResult();

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
            Log.d("FORM", form);
        }

        Log.d("TYPE URL", url);
        Log.d("TYPE INTENT", intentName);
        JsonObjectRequest jsonObjectRequest = createObject(url, getApplicationContext());
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

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
                        Toast.makeText(c, "Network Error", Toast.LENGTH_LONG).show();
                    }
                });
    }

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
                        Toast.makeText(c, "Network Error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getActorForm(Context c, String id) {
        URLBuilder builder = URLBuilder.getInstance(c);
        String url = builder.buildPersonFrom(id, form);
        JsonObjectRequest jsonObjectRequest = actorFormObject(url, this);
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void parseData(JSONObject response, RequestType type) {
        try {
            JSONArray jsonResults = response.getJSONArray("results");
            int size = jsonResults.length();
            if(size == 0){
                // FINISH AND TOAST NO RESULTS
            } else if(size == 1) {
                Result result = null;
                JSONObject obj = (JSONObject) jsonResults.get(0);
                if(type.equals(RequestType.ACTOR)) {
                    if (Float.parseFloat(obj.get("popularity").toString()) >= 1.0) { result = buildResult.checkData(obj, type); }
                } else {
                    if (Integer.parseInt(obj.get("vote_count").toString()) >= 2) { result = buildResult.checkData(obj, type); }
                }
                if (result != null) {
                    // NEW ACTIVITY WITH
                    Intent intent = new Intent(this, LoadingInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RESULT", result);
                    intent.putExtras(bundle);
                    finish();
                    startActivity(intent);
                } else {
                    // RETURN AND TOAST
                }
            } else {
                ResultHolder results = searchResults(jsonResults, type);
                if(results.size() == 0){
                    // FINISH AND TOAST
                } else if(results.size() == 1) {
                    // TYPE Activity
                    Intent intent = new Intent(this, LoadingInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RESULT", results.get(0));
                    intent.putExtras(bundle);
                    finish();
                    startActivity(intent);
                } else {
                    // RESULTS ACTIVITY
                    Intent intent = new Intent(this, ResultsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RESULTS", results);
                    intent.putExtras(bundle);
                    intent.putExtra("QUERY", query);
                    finish();
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // TOAST AND FAIL
        }
    }

    private void parseActorCredits(JSONObject response, RequestType type) {
        try {
            JSONArray jsonResults = response.getJSONArray("cast");
            if(jsonResults.length() == 0){
                // FINISH AND TOAST NO RESULTS
            } else {
                ResultHolder results = new ResultHolder();
                Result result;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = jsonResults.getJSONObject(i);
                    result = buildResult.checkData(obj, type);
                    if(result != null){ results.add(result); }
                }
                if(results.size() == 0) {
                    // END ACTIVITY AND TOAST
                } else {
                    // RESULTS ACTIVITY
                    Intent intent = new Intent(this, ResultsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RESULTS", results);
                    intent.putExtras(bundle);
                    intent.putExtra("QUERY", query);
                    finish();
                    startActivity(intent);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            // TOAST AND FAIL
        }
    }

    private void parseMulti(JSONObject response) {
        try {
            JSONArray jsonResults = response.getJSONArray("results");
            if(jsonResults.length() == 0){
                // FINISH AND TOAST NO RESULTS
            } else {
                ResultHolder results = new ResultHolder();
                Result result;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = jsonResults.getJSONObject(i);
                    result = buildResult.checkMultiData(obj);
                    if(result != null){ results.add(result); }
                }
                if(results.size() == 0) {
                    // TOAST AND FAIL
                } else {
                    // RESULTS ACTIVITY
                    Intent intent = new Intent(this, ResultsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RESULTS", results);
                    intent.putExtras(bundle);
                    intent.putExtra("QUERY", query);
                    finish();
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // TOAST AND FAIL
        }
    }

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

    private ResultHolder searchResults(JSONArray arr, RequestType type) {
        ResultHolder results = new ResultHolder();
        Result result = null;
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
}
