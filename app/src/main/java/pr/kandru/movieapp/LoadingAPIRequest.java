package pr.kandru.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by pkkan on 3/31/2018.
 */

public class LoadingAPIRequest extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        Intent intent = getIntent();

        String url = intent.getStringExtra("URL");
        String type = intent.getStringExtra("TYPE");

        TextView urlText = findViewById(R.id.testView);
        urlText.setText(url);
        Log.d("URL", url);

        TextView typeText = findViewById(R.id.testView1);
        typeText.setText(type);
        Log.d("URL 1", url);
    }
}
