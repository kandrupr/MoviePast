package pr.kandru.movieapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pkkan on 4/20/2018.
 */

public class FilmographyAdapter extends ResultAdapter {
    public FilmographyAdapter(Context context, List<String> images, List<String> titles) {
        super(context, images, titles);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.width = parent.getMeasuredWidth() / 3;
        layout.setLayoutParams(lp);

        Holder holder = new Holder(layout);
        return holder;
    }
}
