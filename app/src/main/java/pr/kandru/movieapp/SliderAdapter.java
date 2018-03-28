package pr.kandru.movieapp;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by pkkan on 3/26/2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private int mCount = 0;
    final Handler h=new Handler();
    Runnable updateTask = null;


    public String[] headers = {
            "Profile",
            "Tap to Start",
            "Find",
            "About"
    };

    public String[] searchText = {
            "your favorite Movies",
            "your favorite TV Shows",
            "your favorite actors and actresses"
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
        TextView header;
        View view;
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
                header = (TextView) view.findViewById(R.id.searchHeader);
                header.setText(headers[position]);
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

    public void createHandler() {
        changeText();
        updateTask = new Runnable() {
            @Override
            public void run() {
                changeText();
                h.postDelayed(this,2000);
            }
        };
        h.postDelayed(updateTask,2000);
    }

    private void changeText(){
        final TextView info = ((MainActivity) context).findViewById(R.id.searchInfo);
        info.setText(searchText[mCount%3]);
        mCount++;

        info.startAnimation(AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left));
    }

    public void endHandler() {
        if(updateTask != null) {
            h.removeCallbacks(updateTask);
            updateTask = null;
        }
    }
}
