package pr.kandru.movieapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pkkan on 3/28/2018.
 */

public class Results extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);
    }
}
