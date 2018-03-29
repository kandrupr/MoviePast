package pr.kandru.movieapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.JsonElement;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AIListener{

    private ViewPager slideViewPager;
    private LinearLayout dotLayout;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private SliderAdapter slider;
    private ImageButton listenButton;
    private TextView resultTextView;
    private AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideViewPager = (ViewPager) findViewById(R.id.slideLayout);
        dotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        slider = new SliderAdapter(this);
        slideViewPager.setAdapter(slider);
        slideViewPager.setCurrentItem(1);
        slideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(slideViewPager.getCurrentItem() == 2) {
                    slider.createHandler();
                } else {
                    slider.endHandler();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        listenButton = (ImageButton) findViewById(R.id.voiceButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        final AIConfiguration config = new AIConfiguration(getString(R.string.DialogFlowAPI),
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);
    }

    public void scrollToProfile(View v) { slideViewPager.setCurrentItem(0); }

    public void scrollToSearch(View v) {
        slideViewPager.setCurrentItem(2);
    }

    public void onSearchClick(View v) {
        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(i);
    }

    public void onStartListening(final View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }
        aiService.startListening();
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        resultTextView.setText("Query:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ){};

    }

    @Override
    public void onError(AIError error) {
        resultTextView.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
