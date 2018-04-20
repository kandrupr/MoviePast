package pr.kandru.movieapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.List;

/**
 * Created by pkkan on 3/28/2018.
 */

public class ResultsActivity extends AppCompatActivity {
    RecyclerView resultsView;
    GridLayoutManager layoutManager;
    String query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        query = intent.getStringExtra("QUERY");
        TextView queryText = findViewById(R.id.requestText);
        queryText.setText(query.toUpperCase());

        ResultHolder results = (ResultHolder) bundle.getSerializable("RESULTS");
        Log.d("TITLES", results.getNames().toString());


        resultsView = findViewById(R.id.resultView);
        layoutManager = new GridLayoutManager(ResultsActivity.this, 3);
        resultsView.setLayoutManager(layoutManager);
        List<String> images = results.getImages();
        List<String> titles = results.getNames();

        ResultAdapter adapter = new ResultAdapter(ResultsActivity.this, images, titles);

        resultsView.setAdapter(adapter);
    }

    public void onHomeButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(i);
    }
}
