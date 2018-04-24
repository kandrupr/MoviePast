package pr.kandru.movieapp;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Activity to display individual Actor, Movie,
 */
public class InfoActivity extends AppCompatActivity implements FilmographyAdapter.onItemClicked, FilmographyAdapter.onItemPressed, ImageAdapter.onItemClicked{
    private RecyclerView topView, bottomView;               // RecyclerViews
    private GridLayoutManager layoutManagerBot, layoutManagerTop;
    private TextView title, bio, topText, bottomText;
    private FilmographyAdapter topAdapter, bottomAdapter;   // Two adapters
    private ImageAdapter bottomImageAdapter;                // If an actor is present, use ImageAdapter instead
    private InfoOverview data;                              // Parent class of Actor, Movie, TVShow
    private RequestType form;                               // Type Actor, Movie, or TVShow
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        Log.d("ACTIVITY", "RESUME");

        setContentView(R.layout.info_page_layout);
        title = findViewById(R.id.titleText);
        bio = findViewById(R.id.bioText);
        topView = findViewById(R.id.topCarousel);
        bottomView = findViewById(R.id.bottomCarousel);
        topText = findViewById(R.id.topText);
        bottomText = findViewById(R.id.bottomText);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            form = (RequestType) intent.getSerializableExtra("FORM");
            data = (InfoOverview) bundle.getSerializable("DATA");
        } else {
            finish();
            Toast.makeText(this, "Trouble loading information", Toast.LENGTH_SHORT).show();
        }
        // Allows overview text to be scrollable
        bio.setMovementMethod(new ScrollingMovementMethod());
        bio.setScrollbarFadingEnabled(false);

        switch (form) {
            case ACTOR:
                displayActor((Actor) data);
                break;
            case MOVIE:
                displayMovie((Movie) data);
                break;
            case TV:
                displayTVShow((TVShow) data);
                break;
            default:
                finish();
                Toast.makeText(this, "Couldn't load data", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Display everything about a TVShow
     * @param tv TV Object with attributes
     */
    private void displayTVShow(TVShow tv) {
        bottomText.setText(R.string.similarTV);
        title.setText(tv.getTitle());
        bio.setText(tv.getOverview());
        setPoster(tv.getPoster(), tv.getTitle());

        layoutManagerTop = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        topView.setLayoutManager(layoutManagerTop);
        ResultHolder cast = tv.getCast();
        topAdapter = new FilmographyAdapter(InfoActivity.this, cast);

        layoutManagerBot = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        bottomView.setLayoutManager(layoutManagerBot);
        ResultHolder similar = tv.getSimilar();
        bottomAdapter = new FilmographyAdapter(InfoActivity.this, similar);

        topView.setAdapter(topAdapter);
        bottomView.setAdapter(bottomAdapter);

        topAdapter.setOnClick(this);
        topAdapter.setOnPress(this);
        bottomAdapter.setOnClick(this);
        bottomAdapter.setOnPress(this);
    }

    /**
     * Display everything about a Movie
     * @param movie Movie Object with attributes
     */
    private void displayMovie(Movie movie) {
        title.setText(movie.getTitle());
        bio.setText(movie.getOverview());
        setPoster(movie.getPoster(), movie.getTitle());

        layoutManagerTop = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        topView.setLayoutManager(layoutManagerTop);
        ResultHolder cast = movie.getCast();
        topAdapter = new FilmographyAdapter(InfoActivity.this, cast);

        layoutManagerBot = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        bottomView.setLayoutManager(layoutManagerBot);
        ResultHolder similar = movie.getSimilar();
        bottomAdapter = new FilmographyAdapter(InfoActivity.this, similar);

        topView.setAdapter(topAdapter);
        bottomView.setAdapter(bottomAdapter);

        topAdapter.setOnClick(this);
        topAdapter.setOnPress(this);
        bottomAdapter.setOnClick(this);
        bottomAdapter.setOnPress(this);
    }

    /**
     * Display everything about an Actor
     * @param actor Actor Object with attributes
     */
    private void displayActor(Actor actor) {
        topText.setText(R.string.filmography);
        bottomText.setText(R.string.images);
        title.setText(actor.getName());
        bio.setText(actor.getBio());
        setPoster(actor.getPoster(), actor.getName());

        layoutManagerTop = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        topView.setLayoutManager(layoutManagerTop);
        ResultHolder holder = actor.getHolder();
        topAdapter = new FilmographyAdapter(InfoActivity.this, holder);

        layoutManagerBot = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        bottomView.setLayoutManager(layoutManagerBot);
        List<String> images = actor.getImages();
        bottomImageAdapter = new ImageAdapter(InfoActivity.this, images);

        topView.setAdapter(topAdapter);
        bottomView.setAdapter(bottomImageAdapter);

        topAdapter.setOnClick(this);
        topAdapter.setOnPress(this);
        bottomImageAdapter.setOnClick(this);
    }

    /**
     * Set and handle the poster
     * @param poster Poster URL
     * @param name   InfoOverView Name/Title
     */
    private void setPoster(String poster, final String name) {
        final TextView text = findViewById(R.id.info_resultText);
        final ImageView image = findViewById(R.id.info_posterView);
        final ProgressBar progress= findViewById(R.id.info_progressLoader);
        if(poster.equals("blank")) {        // No poster provided
            progress.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            text.setText(name);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        } else {
            Picasso.with(this)
                    .load(poster.replace("154", "185"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .into(image,  new Callback() {
                        @Override
                        public void onSuccess() {
                            if (progress != null) {             // Poster set successfully
                                text.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);
                                image.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError() {
                            if (progress != null) {             // Poster failed to load
                                progress.setVisibility(View.GONE);
                                image.setVisibility(View.GONE);
                                text.setVisibility(View.VISIBLE);
                                text.setText(name);
                                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                            }
                        }
                    });
        }
    }

    /**
     * Return back to MainActivity and delete all other activities
     * @param v View view that was clicked
     */
    public void onHomeButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(i);
    }

    /**
     * Move to a different InfoActivity based on the result
     * @param result Result Object holding TMDB info
     */
    @Override
    public void onItemClick(Result result) {
        Intent intent = new Intent(this, LoadingInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RESULT", result);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onTrimMemory(int level){
        // Determine which lifecycle or system event was raised.
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */
                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                break;
            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }

    }

    /**
     * Click on filmography items to Toast name/title
     * @param result Result Object holding TMDB info
     */
    @Override
    public void onItemPressed(Result result) {
        Toast toast = Toast.makeText(getApplicationContext(), result.getName(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0,0);
        toast.show();       // Display, name/Title
    }

    /**
     * Click on image items to show a dialog of that image
     * @param url String Poster URL
     */
    @Override
    public void onItemClick(String url) {
        ImageDialog dialog = new ImageDialog();
        dialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog_NoActionBar);
        dialog.setURL(url);
        dialog.show(this.getSupportFragmentManager(),"dialog");
    }
}
