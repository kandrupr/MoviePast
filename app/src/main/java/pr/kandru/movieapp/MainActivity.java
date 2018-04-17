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
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener{
    private ViewPager slideViewPager;
    private LinearLayout dotLayout;
    private ImageView[] dots;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private SliderAdapter mSlider;
    private ImageButton listenButton;
    //private TextView resultTextView;
    private AIService aiService;
    private DialogFlowParser mParser;
    //private String mState = "open";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideViewPager = (ViewPager) findViewById(R.id.slideLayout);
        dotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        mSlider = new SliderAdapter(this);
        slideViewPager.setAdapter(mSlider);
        slideViewPager.setCurrentItem(1);
        slideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(slideViewPager.getCurrentItem() == 2) {
                    mSlider.createHandler();
                } else {
                    mSlider.endHandler();
                }
                createDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        listenButton = (ImageButton) findViewById(R.id.voiceButton);
        //resultTextView = (TextView) findViewById(R.id.resultTextView);
        final AIConfiguration config = new AIConfiguration(getString(R.string.DialogFlowAPI),
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        createDots(1);
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
        //if(mState.equals("open")) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            }
            aiService.startListening();
        //}
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        mParser = new DialogFlowParser(getApplicationContext(), result);
        String intent = result.getMetadata().getIntentName().toString();
        String value = mParser.getURL();
        //mState = "open";
        Log.d("INTENT NAME ", result.getMetadata().getIntentName().toString());
        Log.d("INTENT INT ", result.getResolvedQuery());
        Log.d("INTENT PARAMS ", result.getParameters().toString());


        if(value.equals("fail")) {
            // TOAST FAIL
            Toast toast = Toast.makeText(getApplicationContext(), "Couldn't put your request together, try the typing it in!", Toast.LENGTH_LONG);
            toast.show();
        } else if(value.equals("invalid")) {
            Toast toast = Toast.makeText(getApplicationContext(), "That's an odd request. Try something else!", Toast.LENGTH_LONG);
            toast.show();
            // TOAST INVALID REQUEST
        } else {
            Intent i = new Intent(getApplicationContext(), LoadingAPIRequest.class);
            i.putExtra("URL", value);
            if(intent.equals("Movie") || intent.equals("MovieGenre")) {
                i.putExtra("TYPE", "movie");
                // Movie
            } else if(intent.equals("TVShows") || intent.equals(("TVShowGenre"))) {
                i.putExtra("TYPE", "tv");
                // TV
            } else if(intent.equals("Person")) {
                i.putExtra("TYPE", "actor");
                // Actor
            } else if(intent.equals("WithTitle")){
                if(result.getParameters().containsKey("Type")){
                    i.putExtra("TYPE", result.getParameters().get("Type").toString().replace("\"", "").toLowerCase());
                } else {
                    i.putExtra("TYPE", "multi");
                }
            } else {
                i.putExtra("TYPE", result.getParameters().get("Type").toString().replace("\"", "").toLowerCase());
                // DESCRIPTOR & DESCRIPTOR BY YEAR & PERSON FORM
            }
            startActivity(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ){
            //TODO: Toast and fix logic;
        }

    }

    private void createDots(int pos) {
        int size = mSlider.getHeaders().length;
        if(dotLayout != null) {
            dotLayout.removeAllViews();
        }

        dots = new ImageView[size];
        for(int i =0; i < size; i++) {
            dots[i] = new ImageView(this);

            if(i == pos) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            dotLayout.addView(dots[i],params);
        }
    }

    @Override
    public void onError(AIError error) {
        Toast toast = Toast.makeText(getApplicationContext(), "Didn't quite catch that. Try again!", Toast.LENGTH_LONG);
        toast.show();
        //mState = "open";
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        //mState = "listening";
/*
fading animation
final Animation in = new AlphaAnimation(0.0f, 1.0f);
in.setDuration(3000);

final Animation out = new AlphaAnimation(1.0f, 0.0f);
out.setDuration(3000);

AnimationSet as = new AnimationSet(true);
as.addAnimation(out);
in.setStartOffset(3000);
as.addAnimation(in);
*/
    }

    @Override
    public void onListeningCanceled() {
        //mState = "open";
    }

    @Override
    public void onListeningFinished() {
        //mState = "finished";
    }
}
