package pr.kandru.movieapp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Custom ViewPager Adapter
 */
public class SliderAdapter extends PagerAdapter {
    private final Context context;
    private ConstraintLayout searchBar, searchButtons;
    private TextView searchText;
    private SearchEditText inputText;
    private AnimationSet as;
    private TextView mainTitle;

    private final int[] layouts = new int[]{
            /*R.layout.slide_history,*/ R.layout.slide_main, R.layout.slide_search, R.layout.slide_about
    };

    /**
     * Constructor
     * @param context Application context
     */
    SliderAdapter(Context context) {
        this.context = context;
        // Animation
    }

    /**
     * Get number of Items in the View Pager
     * @return Integer size of view pager
     */
    @Override
    public int getCount() {
        return layouts.length;
    }

    /**
     * Tells us if the current view is showing the correct object
     * @param view View being shown
     * @param object Object The object being shown on view
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
        System.out.println("INSTANITATE");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layouts[position], container, false);
        if(position == 0) {
//            RecyclerView historyView = view.findViewById(R.id.historyView);
//            GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
//            historyView.setLayoutManager(layoutManager);
//
//            HistoryAdapter adapter = new HistoryAdapter(context);
//            historyView.setAdapter(adapter);
            mainTitle = view.findViewById(R.id.textCommand);
//        } else if(position == 1){
//          mainTitle = view.findViewById(R.id.textCommand);
        } else if(searchText == null && position == 1) {
            searchBar = view.findViewById(R.id.search_bar_layout);
            searchButtons = view.findViewById(R.id.search_button_layout);
            searchText = view.findViewById(R.id.textSearch);
            inputText = view.findViewById(R.id.textInput);
            inputText.setSiblings(view);
        }
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

    /**
     * Sets up the layout for out text request
     * @param type RequestType Actor, Movie, TV Show
     * @return SearchEditText Instance of the custom EditText
     */
    SearchEditText setTextSearch(String type){
        searchButtons.setVisibility(INVISIBLE);
        searchText.setVisibility(INVISIBLE);
        ((TextView)searchBar.findViewById(R.id.textType)).setText(type);
        searchBar.setVisibility(VISIBLE);
        searchButtons.invalidate();
        searchText.invalidate();
        searchBar.invalidate();
        return inputText;
    }

    void setMainTitleText(int string) {
        if(mainTitle != null) {
            mainTitle.setText(string);
        }
    }

    void setAnimation() {
        //mainTitle.setAnimation(as);
    }

    void clearAnimation() {
        //mainTitle.clearAnimation();
    }

    /**
     * Sets Edit Text to empty
     */
    void clearInput() {
        ((TextView)searchBar.findViewById(R.id.textInput)).setText("");
    }
}
