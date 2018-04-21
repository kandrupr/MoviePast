package pr.kandru.movieapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by pkkan on 4/20/2018.
 */

public class FilmographyAdapter extends ResultAdapter {
    public FilmographyAdapter(Context context, ResultHolder results) {
        super(context, results);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.width = parent.getMeasuredWidth() / 3;
        layout.setLayoutParams(lp);

        return new Holder(layout);
    }
}
