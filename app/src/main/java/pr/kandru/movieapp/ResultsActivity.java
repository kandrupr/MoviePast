package pr.kandru.movieapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that displays results as a grid
 */
public class ResultsActivity extends AppCompatActivity implements ResultAdapter.onItemClicked, ResultAdapter.onItemPressed {
    private RecyclerView resultsView;           // Main View
    private GridLayoutManager layoutManager;
    private String query;                       // User query
    private ResultHolder results;               // Results
    private ResultAdapter adapter;              // RecyclerView adapter

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

        if (bundle != null) {
            results = (ResultHolder) bundle.getSerializable("RESULTS");
        } else {
            finish();
            Toast.makeText(this, "Couldn't get results", Toast.LENGTH_SHORT).show();
        }
        resultsView = findViewById(R.id.resultView);
        layoutManager = new GridLayoutManager(ResultsActivity.this, 3);
        resultsView.setLayoutManager(layoutManager);

        adapter = new ResultAdapter(ResultsActivity.this, results);
        resultsView.setAdapter(adapter);

        adapter.setOnClick(this);
        adapter.setOnPress(this);// Bind the listener
    }

    @Override
    public void onItemClick(Result result) {
        Intent intent = new Intent(this, LoadingInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RESULT", result);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onHomeButton(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    @Override
    public void onItemPressed(Result result) {
        Toast toast = Toast.makeText(getApplicationContext(), result.getName(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}
