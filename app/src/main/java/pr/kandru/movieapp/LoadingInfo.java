package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        tv = findViewById(R.id.testView);
        //setContentView(R.layout.loading_layout);
        builder = new URLBuilder(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Result result = (Result) bundle.getSerializable("RESULT");
        Log.d("RESULT INFO ACTIVITY", result.toString());
        name = result.getName();
        type = result.getType();
        id = result.getId();
        poster = result.getPoster();

        switch(type) {
            case ACTOR:
                getActorInfo();
                break;
            case MOVIE:
                break;
            case TV:
                break;
            default:
                // FAIL AND TOAST;
                break;
        }
    }

    private void getActorInfo() {
        String url = builder.buildActorInfo(id);
        JsonObjectRequest jsonObjectRequest = createActorObject(url, this);
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private JsonObjectRequest createActorObject(String url, final Context c) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        Actor actor = parseActorObject(response);
                        showActor(actor);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("FAILED", "FAILED");
                    }
                });
    }

    private Actor parseActorObject(JSONObject response) {
        List<String> images = new ArrayList<>();
        ResultHolder holder = new ResultHolder();
        String biography;
        try {
            JSONArray arr = response.getJSONObject("images").getJSONArray("profiles");
            int size = arr.length();
            if(size > 10) { size = 10; }
            for (int i = 0; i < size; i++) {
                JSONObject index = arr.getJSONObject(i);
                if(!index.equals("null")) {
                    images.add(image_url + index.get("file_path").toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // NO IMAGES THATS OKAY
        }

        try {
            JSONArray arr = response.getJSONObject("combined_credits").getJSONArray("cast");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Result result = buildResult.checkMultiData(obj);
                if(result != null) {
                    holder.add(result);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // NO FILM THATS OKAY
        }

        try {
            biography = response.get("biography").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            // NO FILM THATS OKAY
            biography = "blank";
        }
        return new Actor(name, biography, poster, images, holder);
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

}

