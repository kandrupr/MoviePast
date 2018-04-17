package pr.kandru.movieapp;

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
    TextView tv;
    TextView tv1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        Intent intent = getIntent();

        String url = intent.getStringExtra("URL");
        intentName = intent.getStringExtra("TYPE");

        TextView urlText = findViewById(R.id.testView);
        urlText.setText(url);
        Log.d("URL", url);

        TextView typeText = findViewById(R.id.testView1);
        typeText.setText(intentName);
        Log.d("URL 1", url);

        tv = findViewById(R.id.testView2);
        tv1 = findViewById(R.id.testView3);

        JsonObjectRequest jsonObjectRequest = createObject(url);
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private JsonObjectRequest createObject(String url) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        tv1.setText(response.toString());
                        if(intentName.equals("movie")){
                            parseData(response, Type.MOVIE);
                        } else if(intentName.equals("tv")) {
                            parseData(response, Type.TV);
                        } else if(intentName.equals("actor")) {
                            parseData(response, Type.ACTOR);
                        } else {
                            //parseMulti(response);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
    }

    private void parseData(JSONObject response, Type type) {
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
                    if (Integer.parseInt(obj.get("vote_count").toString()) >= 2) {
                        result = checkData(obj, type);
                        if(result != null){
                            results.add(result);
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

    private Result checkData(JSONObject obj, Type type) {
        String name;
        String id;
        String poster;
        try {
            if(type.equals(Type.MOVIE)) {
                name = obj.get("title").toString();
            } else {
                name = obj.get("name").toString();
            }
            id = obj.get("id").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        try {
           poster = obj.get("poster_path").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            poster = "blank";
        }

        return new Result(type, name, id, poster);
    }
}
