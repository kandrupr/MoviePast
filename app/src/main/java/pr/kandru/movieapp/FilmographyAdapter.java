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
     * @param results A list of results that contain a name/title, id, and poster url
     */
    public FilmographyAdapter(Context context, ResultHolder results) {
        super(context, results);
    }

    /**
     * Individual item in a view
     * @param parent RecyclerView
     * @param viewType
     * @return A layout version of ResultHolder
     */
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.width = parent.getMeasuredWidth() / 3;   // Get exactly 1/3
        layout.setLayoutParams(lp);

        return new Holder(layout);
    }
}
