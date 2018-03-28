package pr.kandru.movieapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import com.google.gson.JsonElement;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout dotLayout;

    private SliderAdapter slider;

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
    }

    public void scrollToProfile(View v) { slideViewPager.setCurrentItem(0); }

    public void scrollToSearch(View v) {
        slideViewPager.setCurrentItem(2);
    }

    public void onSearchClick(View v) {
        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(i);
    }
}
