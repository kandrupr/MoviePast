package pr.kandru.movieapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by pkkan on 4/19/2018.
 */

public class InfoActivity extends AppCompatActivity implements FilmographyAdapter.onItemClicked{
    RecyclerView topView;
    RecyclerView bottomView;
    GridLayoutManager layoutManagerBot;
    GridLayoutManager layoutManagerTop;
    TextView title;
    TextView bio;
    TextView topText;
    TextView bottomText;

    RequestType form;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.info_page_layout);
        title = findViewById(R.id.titleText);
        bio = findViewById(R.id.bioText);
        topView = findViewById(R.id.topCarousel);
        bottomView = findViewById(R.id.bottomCarousel);
        topText = findViewById(R.id.topText);
        bottomText = findViewById(R.id.bottomText);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        form = (RequestType) intent.getSerializableExtra("FORM");
        switch(form) {
            case ACTOR:
                Actor actor = (Actor) bundle.getSerializable("DATA");
                displayActor(actor);
                break;
            case MOVIE:
                Movie movie = (Movie) bundle.getSerializable("DATA");
                displayMovie(movie);
                break;
            case TV:
                TVShow tv = (TVShow) bundle.getSerializable("DATA");
                displayTVShow(tv);
                break;
            default:
                //FAIL AND TOAST
                break;
        }

        bio.setMovementMethod(new ScrollingMovementMethod());
        bio.setScrollbarFadingEnabled(false);
    }

    private void displayTVShow(TVShow tv) {
        bottomText.setText(R.string.similarTV);
        title.setText(tv.getTitle());
        bio.setText(tv.getOverview());
        setPoster(tv.getPoster(), tv.getTitle());

        layoutManagerTop = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        topView.setLayoutManager(layoutManagerTop);
        ResultHolder cast = tv.getCast();
        FilmographyAdapter topAdapter = new FilmographyAdapter(InfoActivity.this, cast);

        layoutManagerBot = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        bottomView.setLayoutManager(layoutManagerBot);
        ResultHolder similar = tv.getSimilar();
        FilmographyAdapter bottomAdapter = new FilmographyAdapter(InfoActivity.this, similar);

        topView.setAdapter(topAdapter);
        bottomView.setAdapter(bottomAdapter);

        topAdapter.setOnClick(this);
        bottomAdapter.setOnClick(this);
    }

    private void displayMovie(Movie movie) {
        title.setText(movie.getTitle());
        bio.setText(movie.getOverview());
        setPoster(movie.getPoster(), movie.getTitle());

        layoutManagerTop = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        topView.setLayoutManager(layoutManagerTop);
        ResultHolder cast = movie.getCast();
        FilmographyAdapter topAdapter = new FilmographyAdapter(InfoActivity.this, cast);

        layoutManagerBot = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        bottomView.setLayoutManager(layoutManagerBot);
        ResultHolder similar = movie.getSimilar();
        FilmographyAdapter bottomAdapter = new FilmographyAdapter(InfoActivity.this, similar);

        topView.setAdapter(topAdapter);
        bottomView.setAdapter(bottomAdapter);

        topAdapter.setOnClick(this);
        bottomAdapter.setOnClick(this);
    }

    private void displayActor(Actor actor) {
        topText.setText(R.string.filmography);
        bottomText.setText(R.string.images);
        title.setText(actor.getName());
        bio.setText(actor.getBio());
        setPoster(actor.getPoster(), actor.getName());

        layoutManagerTop = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        topView.setLayoutManager(layoutManagerTop);
        ResultHolder holder = actor.getHolder();
        FilmographyAdapter topAdapter = new FilmographyAdapter(InfoActivity.this, holder);

        layoutManagerBot = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        bottomView.setLayoutManager(layoutManagerBot);
        List<String> images = actor.getImages();
        ImageAdapter bottomAdapter = new ImageAdapter(InfoActivity.this, images);

        topView.setAdapter(topAdapter);
        bottomView.setAdapter(bottomAdapter);

        topAdapter.setOnClick(this);
    }

    private void setPoster(String poster, final String name) {
        final TextView text = findViewById(R.id.info_resultText);
        final ImageView image = findViewById(R.id.info_posterView);
        final ProgressBar progress= findViewById(R.id.info_progressLoader);
        if(poster.equals("blank")) {
            progress.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            text.setText(name);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        } else {
            Picasso.with(this)
                    .load(poster)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(image,  new Callback() {
                        @Override
                        public void onSuccess() {
                            if (progress != null) {
                                text.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);
                                image.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError() {
                            if (progress != null) {
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

    public void onHomeButton(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(i);
    }

    @Override
    public void onItemClick(Result result) {
        Intent intent = new Intent(this, LoadingInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RESULT", result);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
