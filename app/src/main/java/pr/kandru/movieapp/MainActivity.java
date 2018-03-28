package pr.kandru.movieapp;

import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

    public void scrolltoProfile(View v) {
        slideViewPager.setCurrentItem(0);
    }

    public void scrolltoSearch(View v) {
        slideViewPager.setCurrentItem(2);
    }

}
