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
import com.google.gson.JsonObject;

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
                            parseMovieData(response);
                        } else if(intentName.equals("tv")) {

                        } else if(intentName.equals("actor")) {

                        } else {

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
    }

    private void parseMovieData(JSONObject response) {
        try {
            //JSONArray jsonResults = response.getJSONArray("results");
            JSONArray jsonResults = new JSONArray("[]");
            JSONArray j= new JSONArray("[1,1,1,1]");


            if(jsonResults.length() == 0){
                Log.d("ARRAY TITLE", Integer.toString(jsonResults.length()));
                Log.d("ARRAY TITLE", Integer.toString(j.length()));

            } else {
                for (int i = 0; i < jsonResults.length(); i++) {
                    JSONObject obj = (JSONObject) jsonResults.get(i);
                    if (Integer.parseInt(obj.get("vote_count").toString()) > 4) {
                        Log.d("ARRAY TITLE", obj.get("title").toString());
                        Log.d("ARRAY ID", obj.get("id").toString());
                        Log.d("ARRAY POSTER", obj.get("poster_path").toString());
                    }


                    //obj.
                    Log.d("ARRAY", obj.toString());
                }
            }
            tv.setText(jsonResults.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            tv.setText("FAILED");
        }
    }
}
