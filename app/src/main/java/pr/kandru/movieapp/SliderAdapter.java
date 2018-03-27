package pr.kandru.movieapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pkkan on 3/26/2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public String[] headers = {
            "Profile",
            "Tap to Start",
            "Find",
            "About"
    };

    public SliderAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return headers.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view;
        TextView header;
        switch(position) {
            case 0:
                view = layoutInflater.inflate(R.layout.slide_main, container, false);
                header = (TextView) view.findViewById(R.id.textCommand);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.slide_main, container, false);
                header = (TextView) view.findViewById(R.id.textCommand);

                break;
            case 2:
                view = layoutInflater.inflate(R.layout.slide_search, container, false);
                //header = (TextView) view.findViewById(R.id.textCommand);

                break;
            case 3:
                view = layoutInflater.inflate(R.layout.slide_main, container, false);
                header = (TextView) view.findViewById(R.id.textCommand);

                break;
            default:
                view = layoutInflater.inflate(R.layout.slide_main, container, false);
                header = (TextView) view.findViewById(R.id.textCommand);
                break;
        }
        //header.setText(headers[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
