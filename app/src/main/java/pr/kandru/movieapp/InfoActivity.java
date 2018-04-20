package pr.kandru.movieapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by pkkan on 4/19/2018.
 */

public class InfoActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_page_layout);

        TextView tv = findViewById(R.id.overviewText);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setScrollbarFadingEnabled(false);
    }
}
