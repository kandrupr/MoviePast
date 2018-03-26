package pr.kandru.movieapp;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout dotLayout;

    private SliderAdapter slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideViewPager = (ViewPager) findViewById(R.id.slideLayout);
        dotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        slider = new SliderAdapter(this);
        slideViewPager.setAdapter(slider);
        slideViewPager.setCurrentItem(1);
    }

}
