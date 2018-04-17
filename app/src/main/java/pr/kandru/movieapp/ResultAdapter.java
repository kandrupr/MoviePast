package pr.kandru.movieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by pkkan on 4/9/2018.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.Holder> {
    private Context context;
    private ArrayList<String> images;
    private ArrayList<String> titles;
    private int mHeight;

    public ResultAdapter(Context context, ArrayList<String> images, ArrayList<String> titles) {
        this.context = context;
        this.images = images;
        this.titles = titles;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 3;
        layout.setLayoutParams(lp);

        Holder holder = new Holder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if(images.get(position).equals("blank")) {
            holder.image.setVisibility(View.INVISIBLE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(titles.get(position));
            holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        } else {
            Picasso.with(context).load(images.get(position)).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        public Holder (View view) {
            super(view);
            image = (ImageView) itemView.findViewById(R.id.resultImage);
            text = (TextView) itemView.findViewById(R.id.resultText);
        }
    }
}
