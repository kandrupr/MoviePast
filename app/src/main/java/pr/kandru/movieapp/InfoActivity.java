package pr.kandru.movieapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.text.IDNA;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by pkkan on 4/19/2018.
 */

public class InfoActivity extends AppCompatActivity {
    RecyclerView bottomView;
    GridLayoutManager layoutManager;
    RequestType form;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actor_page_layout);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        form = (RequestType) intent.getSerializableExtra("FORM");
        switch(form) {
            case ACTOR:
                Actor actor = (Actor) bundle.getSerializable("DATA");
                displayActor(actor);
                break;
            case MOVIE:
                break;
            case TV:
                break;
            default:
                //FAIL AND TOAST
                break;
        }

        TextView tv = findViewById(R.id.bioText);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setScrollbarFadingEnabled(false);
    }

    private void displayActor(Actor actor) {
        TextView title = findViewById(R.id.requestText);
        TextView bio = findViewById(R.id.bioText);
        title.setText(actor.getName());
        bio.setText(actor.getBio());
        setPoster(actor.getPoster(), actor.getName());

        bottomView = findViewById(R.id.carouselBottom);
        layoutManager = new GridLayoutManager(InfoActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        bottomView.setLayoutManager(layoutManager);
        List<String> images = actor.getImages();

        ImageAdapter adapter = new ImageAdapter(InfoActivity.this, images);

        bottomView.setAdapter(adapter);
    }

    private void setPoster(String poster, final String name) {
        final TextView text = findViewById(R.id.resultText);
        final ImageView image = findViewById(R.id.posterView);
        final ProgressBar progress= findViewById(R.id.progressLoader);
        if(poster.equals("blank")) {
            progress.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            text.setText(name);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        } else {
            Picasso.with(this)
                    .load(poster)
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
}
