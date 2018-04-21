package pr.kandru.movieapp;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by pkkan on 4/9/2018.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.Holder> {
    private Context context;
    private onItemClicked onClick;
    private ResultHolder resultHolder;

    public interface onItemClicked {
        void onItemClick(Result result);
    }

    public ResultAdapter(Context context, ResultHolder results) {
        this.resultHolder = results;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) layout.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 3;
        layout.setLayoutParams(lp);

        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final int pos = position;
        if(resultHolder.getImages().get(position).equals("blank")) {
            holder.progress.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(resultHolder.getNames().get(position));
            holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        } else {
            Picasso.with(context)
                    .load(resultHolder.getImages().get(position))
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .into(holder.image,  new Callback() {
                        @Override
                        public void onSuccess() {
                            if (holder.progress != null) {
                                holder.text.setVisibility(View.GONE);
                                holder.progress.setVisibility(View.GONE);
                                holder.image.setVisibility(View.VISIBLE);
                                holder.image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onClick.onItemClick(resultHolder.get(pos));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError() {
                            if (holder.progress != null) {
                                holder.progress.setVisibility(View.GONE);
                                holder.image.setVisibility(View.GONE);
                                holder.text.setVisibility(View.VISIBLE);
                                holder.text.setText(resultHolder.getNames().get(pos));
                                holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                                holder.text.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onClick.onItemClick(resultHolder.get(pos));
                                    }
                                });
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return resultHolder.size();
    }

    public void setOnClick(onItemClicked onClick) {
        this.onClick=onClick;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        final ProgressBar progress;

        public Holder (View view) {
            super(view);
            image = itemView.findViewById(R.id.resultImage);
            text = itemView.findViewById(R.id.resultText);
            progress = itemView.findViewById(R.id.progressLoader);
        }
    }
}
