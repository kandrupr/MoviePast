package pr.kandru.movieapp;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pkkan on 4/20/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {

    private Context context;
    private List<String> images;

    public ImageAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.width = parent.getMeasuredWidth() / 3;
        layout.setLayoutParams(lp);
        ImageAdapter.Holder holder = new ImageAdapter.Holder(layout);
        return holder;
        //return null;
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.Holder holder, int position) {
        if(images.get(position).equals("blank")) {
            holder.progress.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText("Error");
            holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        } else {
            Picasso.with(context)
                    .load(images.get(position))
                    .into(holder.image,  new Callback() {
                        @Override
                        public void onSuccess() {
                            if (holder.progress != null) {
                                holder.text.setVisibility(View.GONE);
                                holder.progress.setVisibility(View.GONE);
                                holder.image.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError() {
                            if (holder.progress != null) {
                                holder.progress.setVisibility(View.GONE);
                                holder.image.setVisibility(View.GONE);
                                holder.text.setVisibility(View.VISIBLE);
                                holder.text.setText("Error");
                                holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        final ProgressBar progress;

        public Holder (View view) {
            super(view);
            image = (ImageView) itemView.findViewById(R.id.resultImage);
            text = (TextView) itemView.findViewById(R.id.resultText);
            progress = (ProgressBar) itemView.findViewById(R.id.progressLoader);
        }
    }
}
