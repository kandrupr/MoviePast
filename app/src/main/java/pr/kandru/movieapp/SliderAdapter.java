package pr.kandru.movieapp;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Custom ViewPager Adapter
 */
public class SliderAdapter extends PagerAdapter {
    private Context context;
    //private int mCount = 0;
    //private final Handler h=new Handler();
    private Runnable updateTask;
    private ConstraintLayout searchBar, searchButtons;
    private TextView searchText;
    private SearchEditText inputText;

    private String[] headers = {
            "Profile",
            "Tap to Start",
            "Find",
            "About"
    };
/*
    private String[] searchText = {
            "your favorite Movies",
            "your favorite TV Shows",
            "your favorite actors and actresses"
    };*/

    /**
     * Constructor
     * @param context Application context
     */
    public SliderAdapter(Context context) {
        this.context = context;
        updateTask = null;
    }

    /**
     * Get number of Items in the View Pager
     * @return Integer size of view pager
     */
    @Override
    public int getCount() {
        return headers.length;
    }

    /**
     * Tells us if the current view is showing the correct object
     * @param view View being shown
     * @param object Object The cbject being shown on view
     * @return Boolean
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Create layout when it is in view
     * @param container ViewGroup The container holding all the views
     * @param position Integer The page we are looking at
     * @return Object The view of the current page
     */
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
            view = layoutInflater.inflate(R.layout.slide_search, container, false);
            if(searchText == null) {
                searchBar = view.findViewById(R.id.search_bar_layout);
                searchButtons = view.findViewById(R.id.search_button_layout);
                searchText = view.findViewById(R.id.textSearch);
                inputText = view.findViewById(R.id.textInput);
                inputText.setSiblings(view);
            }
            //header = view.findViewById(R.id.searchHeader);
            //header.setText(headers[position]);
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

    /**
     * Handle when a page in View pager is not available
     * @param container ViewGroup Contains all the views
     * @param position Integer Position according to ViewPager
     * @param object Object The page being destroyed
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
/*
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
    }*/

    /**
     * Get the headers for each page
     * @return String header of the certain page in viewpager
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * Sets up our Edit Text
     * @param type RequestType Actor, Movie, TV Show
     * @return SearchEditText Instance of the custom EditText
     */
    public SearchEditText setTextSearch(String type){
        searchButtons.setVisibility(INVISIBLE);
        searchText.setVisibility(INVISIBLE);
        ((TextView)searchBar.findViewById(R.id.textType)).setText(type);
        searchBar.setVisibility(VISIBLE);
        return inputText;
    }

    /**
     * Sets Edit Text to empty
     */
    public void clearInput() {
        ((TextView)searchBar.findViewById(R.id.textInput)).setText("");
    }
}
