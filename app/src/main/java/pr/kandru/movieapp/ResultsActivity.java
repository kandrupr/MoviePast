package pr.kandru.movieapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pkkan on 3/28/2018.
 */

public class ResultsActivity extends AppCompatActivity implements ResultAdapter.onItemClicked, ResultAdapter.onItemPressed {
    private RecyclerView resultsView;
    private GridLayoutManager layoutManager;
    private String query;
    private ResultHolder results;
    private ResultAdapter adapter;

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

        results = (ResultHolder) bundle.getSerializable("RESULTS");
        resultsView = findViewById(R.id.resultView);
        layoutManager = new GridLayoutManager(ResultsActivity.this, 3);
        resultsView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ResultAdapter(ResultsActivity.this, results);
        resultsView.setAdapter(adapter);
        adapter.setOnClick(this);
        adapter.setOnPress(this);// Bind the listener
    }

    @Override
    protected void onPause() {
        super.onPause();
        resultsView.setAdapter(null);
        adapter = null;
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
