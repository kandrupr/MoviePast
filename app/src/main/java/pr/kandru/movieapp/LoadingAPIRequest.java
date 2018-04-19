package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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
    TextView tv;
    TextView tv1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        Intent intent = getIntent();

        String url = intent.getStringExtra("URL");
        intentName = intent.getStringExtra("TYPE");
        query = intent.getStringExtra("QUERY");
        if(intentName.equals("actorForm")) {
            form = intent.getStringExtra("FORM");
            Log.d("FORM", form);
        }

        TextView urlText = findViewById(R.id.testView);
        urlText.setText(url);
        Log.d("TYPE URL", url);

        TextView typeText = findViewById(R.id.testView1);
        typeText.setText(intentName);
        Log.d("TYPE INTENT", intentName);

        tv = findViewById(R.id.testView2);
        tv1 = findViewById(R.id.testView3);

        JsonObjectRequest jsonObjectRequest = createObject(url, this);
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private JsonObjectRequest createObject(String url, final Context c) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        tv1.setText(response.toString());
                        Log.d("TYPE NAME", intentName);
                        if(intentName.equals("movie")){
                            parseData(response, RequestType.MOVIE);
                            // COMPLETE
                        } else if(intentName.equals("tv")) {
                            parseData(response, RequestType.TV);
                            // COMPLETE
                        } else if(intentName.equals("actor")) {
                            parseData(response, RequestType.ACTOR);
                        } else if(intentName.equals("actorForm")){
                            String id = getActorID(response);
                            getActorForm(c,id);
                            // Send another request
                            // COMPLETE
                        } else {
                            Log.d("MULTI STATEMENT", "OKAY");
                            parseMulti(response);
                            // COMPLETE
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
    }

    private JsonObjectRequest actorFormObject(String url) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        if(form.equals("movie")) {
                            Log.d("ACTOR FORM", response.toString());
                            parseActorCredits(response, RequestType.MOVIE);
                        } else {
                            Log.d("ACTOR FORM", response.toString());
                            parseActorCredits(response, RequestType.TV);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
    }

    private void getActorForm(Context c, String id) {
        URLBuilder builder = new URLBuilder(c);
        String url = builder.buildPersonFrom(id, form);
        JsonObjectRequest jsonObjectRequest = actorFormObject(url);
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void parseData(JSONObject response, RequestType type) {
        try {
            JSONArray jsonResults = response.getJSONArray("results");

            if(jsonResults.length() == 0){
                // FINISH AND TOAST NO RESULTS
            } else if(jsonResults.length() == 1) {
                Result result;
                JSONObject obj = (JSONObject) jsonResults.get(0);
                if (Integer.parseInt(obj.get("vote_count").toString()) >= 2) {
                    result = checkData(obj, type);
                    if(result != null){
                        // NEW ACTIVITY WITH ITEM
                    } else {
                        // RETURN AND TOAST
                    }
                }
            } else {
                Log.d("ARRAY", Integer.toString(jsonResults.length()));
                ResultHolder results = new ResultHolder();
                Result result;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = (JSONObject) jsonResults.get(i);
                    if(type.equals(RequestType.ACTOR)){
                        if (Float.parseFloat(obj.get("popularity").toString()) >= 1.0) {
                            result = checkData(obj, type);
                            if (result != null) {
                                results.add(result);
                            }
                        }
                    } else {
                        if (Integer.parseInt(obj.get("vote_count").toString()) >= 2) {
                            result = checkData(obj, type);
                            if (result != null) {
                                results.add(result);
                            }
                        }
                    }
                }
                if(results.size() == 0){
                    // FINISH AND TOAST
                } else if(results.size() == 1) {
                    // TYPE Activity
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
            //tv.setText(jsonResults.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            //tv.setText("FAILED");
            // TOAST AND FAIL
        }
    }

    private void parseActorCredits(JSONObject response, RequestType type) {
        try {
            JSONArray jsonResults = response.getJSONArray("cast");
            if(jsonResults.length() == 0){
                // FINISH AND TOAST NO RESULTS
            } else {
                Log.d("ARRAY", Integer.toString(jsonResults.length()));
                ResultHolder results = new ResultHolder();
                Result result;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = (JSONObject) jsonResults.get(i);
                    result = checkData(obj, type);
                    if(result != null){
                        results.add(result);
                    }
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
                Log.d("ARRAY", Integer.toString(jsonResults.length()));
                ResultHolder results = new ResultHolder();
                Result result;
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = (JSONObject) jsonResults.get(i);
                    result = checkMultiData(obj);
                    if(result != null){
                        results.add(result);
                    }
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

    private Result checkData(JSONObject obj, RequestType type) {
        String name;
        String id;
        String poster;
        try {
            if(type.equals(RequestType.MOVIE)) {
                name = obj.get("title").toString();
            } else {
                name = obj.get("name").toString();
            }
            id = obj.get("id").toString();
            if(name.equals("null") || id.equals("null")) { return null; }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        try {
            if(type.equals(RequestType.ACTOR)){
                poster = obj.get("profile_path").toString();
                Log.d("PROFILE PATH",poster);
            } else {
                poster = obj.get("poster_path").toString();
            }
            if(poster.equals("null")){ return null; }
        } catch (JSONException e) {
            e.printStackTrace();
            // poster = "blank";
            return null;
        }

        return new Result(type, name, id, poster);
    }

    private Result checkMultiData(JSONObject obj) {
        String name, id, poster, media;
        RequestType type;

        try {
            media = obj.get("media_type").toString();
            switch(media) {
                case "movie":
                    if(Integer.parseInt(obj.get("vote_count").toString()) <= 2){ return null;}
                    name = obj.get("title").toString();
                    type = RequestType.MOVIE;
                    break;
                case "tv":
                    if(Integer.parseInt(obj.get("vote_count").toString()) <= 2){ return null;}
                    name = obj.get("name").toString();
                    type = RequestType.TV;
                    break;
                case "person":
                    if(Float.parseFloat(obj.get("popularity").toString()) <= 1.0){ return null; }
                    name = obj.get("name").toString();
                    type = RequestType.ACTOR;
                    break;
                default:
                    return null;
            }
            id = obj.get("id").toString();
            if(name.equals("null") || id.equals("null")) { return null;}
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        try {
            poster = obj.get("poster_path").toString();
            if(poster.equals("null")){ return null; }
        } catch (JSONException e) {
            e.printStackTrace();
            // poster = "blank";
            return null;
        }

        return new Result(type, name, id, poster);
    }
}
