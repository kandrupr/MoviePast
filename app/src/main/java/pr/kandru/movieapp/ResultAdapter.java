package pr.kandru.movieapp;

import android.content.Context;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Class that builds into the Grid Layout in the RecyclerView in the ResultsActivity
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.Holder> {
    private final Context context;
    private onItemClicked onClick;
    private onItemPressed onItemPressed;
    private final ResultHolder resultHolder;

    /**
     * On click, the information activity is displayed
     */
    public interface onItemClicked {
        void onItemClick(Result result);
    }

    /**
     * On hold, the result name is toasted
     */
    public interface onItemPressed{
        void onItemPress(Result result);
    }

    /**
     * Constructor
     * @param context Application Context
     * @param results Results to display
     */
    public ResultAdapter(Context context, ResultHolder results) {
        this.resultHolder = results;
        this.context = context;
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
        lp.height = parent.getMeasuredHeight() / 3;         // Get exactly 1/3 to get 3 results in a row
        layout.setLayoutParams(lp);

        return new Holder(layout);
    }

    /**
     * Binds Holder item to RecyclerView
     * @param holder Holder Item
     * @param position Integer The position in the Adapter data
     */
    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final int pos = position;
        if(resultHolder.getImages().get(position).equals("blank")) {
            holder.progress.setVisibility(View.GONE);   // No poster to load, hide progressbar
            holder.image.setVisibility(View.GONE);      // Hide Image
            holder.text.setVisibility(View.VISIBLE);    // Make Text Appear
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
                                holder.text.setVisibility(View.GONE); // Successfully loaded image, Hide Text
                                holder.progress.setVisibility(View.GONE);   // Hide progressbar
                                holder.image.setVisibility(View.VISIBLE);   // Show Image
                                holder.image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onClick.onItemClick(resultHolder.get(pos));
                                    }
                                });
                                holder.image.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        onItemPressed.onItemPress(resultHolder.get(pos));
                                        return true;
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError() {
                            if (holder.progress != null) {
                                holder.progress.setVisibility(View.GONE);   // Failed to load image, hide progressbar
                                holder.image.setVisibility(View.GONE);      // Hide Image
                                holder.text.setVisibility(View.VISIBLE);    // Make Text Appear
                                holder.text.setText(resultHolder.getNames().get(pos));
                                holder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                                holder.text.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onClick.onItemClick(resultHolder.get(pos));
                                    }
                                });
                                holder.text.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        onItemPressed.onItemPress(resultHolder.get(pos));
                                        return true;
                                    }
                                });
                            }
                        }
                    });
        }
    }

    /**
     * Gets the number of items in the Adapter
     * @return Integer size of items
     */
    @Override
    public int getItemCount() {
        return resultHolder.size();
    }

    /**
     * Bind listener to activity
     * @param onClick On click action
     */
    public void setOnClick(onItemClicked onClick) {
        this.onClick=onClick;
    }

    /**
     * Bind listener to activity
     * @param onItemPressed On hold item
     */
    public void setOnPress(onItemPressed onItemPressed) {
        this.onItemPressed=onItemPressed;
    }

    /**
     * Individual Items in our RecyclerView
     */
    public static class Holder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView text;
        final ProgressBar progress;

        public Holder (View view) {
            super(view);
            image = itemView.findViewById(R.id.resultImage);
            text = itemView.findViewById(R.id.resultText);
            progress = itemView.findViewById(R.id.progressLoader);
        }
    }
}
