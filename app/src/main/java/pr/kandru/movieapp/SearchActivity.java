package pr.kandru.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by pkkan on 3/28/2018.
 */

public class SearchActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_search);
    }

    public void onSearch(View v) {
        EditText text = this.findViewById(R.id.textInput);
        String result = text.getText().toString();

        if (!result.matches("")) {
            Intent intent = new Intent(getBaseContext(), Loading.class);
            intent.putExtra("QUERY", result);
            startActivity(intent);
            finish();
        }
    }
}
