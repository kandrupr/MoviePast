package pr.kandru.movieapp;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pkkan on 3/28/2018.
 */

public class ResultsActivity extends AppCompatActivity {
    RecyclerView resultsView;
    GridLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);

        resultsView = findViewById(R.id.resultView);
        layoutManager = new GridLayoutManager(ResultsActivity.this, 3);
        resultsView.setLayoutManager(layoutManager);
        ArrayList<String> images = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();

        titles.add("ONE");
        titles.add("TWO");
        titles.add("THREE");
        titles.add("ONE");
        titles.add("TWO");
        titles.add("THREE");
        titles.add("ONE");
        titles.add("TWO");
        titles.add("THREE");
        images.add("blank");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");
        images.add("https://cnl.psy.msu.edu/wp-content/uploads/2018/04/han_photo.jpg");

        ResultAdapter adapter = new ResultAdapter(ResultsActivity.this, images, titles);

        resultsView.setAdapter(adapter);
    }
}
