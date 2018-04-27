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
import android.view.animation.AnimationSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity implements AIListener{
    private ViewPager slideViewPager;
    private LinearLayout dotLayout;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private SliderAdapter mSlider;
    private AIService aiService;
    private AnimationSet as;
    private ViewPager.OnPageChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slideViewPager = findViewById(R.id.slideLayout);
        dotLayout = findViewById(R.id.dotsLayout);

        mSlider = new SliderAdapter(this);
        slideViewPager.setAdapter(mSlider);
        slideViewPager.setCurrentItem(1);
        createDots(1);
    }

    /**
     * Add listeners back
     */
    @Override
    protected void onResume() {
        super.onResume();
        final AIConfiguration config = new AIConfiguration(getString(R.string.DialogFlowAPI),
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                createDots(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        };
        slideViewPager.addOnPageChangeListener(listener);
        mSlider.setMainTitleText(R.string.tap);

    }

    /**
     * Remove listeners
     */
    @Override
    protected void onPause() {
        super.onPause();
        aiService = null;
        slideViewPager.removeOnPageChangeListener(listener);
    }

    public void scrollToProfile(View view) { slideViewPager.setCurrentItem(0); }

    public void scrollToSearch(View view) {
        slideViewPager.setCurrentItem(2);
    }

    /**
     * Have device start to listen
     * @param view Current view
     */
    public void onStartListening(final View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            permissionToRecordAccepted = true;
        }
        if(permissionToRecordAccepted) {
            aiService.startListening();
        } else {
            Toast.makeText(this, "Application needs permissions", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Response from Dialogflow/API.AI
     * @param response
     */
    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        DialogFlowParser mParser = new DialogFlowParser(getApplicationContext(), result);
        String intent = result.getMetadata().getIntentName();
        String query = result.getResolvedQuery();
        String value = mParser.getURL();
        //mState = "open";
        Log.d("INTENT NAME ", intent);
        Log.d("QUERY", query);
        Log.d("INTENT PARAMS ", result.getParameters().toString());

        Toast toast;
        switch (value) {
            case "fail": // TOAST FAIL
                toast = Toast.makeText(getApplicationContext(), "Couldn't put your request together, try the typing it in!", Toast.LENGTH_LONG);
                toast.show();
                mSlider.setMainTitleText(R.string.tap);
                break;
            case "invalid": // TOAST INVALID REQUEST
                toast = Toast.makeText(getApplicationContext(), "That's an odd request. Try something else!", Toast.LENGTH_LONG);
                toast.show();
                mSlider.setMainTitleText(R.string.tap);
                break;
            default:
                Intent i = new Intent(getApplicationContext(), LoadingAPIRequest.class);
                i.putExtra("QUERY", query);
                i.putExtra("URL", value);
                switch (intent) {
                    case "Movie":   // Movie
                    case "MovieGenre":
                        i.putExtra("TYPE", "movie");
                        break;
                    case "TVShows": // TV
                    case "TVShowGenre":
                        i.putExtra("TYPE", "tv");
                        break;
                    case "Person":  // Actor
                        i.putExtra("TYPE", "actor");
                        break;
                    case "PersonForm":  // Actor
                        i.putExtra("TYPE", "actorForm");
                        i.putExtra("FORM", result.getParameters().get("Type").toString().replace("\"", "").toLowerCase());
                        break;
                    case "WithTitle":   // Movie, TVShow, or ALL
                        if (result.getParameters().containsKey("Type"))
                            i.putExtra("TYPE", result.getParameters().get("Type").toString().replace("\"", "").toLowerCase());
                        else
                            i.putExtra("TYPE", "multi");
                        break;
                    default:    // DESCRIPTOR & DESCRIPTOR BY YEAR
                        i.putExtra("TYPE", result.getParameters().get("Type").toString().replace("\"", "").toLowerCase());
                        break;
                }
                startActivity(i);
                break;
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
    }

    /**
     * Create bottom dots on the screen used for scrolling
     * @param pos Page position in viewpager
     */
    private void createDots(int pos) {
        int size = mSlider.getCount();
        if(dotLayout != null) {
            dotLayout.removeAllViews();
        }

        ImageView[] dots = new ImageView[size];
        for(int i =0; i < size; i++) {
            dots[i] = new ImageView(this);

            if(i == pos) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots_drawable));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots_drawable));
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
        mSlider.setMainTitleText(R.string.tap);
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        //mSlider.setAnimation();
        mSlider.setMainTitleText(R.string.listening);
    }

    @Override
    public void onListeningCanceled() {
        //mSlider.clearAnimation();
        mSlider.setMainTitleText(R.string.tap);
    }

    @Override
    public void onListeningFinished() {
        //mSlider.clearAnimation();
        mSlider.setMainTitleText(R.string.processing);
    }

    /**
     * Handle clicking on any of the TextSearch options
     * @param v
     */
    public void onClickMedia(View v){
        String type = ((TextView)v).getText().toString();
        SearchEditText inputText = mSlider.setTextSearch(type);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void onClearInput(View v){
        mSlider.clearInput();
    }
}
