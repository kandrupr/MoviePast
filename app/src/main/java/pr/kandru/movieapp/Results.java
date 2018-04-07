package pr.kandru.movieapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by pkkan on 3/28/2018.
 */

public class Results extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);

        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=c67800592cc9f12da208901fb31247fd&language=en-US&page=1";

        JsonObjectRequest jsonObjectRequest = createObject(url);
// Access the RequestQueue through your singleton class.
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private JsonObjectRequest createObject(String url) {
        return new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("RESPONSE", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
    }
}
