package pr.kandru.movieapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A child of ResultAdapter used in the InfoActivity
 */
public class FilmographyAdapter extends ResultAdapter {
    /**
     * Constructor
     * @param context Application Context
     * @param results ResultHolder A list of results that contain a name/title, id, and poster url
     */
    public FilmographyAdapter(Context context, ResultHolder results) {
        super(context, results);
    }

    /**
     * Creates Holder Item to Parent RecyclerView
     * @param parent ViewGroup The RecyclerView which holds all of the Holder items
     * @param viewType Integer The position in the Adapter data
     * @return Holder object
     */
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.width = parent.getMeasuredWidth() / 3;   // Get exactly 1/3 to get 3 results on a page
        layout.setLayoutParams(lp);

        return new Holder(layout);
    }
}
