package pr.kandru.movieapp;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by pkkan on 3/26/2018.
 */

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private int mCount = 0;
    private final Handler h=new Handler();
    private Runnable updateTask;

    private String[] headers = {
            "Profile",
            "Tap to Start",
            "Find",
            "About"
    };

    private String[] searchText = {
            "your favorite Movies",
            "your favorite TV Shows",
            "your favorite actors and actresses"
    };

    public SliderAdapter(Context context) {
        this.context = context;
        updateTask = null;
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
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView header;
        View view;
        if (position == 0) {
            view = layoutInflater.inflate(R.layout.slide_main, container, false);
            // header = (TextView) view.findViewById(R.id.textCommand);
        } else if (position == 1) {
            view = layoutInflater.inflate(R.layout.slide_main, container, false);
            header = view.findViewById(R.id.textCommand);

        } else if (position == 2) {
            view = layoutInflater.inflate(R.layout.slide_find, container, false);
            header = view.findViewById(R.id.searchHeader);
            header.setText(headers[position]);

        } else if (position == 3) {
            view = layoutInflater.inflate(R.layout.slide_main, container, false);
            header = view.findViewById(R.id.textCommand);

        } else {
            view = layoutInflater.inflate(R.layout.slide_main, container, false);
            header = view.findViewById(R.id.textCommand);

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
        if (info != null) {
            info.setText(searchText[mCount % 3]);
            mCount++;
            info.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
        }
    }

    public void endHandler() {
        if(updateTask != null) {
            h.removeCallbacks(updateTask);
            updateTask = null;
        }
    }

    public String[] getHeaders() {
        return headers;
    }
}
